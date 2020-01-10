package org.egov.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CustomDiscoveryClientRouteLocator extends DiscoveryClientRouteLocator {


    private DiscoveryClient discovery;
    private ZuulProperties properties;


    public CustomDiscoveryClientRouteLocator(String servletPath, DiscoveryClient discovery, ZuulProperties properties, ServiceInstance localServiceInstance) {
        super(servletPath, discovery, properties, localServiceInstance);
        this.properties = properties;
        this.discovery = discovery;
    }

    public CustomDiscoveryClientRouteLocator(String servletPath, DiscoveryClient discovery, ZuulProperties properties, ServiceRouteMapper serviceRouteMapper, ServiceInstance localServiceInstance) {
        super(servletPath, discovery, properties, serviceRouteMapper, localServiceInstance);
        this.properties = properties;
        this.discovery = discovery;
    }

    @Override
    protected LinkedHashMap<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<>();
        routesMap.putAll(super.locateRoutes());
        if (this.discovery != null) {
            Map<String, ZuulRoute> staticServices = new LinkedHashMap<>();
            for (ZuulRoute route : routesMap.values()) {
                String serviceId = route.getServiceId();
                if (serviceId == null) {
                    serviceId = route.getId();
                }
                if (serviceId != null) {
                    staticServices.put(serviceId, route);
                }
            }
            // Add routes for discovery services by default
            List<String> services = this.discovery.getServices();
            String[] ignored = this.properties.getIgnoredServices()
                .toArray(new String[0]);
            for (String serviceId : services) {
                if (serviceId != null){
                    // Ignore specifically ignored services and those that were manually
                    // configured
                    String key = "/" + mapRouteToService(serviceId) + "/**";
                if (staticServices.containsKey(serviceId)
                    && staticServices.get(serviceId).getUrl() == null) {
                    // Explicitly configured with no URL, cannot be ignored
                    // all static routes are already in routesMap
                    // Update location using serviceId if location is null
                    ZuulRoute staticRoute = staticServices.get(serviceId);
                    if (!StringUtils.hasText(staticRoute.getLocation())) {
                        staticRoute.setLocation(serviceId);
                    }
                }
                if (!PatternMatchUtils.simpleMatch(ignored, serviceId)
                ) {
                    // Not ignored

                    if (!this.discovery.getInstances(serviceId).isEmpty()) {
                        ServiceInstance serviceInstance = this.discovery.getInstances(serviceId).get(0);
                        String servicePath =
                            "http://" + serviceInstance.getServiceId() + ":" + serviceInstance.getPort() + "/";

                        log.info("Setting up route, forwarding URI {} to service {}", key, servicePath);
                        ZuulRoute route = new ZuulRoute(key, servicePath);
                        route.setStripPrefix(false);
                        routesMap.put(key, route);
                    }
                }
            }
            }
        }
        if (routesMap.get(DEFAULT_ROUTE) != null) {
            ZuulRoute defaultRoute = routesMap.get(DEFAULT_ROUTE);
            // Move the defaultServiceId to the end
            routesMap.remove(DEFAULT_ROUTE);
            routesMap.put(DEFAULT_ROUTE, defaultRoute);
        }
        LinkedHashMap<String, ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            // Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }
}
