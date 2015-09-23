package com.dvcoders;

import com.dvcoders.config.AppConfiguration;
import com.dvcoders.filter.CommonRequestFilter;
import com.dvcoders.filter.CommonResponseFilter;
import com.dvcoders.other.RootExceptionMapper;
import com.dvcoders.other.Views;
import com.dvcoders.resource.GithubResources;
import com.dvcoders.resource.UserResources;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * @author Jake Loo (03 July, 2015)
 */
public class App extends Application<AppConfiguration> {
    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public void run(AppConfiguration appConfiguration, Environment environment) throws Exception {
        // object mapper
        ObjectMapper mapper = environment.getObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setConfig(mapper.getSerializationConfig().withView(Views.Public.class));

        // cors
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");

        // exception mapper
        environment.jersey().register(new RootExceptionMapper());

        // resources
        environment.jersey().register(new UserResources());
        environment.jersey().register(new GithubResources());

        // filter
        environment.jersey().register(CommonResponseFilter.class);
        environment.jersey().register(CommonRequestFilter.class);

    }

}
