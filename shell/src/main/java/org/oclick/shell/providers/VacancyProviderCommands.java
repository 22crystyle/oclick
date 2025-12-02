package org.oclick.shell.providers;

import org.jline.terminal.Terminal;
import org.oclick.shell.auth.AuthCommands;
import org.oclick.shell.dto.TokenResponse;
import org.oclick.shell.support.model.KeycloakProperties;
import org.oclick.shell.support.model.RestKeys;
import org.springframework.http.MediaType;
import org.springframework.shell.command.annotation.Command;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Command(command = "vacancy-provider", group = "Vacancy Provider")
public class VacancyProviderCommands {
    private final RestClient restClient;
    private final KeycloakProperties keycloakProperties;
    private final Terminal terminal;
    private final AuthCommands authCommands;

    public VacancyProviderCommands(RestClient.Builder restClientBuilder, KeycloakProperties keycloakProperties, Terminal terminal, AuthCommands authCommands) {
        this.restClient = restClientBuilder.build();
        this.keycloakProperties = keycloakProperties;
        this.terminal = terminal;
        this.authCommands = authCommands;
    }

    @Command(command = "connect hh", description = "Exchanges the Keycloak token for a HeadHunter access token.")
    public String getHHToken() {
        String currentAccessToken = authCommands.getCurrentAccessToken();

        if (currentAccessToken == null) {
            return "❌ Сначала выполните вход (login).";
        }

        TokenResponse hhToken = exchangeTokenForJobboard(currentAccessToken, "hh");

        if (hhToken != null) {
            return "%n✅ Успешно! HeadHunter Access Token получен. %s".formatted(hhToken);
        } else {
            return "\n❌ Не удалось получить токен HeadHunter. Убедитесь, что аккаунт связан.";
        }
    }

    public TokenResponse exchangeTokenForJobboard(String keycloakAccessToken, String jobboardAlias) {
        String requestedResource = keycloakProperties.getJobboardResourceEndpoint(jobboardAlias);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add(RestKeys.GRANT_TYPE.toString(), "urn:ietf:params:oauth:grant-type:token-exchange");
        body.add(RestKeys.CLIENT_ID.toString(), keycloakProperties.getClientId());
        body.add(RestKeys.CLIENT_SECRET.toString(), keycloakProperties.getClientSecret());
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
}
