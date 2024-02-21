package ru.nsu.payload.request;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class AuthRequest {

    @NotBlank
    private final String login = null;

    @NotBlank
    private final String password = null;
}
