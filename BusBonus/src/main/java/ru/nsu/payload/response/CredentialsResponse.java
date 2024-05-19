package ru.nsu.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CredentialsResponse {

    private String login;

    private String password;
}
