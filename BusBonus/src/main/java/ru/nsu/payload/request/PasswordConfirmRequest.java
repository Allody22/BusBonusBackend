package ru.nsu.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordConfirmRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String codeToken;
}
