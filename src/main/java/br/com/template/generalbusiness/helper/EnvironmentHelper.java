package br.com.template.generalbusiness.helper;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class EnvironmentHelper {

    @Resource
    private Environment environment;


    /*
     * Environments
     * */

    private static final String PROPERTY_APPLICATION_ENVIRONMENT = "appplication.environment";

    public boolean isEnvironmentTest() {
        return this.environment.getProperty(PROPERTY_APPLICATION_ENVIRONMENT).equals("test");
    }

    public boolean isEnvironmentDevelopment() {
        return this.environment.getProperty(PROPERTY_APPLICATION_ENVIRONMENT).equals("development");
    }

    public boolean isEnvironmentProduction() {
        return this.environment.getProperty(PROPERTY_APPLICATION_ENVIRONMENT).equals("production");
    }


    private static final String PROPERTY_NAME_PAGINATION_SIZE = "pagination.default.size";

    public EnvironmentHelper(ApplicationContext context) {
        this.environment = context.getEnvironment();
    }

    public int getPaginationDefaultSize() {
        return Integer.parseInt(this.environment.getProperty(PROPERTY_NAME_PAGINATION_SIZE));
    }

    
}

