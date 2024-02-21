package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Race {
    private Integer depotId;
    private String num;
    private String name;
    private String description;
    private String dispatchDate;
    private String arrivalDate;
    private String dispatchStationName;
    private String arrivalStationName;
    private Integer dispatchPointId;
    private Integer arrivalPointId;
    private Double supplierPrice;
    private Double price;
    private Integer freeSeatCount;
    private String freeSeatEstimation;
    private String busInfo;
    private String carrier;
    private RaceType type;
    private RaceClass clazz;
    private RaceStatus status;
}
