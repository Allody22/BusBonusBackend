package ru.nsu.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRefreshResponse {

    private final String accessToken;

    private final String refreshToken;

    private final String tokenType = "Bearer";

    private final Long expires_in;

    public TokenRefreshResponse(String accessToken, String refreshToken, Long expires_in) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expires_in = expires_in;
    }
}
