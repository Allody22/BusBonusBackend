package ru.nsu.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.nsu.model.constants.ETripStatuses;

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

    @Column(name = "bus_driver")
    private String busDriver;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private ETripStatuses currentStatus;

    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private final List<TripBBId> tripUsers = new ArrayList<>();
}
