package org.oclick.shell;

import org.jline.terminal.Terminal;
import org.oclick.shell.config.KeycloakProperties;
import org.oclick.shell.dto.DeviceAuthResponse;
import org.oclick.shell.dto.TokenResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@ShellComponent
public class AuthCommands {
    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_SECRET_KEY = "client_secret";
    private static final String GRANT_TYPE_KEY = "grant_type";

    private final RestClient restClient;
    private final KeycloakProperties keycloakProperties;
    private final Terminal terminal;
    private String currentAccessToken;

    public AuthCommands(RestClient.Builder restClientBuilder, KeycloakProperties keycloakProperties, Terminal terminal) {
        this.restClient = restClientBuilder.build();
        this.keycloakProperties = keycloakProperties;
        this.terminal = terminal;
    }

    @ShellMethod(key = "login", value = "Initiates Keycloak Device Authorization Grant flow.")
    public String login() {
        DeviceAuthResponse deviceAuthResponse = requestDeviceCode();

        if (deviceAuthResponse == null) {
            return "❌ Не удалось запустить авторизацию.";
        }

        terminal.writer().printf("%n🔥 Для авторизации перейдите по ссылке и введите код:%n");
        terminal.writer().printf("   URL: %s%n", deviceAuthResponse.verificationUri());
        terminal.writer().printf("   КОД: %s%n", deviceAuthResponse.userCode());
        terminal.writer().printf("   ПОЛНЫЙ URL: %s%n", deviceAuthResponse.verificationUriComplete());
        terminal.writer().printf("   Ожидание: %s секунд. Проверка каждые %s с.%n", deviceAuthResponse.expiresIn(), deviceAuthResponse.interval());
        terminal.writer().flush();

        TokenResponse tokenResponse = pollForToken(deviceAuthResponse);

        if (tokenResponse != null) {
            this.currentAccessToken = tokenResponse.accessToken();
            return "\n✅ Успешная авторизация! Access Token получен.\n";
        } else {
            return "\n❌ Авторизация не завершена или время вышло.";
        }
    }

    private DeviceAuthResponse requestDeviceCode() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(CLIENT_ID_KEY, keycloakProperties.getClientId());
        body.add(CLIENT_SECRET_KEY, keycloakProperties.getClientSecret());

        try {
            return restClient.post()
                    .uri(keycloakProperties.getDeviceAuthEndpoint())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(DeviceAuthResponse.class);
        } catch (RestClientException e) {
            terminal.writer().printf("Ошибка запроса Device Auth: %s%n", e.getMessage());
            terminal.writer().flush();
            return null;
        }
    }

    private TokenResponse pollForToken(DeviceAuthResponse deviceAuthResponse) {
        long startTime = System.currentTimeMillis();
        long expiryTime = startTime + (deviceAuthResponse.expiresIn() * 1000L);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE_KEY, "urn:ietf:params:oauth:grant-type:device_code");
        body.add(CLIENT_ID_KEY, keycloakProperties.getClientId());
        body.add(CLIENT_SECRET_KEY, keycloakProperties.getClientSecret());
        body.add("device_code", deviceAuthResponse.deviceCode());

        while (System.currentTimeMillis() < expiryTime && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(deviceAuthResponse.interval() * 1000L);

                return restClient.post()
                        .uri(keycloakProperties.getTokenEndpoint())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(body)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                            throw new ShellTokenPendingException();
                        })
                        .body(TokenResponse.class);

            } catch (ShellTokenPendingException e) {
                terminal.writer().print(".");
                terminal.writer().flush();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                terminal.writer().print("\n🚫 Опрос токена прерван пользователем (Ctrl+C).");
                terminal.writer().flush();
                return null;
            } catch (RestClientException e) {
                terminal.writer().printf("Ошибка при получении токена: %s", e.getMessage());
                terminal.writer().flush();
                return null;
            }
        }
        return null;
    }

    @ShellMethod(key = "vacancy-provider connect hh", value = "Exchanges the Keycloak token for a HeadHunter access token.")
    //TODO: separate by namespaces
    public String getHHToken() {
        if (this.currentAccessToken == null) {
            return "❌ Сначала выполните вход (login).";
        }

        TokenResponse hhToken = exchangeTokenForJobboard(this.currentAccessToken, "hh-oauth2");

        if (hhToken != null) {
            return "%n✅ Успешно! HeadHunter Access Token получен. %s".formatted(hhToken);
        } else {
            return "\n❌ Не удалось получить токен HeadHunter. Убедитесь, что аккаунт связан.";
        }
    }

    public TokenResponse exchangeTokenForJobboard(String keycloakAccessToken, String jobboardAlias) {
        String requestedResource = keycloakProperties.getJobboardResourceEndpoint(jobboardAlias);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add(GRANT_TYPE_KEY, "urn:ietf:params:oauth:grant-type:token-exchange");
        body.add(CLIENT_ID_KEY, keycloakProperties.getClientId());
        body.add(CLIENT_SECRET_KEY, keycloakProperties.getClientSecret());
        body.add("subject_token", keycloakAccessToken);
        body.add("subject_token_type", "urn:ietf:params:oauth:token-type:access_token");
        body.add("requested_token_type", "urn:ietf:params:oauth:token-type:access_token");
        body.add("resource", requestedResource);

        try {
            return restClient.post()
                    .uri(keycloakProperties.getTokenEndpoint())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(TokenResponse.class);
        } catch (RestClientException e) {
            terminal.writer().printf("❌ Ошибка при обмене токена на %s: %s", jobboardAlias, e.getMessage());
            terminal.writer().flush();
            return null;
        }
    }

    private static class ShellTokenPendingException extends RuntimeException {
    }
}
