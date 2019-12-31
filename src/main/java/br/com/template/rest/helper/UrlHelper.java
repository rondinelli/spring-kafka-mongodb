package br.com.template.rest.helper;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class UrlHelper {

    private static final String PROPERTY_APPLICATION_URL = "application.url";
    private static final String PROPERTY_APPLICATION_PROTOCOL = "application.protocol";

    @Resource
    private Environment environment;

    public String applicationUrl() {
        return environment.getProperty(PROPERTY_APPLICATION_URL);
    }

    public String applicationProtocol() {
        return environment.getProperty(PROPERTY_APPLICATION_PROTOCOL);
    }

    public String applicationBaseUrl() {
        return String.format("%s://%s", this.applicationProtocol(), this.applicationUrl());
    }

    private String fullUrl(String path) {
        return String.format("%s%s", this.applicationBaseUrl(), path);
    }

    public String usersResetPasswordUrl(String token) {
        String path = String.format("/#!/reset?token=%s", token);
        return this.fullUrl(path).replaceAll(":\\d+", "");
    }

}
