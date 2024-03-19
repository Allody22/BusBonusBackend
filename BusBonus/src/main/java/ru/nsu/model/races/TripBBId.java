package ru.nsu.model.races;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "current_status")
    private String currentTicketStatus;

    @Column(name = "ticket_series", unique = true)
    private String ticketSeries;

    @Column(name = "supplier_price")
    private Double supplierPrice;

    @Column(name = "ticket_nubmer", unique = true)
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

    @Column(name = "supplier_dues")
    private Double supplierDues;

    @Column(name = "supplier_fare")
    private Double supplierFare;
}
