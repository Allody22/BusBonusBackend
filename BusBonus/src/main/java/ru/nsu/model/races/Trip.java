package ru.nsu.model.races;


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

    @Column(name = "race_name")
    private String raceName;

    @Column(name = "race_number")
    private String raceNumber;

    @Column(name = "uid", unique = true)
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
    private List<Order> orders = new ArrayList<>();
}
