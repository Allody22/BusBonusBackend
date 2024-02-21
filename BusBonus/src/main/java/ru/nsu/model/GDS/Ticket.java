package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ticket {
    private Integer id;
    private String ticketCode;
    private String ticketNum;
    private String ticketSeries;
    private ETicketClass ticketClass;
    private String ticketTypeCode;
    private String raceNum;
    private String raceName;
    private Integer raceClassId;
    private String dispatchDate;
    private String dispatchStation;
    private String dispatchAddress;
    private String arrivalDate;
    private String arrivalStation;
    private String arrivalAddress;
    private String seat;
    private String lastName;
    private String firstName;
    private String middleName;
    private String docTypeCode;
    private String docSeries;
    private String docNum;
    private String citizenship;
    private String gender;
    private String birthday;
    private String phone;
    private String email;
    private String supplierCurrencyCode;
    private Double supplierFare;
    private Double supplierDues;
    private Double supplierPrice;
    private Double supplierRepayment;
    private Double price;
}
