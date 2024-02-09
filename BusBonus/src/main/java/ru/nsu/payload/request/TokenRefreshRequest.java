package ru.nsu.payload.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRefreshRequest {

    @NotBlank
    private String refresh_token;

    @NotBlank
    private String grant_type;
}
