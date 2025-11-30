package org.oclick.libs.shared.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class GatewayHeaderRequestMatcher implements RequestMatcher {
    private final String headerName;
    private final String headerValue;

    public GatewayHeaderRequestMatcher(final String headerName, final String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return headerValue.equals(request.getHeader(headerName));
    }
}
