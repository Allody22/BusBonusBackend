package ru.nsu.payload.request;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateOwnerDataRequest {

    private String name;

    private String lastName;

    private String patronymic;

    private Date birthDate;

    private String gender;
}
