package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Stop {
    private String name;
    private String regionName;
    private String arrivalDate;
    private String dispatchDate;
    private Integer stopTime;
    private Integer distance;
    private String address;
}
