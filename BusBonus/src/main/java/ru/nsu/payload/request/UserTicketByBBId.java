package ru.nsu.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class UserTicketByBBId {
    @NotBlank
    private String busBonusId;
    private String serviceInfo;
    private String raceStatus;
    private String raceNumber;
    private String raceName;
    private String raceUid;
    private String dispatchStation;
    private String arrivalStation;
    private Integer raceClassId;
    private String dispatchDate;
    private String arrivalDate;
    private String dispatchAddress;
    private String arrivalAddress;
    private String carrier;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String phone;
    private String documentType;
    private String documentNumber;
    private String documentSeries;
    private String citizenship;
    private String supplierCurrencyCode;
    private Double supplierFare;
    private Double supplierDues;
    private Double supplierPrice;
    private Double supplierRepayment;
    private String currencyCode;
    private Double price;
    private String ticketStatus;
    private String ticketClass;
    private String ticketType;
    private Date purchaseDate;
    private String ticketNum;
    private String ticketSeries;
    private String busInfo;
    private String userSeat;

}
