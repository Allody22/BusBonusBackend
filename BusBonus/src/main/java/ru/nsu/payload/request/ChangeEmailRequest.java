package ru.nsu.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangeEmailRequest {

    @NotBlank
    private String newEmail;

    @NotBlank
    private String codeToken;
}
