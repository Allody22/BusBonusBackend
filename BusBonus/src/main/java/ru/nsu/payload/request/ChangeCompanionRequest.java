package ru.nsu.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ChangeCompanionRequest {

    private String name;

    private String lastName;

    private String patronymic;

    private Date birthDate;

    private String gender;

    private String documentType;

    private String documentNumber;

    private String issuingAuthority;

    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date issueDate;

    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date expirationDate;

    private String additionalData;
}
