package org.egov.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
public class KubernetesServiceRouteMapper implements ServiceRouteMapper {

    private DiscoveryClient discoveryClient;

    KubernetesServiceRouteMapper(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    private static final String SERVICE_META_KEY = "zuul/route-path";

    @Override
    public String apply(String serviceId) {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
        String routePath = "";
        for(ServiceInstance instance : serviceInstances){
            routePath = instance.getMetadata().getOrDefault(SERVICE_META_KEY, serviceId);

        }
        routePath = StringUtils.hasText(routePath) ? routePath : serviceId;
        log.info("Routing {} to service {}", routePath, serviceId);
        return routePath;

    }
}
