package org.oclick.shell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "keycloak.client")
public class KeycloakProperties {
    private static final String REALMS_PATH = "realms";
    private static final String BROKER_PATH = "broker";
    private String baseUrl;
    private String realm;
    private String clientId;
    private String clientSecret;

    public String getDeviceAuthEndpoint() {
        return baseUrl + "/" + REALMS_PATH + "/" + realm + "/protocol/openid-connect/auth/device";
    }

    public String getTokenEndpoint() {
        return baseUrl + "/" + REALMS_PATH + "/" + realm + "/protocol/openid-connect/token";
    }

    public String getJobboardResourceEndpoint(String jobboardAlias) {
        return baseUrl + "/" + BROKER_PATH + "/" + jobboardAlias;
    }
}
