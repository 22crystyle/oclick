package org.oclick.shell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "keycloak.client")
public class KeycloakProperties {
    private static final String REALM_URL = "/realms/";
    private String baseUrl;
    private String realm;
    private String clientId;

    public String getDeviceAuthEndpoint() {
        return baseUrl + REALM_URL + realm + "/protocol/openid-connect/auth/device";
    }

    public String getTokenEndpoint() {
        return baseUrl + REALM_URL + realm + "/protocol/openid-connect/token";
    }

    public String getIdentityProviderEndpoint() {
        return baseUrl + REALM_URL + realm + "/broker";
    }
}
