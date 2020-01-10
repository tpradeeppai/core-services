package org.egov.config;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.utils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.kubernetes.discovery.KubernetesDiscoveryClient;
import org.springframework.cloud.kubernetes.discovery.KubernetesDiscoveryProperties;
import org.springframework.cloud.kubernetes.discovery.KubernetesServiceInstance;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class CustomKubernetesDiscoveryClient implements DiscoveryClient {

    private static final Log log = LogFactory.getLog(KubernetesDiscoveryClient.class);

    private static final String HOSTNAME = "HOSTNAME";
    private final KubernetesDiscoveryProperties properties;

    private KubernetesClient client;

    public CustomKubernetesDiscoveryClient(KubernetesClient client,
                                     KubernetesDiscoveryProperties kubernetesDiscoveryProperties) {

        this.client = client;
        this.properties = kubernetesDiscoveryProperties;
    }

    public KubernetesClient getClient() {
        return this.client;
    }

    public void setClient(KubernetesClient client) {
        this.client = client;
    }

    @Override
    public ServiceInstance getLocalServiceInstance() {

        String serviceName = properties.getServiceName();
        String podName = System.getenv(HOSTNAME);
        ServiceInstance defaultInstance = new DefaultServiceInstance(serviceName,
            "localhost",
            8080,
            false);

        Endpoints endpoints = client.endpoints().withName(serviceName).get();
        Optional<Service> service = Optional.ofNullable(client.services().withName(serviceName).get());
        final Map<String, String> labels;
        if (service.isPresent()) {
            labels = service.get().getMetadata().getLabels();
        } else {
            labels = null;
        }
        if (Utils.isNullOrEmpty(podName) || endpoints == null) {
            return defaultInstance;
        }
        try {
            List<EndpointSubset> subsets = endpoints.getSubsets();

            if (subsets != null) {
                for (EndpointSubset s : subsets) {
                    List<EndpointAddress> addresses = s.getAddresses();
                    for (EndpointAddress a : addresses) {
                        return new KubernetesServiceInstance(serviceName,
                            a,
                            s.getPorts().stream().findFirst().orElseThrow(IllegalStateException::new),
                            labels,
                            false);
                    }
                }
            }
            return defaultInstance;

        } catch (Throwable t) {
            return defaultInstance;
        }
    }

    @Override
    public String description() {
        return "Custom Kubernetes Discovery Client";
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        Assert.notNull(serviceId,
            "[Assertion failed] - the object argument must not be null");

        Endpoints endpoints = this.client.endpoints().withName(serviceId).get();
        List<EndpointSubset> subsets = getSubsetsFromEndpoints(endpoints);
        List<ServiceInstance> instances = new ArrayList<>();
        if (!subsets.isEmpty()) {

            final Service service = this.client.services().withName(serviceId).get();

            if (log.isDebugEnabled()) {
                log.debug("Adding label metadata: " + service.getMetadata().getLabels());
            }
            final Map<String, String> serviceMetadata = new HashMap<>(service.getMetadata().getLabels());

                if (log.isDebugEnabled()) {
                    log.debug("Adding annotation metadata: " + service.getMetadata().getAnnotations());
                }
                if(service.getMetadata().getAnnotations() != null)
                    serviceMetadata.putAll(service.getMetadata().getAnnotations());


            for (EndpointSubset s : subsets) {
                // Extend the service metadata map with per-endpoint port information (if
                // requested)
                Map<String, String> endpointMetadata = new HashMap<>(serviceMetadata);
                    Map<String, String> ports = s.getPorts().stream()
                        .filter(port -> !StringUtils.isEmpty(port.getName()))
                        .collect(toMap(EndpointPort::getName,
                            port -> Integer.toString(port.getPort())));
                    if (log.isDebugEnabled()) {
                        log.debug("Adding port metadata: " + ports);
                    }
                    endpointMetadata.putAll(ports);


                List<EndpointAddress> addresses = s.getAddresses();
                for (EndpointAddress endpointAddress : addresses) {

                    instances.add(new KubernetesServiceInstance(serviceId,
                        endpointAddress,
                        s.getPorts().stream().findFirst().orElseThrow(IllegalStateException::new),
                        endpointMetadata, false));
                }
            }
        }

        return instances;
    }


    private List<EndpointSubset> getSubsetsFromEndpoints(Endpoints endpoints) {
        if (endpoints == null) {
            return new ArrayList<>();
        }
        if (endpoints.getSubsets() == null) {
            return new ArrayList<>();
        }

        return endpoints.getSubsets();
    }

    @Override
    public List<String> getServices() {
        return client.services().list()
            .getItems()
            .stream().map(s -> s.getMetadata().getName())
            .collect(Collectors.toList());
    }

}
