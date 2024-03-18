package ru.nsu.model.races;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.nsu.model.user.Account;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "order")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<TripBBId> orderTickets = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Column(name = "ticket_source")
    private String ticketSource; //Алгоритм определения TripSource

    @Column(name = "baggage_tickets_number")
    private int baggageTicketsNumber = 0;

    @Column(name = "passenger_tickets_number")
    private int passengerTicketsNumber = 0;

    @Column(name = "booking_time")
    private Date bookingTime;

    @Column(name = "platform")
    private String platform;

    @Column(name = "dispatch_point")
    private String dispatchPoint;

    @Column(name = "arrival_point")
    private String arrivalPoint;

    @Column(name = "arrival_address")
    private String arrivalAddress;

    @Column(name = "dispatch_address")
    private String dispatchAddress;

    @Column(name = "dispatch_date")
    private LocalDateTime dispatchDate;

    @Column(name = "arrival_date")
    private LocalDateTime arrivalDate;

    @Column(name = "cheque_download_url")
    private String chequeDownloadUrl;
}
