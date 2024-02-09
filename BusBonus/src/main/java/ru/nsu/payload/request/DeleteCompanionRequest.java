package ru.nsu.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeleteCompanionRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    private String patronymic;

    @NotBlank
    private String citizenship;

    @NotBlank
    private String documentType;

    @NotBlank
    private String documentNumber;

    @NotBlank
    private String documentSeries;
}
