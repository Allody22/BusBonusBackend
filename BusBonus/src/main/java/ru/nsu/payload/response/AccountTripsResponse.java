package ru.nsu.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTripsResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bookingTime;

    private String ticketSource;

    private Double price;

    private String busInfo;

    private String ticketSeries;

    private String currentTicketStatus;

    private String ticketNumber;

    private String ticketType;

    private String ticketClass;

    private String ticketCode;

    private String downloadURL;

    private String seat;

    private Double dues;

    private Double vat;

    private String currentRaceStatus;

    private String dispatchDate;

    private String arrivalDate;

    private String arrivalPoint;

    private String dispatchPoint;

    private String dispatchAddress;

    private String arrivalAddress;

    private String carrierName;

    private String documentType;

    private String documentSeries;

    private String documentNumber;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String phone;

    private Double supplierPrice;
}
