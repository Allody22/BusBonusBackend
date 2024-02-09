package ru.nsu.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserDataResponse {

    private String name;

    private String lastName;

    private String patronymic;

    private Date birthDate;

    private String gender;

    // Пассажир или владелец
    private String typeName;

    //Активный или нет
    private String status;

    private String documentNumber;

    private String documentSeries;

    private String issuingAuthority;

    private Date issueDate;

    private Date expirationDate;

    private String additionalData;

    private String citizenship;

    private String confirmationOfBenefits;

    private String documentType;

    private String ticketType;
}
