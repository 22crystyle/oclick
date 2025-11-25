package org.oclick.shell.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeviceAuthResponse(
        @JsonProperty("device_code") String deviceCode,
        @JsonProperty("user_code") String userCode,
        @JsonProperty("verification_uri") String verificationUri,
        @JsonProperty("verification_uri_complete") String verificationUriComplete,
        @JsonProperty("expires_in") int expiresIn,
        @JsonProperty("interval") int interval) {
}
