package ru.nsu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.model.user.Account;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "trip_bb_id")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TripBBId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Column(name = "ticket_source")
    private String ticketSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "booking_time")
    private Date bookingTime;

    @Column(name = "current_status")
    private String currentTicketStatus;

    @Column(name = "ticket_series")
    private String ticketSeries;

    @Column(name = "ticket_nubmer")
    private String ticketNumber;

    @Column(name = "ticket_type")
    private String ticketType;

    @Column(name = "ticket_class")
    private String ticketClass;

    @Column(name = "ticket_code")
    private String ticketCode;

    @Column(name = "ticket_price")
    private Double price;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_series")
    private String documentSeries;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "download_url")
    private String downloadURL;

    @Column(name = "dues")
    private Double dues;

    @Column(name = "seat")
    private String seat;

    @Column(name = "vat")
    private Double vat;

    @Column(name = "updatable")
    private Boolean updatable;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "phone")
    private String phone;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "citizenship")
    private String citizenship;

    @Column(name = "gender")
    private String gender;

    @Column(name = "platform")
    private String platform;

    @Column(name = "supplier_dues")
    private Double supplierDues;

    @Column(name = "supplier_fare")
    private Double supplierFare;

    @Column(name = "supplier_price")
    private Double supplierPrice;

    @Column(name = "dispatch_point")
    private String dispatchPoint;

    @Column(name = "arrival_point")
    private String arrivalPoint;

    @Column(name = "arrival_address")
    private String arrivalAddress;

    @Column(name = "dispatch_address")
    private String dispatchAddress;

    @Column(name = "dispatch_date")
    private String dispatchDate;

    @Column(name = "arrival_date")
    private String arrivalDate;
}
