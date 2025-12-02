package org.oclick.shell.auth;

import lombok.Getter;
import org.jline.terminal.Terminal;
import org.oclick.shell.dto.DeviceAuthResponse;
import org.oclick.shell.dto.TokenResponse;
import org.oclick.shell.support.exception.ShellTokenPendingException;
import org.oclick.shell.support.model.KeycloakProperties;
import org.oclick.shell.support.model.RestKeys;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.shell.command.annotation.Command;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Command(
        command = "auth",
        group = "Auth"
)
public class AuthCommands {
    private final RestClient restClient;
    private final KeycloakProperties keycloakProperties;
    private final Terminal terminal;
    @Getter
    private String currentAccessToken;

    public AuthCommands(RestClient.Builder restClientBuilder, KeycloakProperties keycloakProperties, Terminal terminal) {
        this.restClient = restClientBuilder.build();
        this.keycloakProperties = keycloakProperties;
        this.terminal = terminal;
    }


    @Command(command = "login", description = "Login using device flow, through Keycloak")
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

    private TokenResponse pollForToken(DeviceAuthResponse deviceAuthResponse) {
        long startTime = System.currentTimeMillis();
        long expiryTime = startTime + (deviceAuthResponse.expiresIn() * 1000L);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(RestKeys.GRANT_TYPE.toString(), "urn:ietf:params:oauth:grant-type:device_code");
        body.add(RestKeys.CLIENT_ID.toString(), keycloakProperties.getClientId());
        body.add(RestKeys.CLIENT_SECRET.toString(), keycloakProperties.getClientSecret());
        body.add("device_code", deviceAuthResponse.deviceCode());

        while (System.currentTimeMillis() < expiryTime && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(deviceAuthResponse.interval() * 1000L); //TODO: to find another way to poll token

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

    private DeviceAuthResponse requestDeviceCode() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(RestKeys.CLIENT_ID.toString(), keycloakProperties.getClientId());
        body.add(RestKeys.CLIENT_SECRET.toString(), keycloakProperties.getClientSecret());

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
}
