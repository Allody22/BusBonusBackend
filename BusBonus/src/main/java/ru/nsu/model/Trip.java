package ru.nsu.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_status")
    private String currentRaceStatus;

    @Column(name = "dispatch_point")
    private String dispatchPoint;

    @Column(name = "arrival_point")
    private String arrivalPoint;

    @Column(name = "dispatch_date")
    private String dispatchDate;

    @Column(name = "arrival_date")
    private String arrivalDate;

    @Column(name = "carrier_name")
    private String carrierName;

    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<TripBBId> tripUsers = new ArrayList<>();
}
