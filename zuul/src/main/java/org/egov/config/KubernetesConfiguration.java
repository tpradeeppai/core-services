package org.egov.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.kubernetes.discovery.KubernetesCatalogWatch;
import org.springframework.cloud.kubernetes.discovery.KubernetesDiscoveryProperties;
import org.springframework.cloud.kubernetes.registry.KubernetesRegistration;
import org.springframework.cloud.kubernetes.registry.KubernetesServiceRegistry;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("kubernetes")
public class KubernetesConfiguration {
    @Bean
    public DiscoveryClient discoveryClient(KubernetesClient client,
                                           KubernetesDiscoveryProperties properties) {
        return new CustomKubernetesDiscoveryClient(client, properties);
    }

    @Bean
    public ServiceRouteMapper kubernetesServiceRouteMapper(DiscoveryClient discoveryClient){
        return new KubernetesServiceRouteMapper(discoveryClient);
    }

    @Bean
    public DiscoveryClientRouteLocator discoveryRouteLocator(ServerProperties serverProperties,
                                                             DiscoveryClient discoveryClient,
                                                             ZuulProperties zuulProperties,
                                                             ServiceRouteMapper serviceRouteMapper, Registration registration) {
        return new CustomDiscoveryClientRouteLocator(serverProperties.getServletPrefix(),
            discoveryClient, zuulProperties, serviceRouteMapper, registration);
    }

    @Bean
    public KubernetesServiceRegistry getServiceRegistry() {
        return new KubernetesServiceRegistry();
    }

    @Bean
    public KubernetesRegistration getRegistration(KubernetesClient client,
                                                  KubernetesDiscoveryProperties properties) {
        return new KubernetesRegistration(client, properties);
    }

    @Bean
    @Primary
    public KubernetesDiscoveryProperties getKubernetesDiscoveryProperties() {
        return new KubernetesDiscoveryProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "spring.cloud.kubernetes.discovery.catalog-services-watch.enabled", matchIfMissing = true)
    public KubernetesCatalogWatch kubernetesCatalogWatch(KubernetesClient client) {
        return new KubernetesCatalogWatch(client);
    }

}
