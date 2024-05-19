package ru.nsu.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AuthRequest {

    @NotBlank
    private final String login = null;

    @NotBlank
    private final String password = null;

    @NotBlank
    private final String fingerPrint;

}
