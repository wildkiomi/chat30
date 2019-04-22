package swag;

import io.swagger.jaxrs.config.BeanConfig;
import rest.Agents;
import rest.Clients;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class SwaggerApp extends Application {

    public SwaggerApp() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("rest");
        beanConfig.setScan(true);
    }
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet();
        resources.add(Agents.class);
        resources.add(Clients.class);
        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        return resources;
    }

}