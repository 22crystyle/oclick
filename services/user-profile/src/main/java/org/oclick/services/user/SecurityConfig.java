package org.oclick.services.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new GatewayHeaderRequestMatcher("X-Internal-Gateway", "true")).permitAll()
                        .anyRequest().denyAll()
                ).build();
    }

    static class GatewayHeaderRequestMatcher implements RequestMatcher {
        private final String headerName;
        private final String headerValue;

        public GatewayHeaderRequestMatcher(String headerName, String headerValue) {
            this.headerName = headerName;
            this.headerValue = headerValue;
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            return headerValue.equals(request.getHeader(headerName));
        }
    }
}
