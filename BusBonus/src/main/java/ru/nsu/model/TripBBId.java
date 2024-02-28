package ru.nsu.model;

import lombok.AllArgsConstructor;
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

    @Column(name = "ticket_category")
    private String ticketCategory;

    @Column(name = "ticket_price")
    private Double price;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_series")
    private String documentSeries;

    @Column(name = "document_number")
    private String documentNumber;
}
