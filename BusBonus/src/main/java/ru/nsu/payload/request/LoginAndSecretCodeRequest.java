package ru.nsu.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginAndSecretCodeRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String codeToken;
}
