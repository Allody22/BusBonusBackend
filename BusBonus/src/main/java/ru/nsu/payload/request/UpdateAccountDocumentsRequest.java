package ru.nsu.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateAccountDocumentsRequest {

    private String documentType;

    private String documentNumber;

    private String documentSeries;

    private String issuingAuthority;

    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date issueDate;

    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date expirationDate;

    private String additionalData;

    private String citizenship;

    private String confirmationOfBenefits;

    private String ticketCategory;
}
