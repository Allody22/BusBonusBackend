package ru.nsu.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_status")
    private String currentRaceStatus;

//    @Column(name = "dispatch_final_point")
//    private String dispatchFinalPoint;
//
//    @Column(name = "arrival_final_point")
//    private String arrivalFinalPoint;
//
//    @Column(name = "arrival_final_address")
//    private String arrivalFinalAddress;
//
//    @Column(name = "dispatch_final_address")
//    private String dispatchFinalAddress;

//    @Column(name = "dispatch_final_date")
//    private String dispatchFinalDate;
//
//    @Column(name = "arrival_final_date")
//    private String arrivalFinalDate;

    @Column(name = "name")
    private String name;

    @Column(name = "num")
    private String num;

    @Column(name = "uid")
    private String uid;

    @Column(name = "bus_info")
    private String busInfo;

    @Column(name = "carrier_name")
    private String carrierName;

    @Column(name = "carrier_inn")
    private String carrierINN;

    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<TripBBId> tripUsers = new ArrayList<>();
}
