package ru.nsu.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class AddCompanionRequest {

    private String name;

    private String lastName;

    private String patronymic;

    private Date birthDate;

    private String gender;

    @NotBlank
    private String documentType;

    @NotBlank
    private String documentNumber;

    @NotBlank
    private String documentSeries;

    private String issuingAuthority;

    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date issueDate;

    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date expirationDate;

    private String additionalData;

    private String confirmationOfBenefits;

    @NotBlank
    private String citizenship;

    private String ticketCategory;
}
