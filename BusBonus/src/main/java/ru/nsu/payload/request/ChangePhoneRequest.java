package ru.nsu.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePhoneRequest {

    @NotBlank
    private String login;

    @NotBlank
    private String newPhone;

    @NotBlank
    private String codeToken;
}
