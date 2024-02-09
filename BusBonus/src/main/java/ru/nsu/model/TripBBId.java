package ru.nsu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.model.constants.ETicketsCategory;
import ru.nsu.model.user.Account;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @JsonFormat(timezone = "Asia/Novosibirsk")
    @Column(name = "booking_time")
    private Date bookingTime;

    @Column(name = "current_status")
    private String currentStatus;

    @Column(name = "ticket_category")
    @Enumerated(EnumType.STRING)
    private ETicketsCategory ticketCategory;
}
