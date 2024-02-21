package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Point {
    private Integer id;
    private String name;
    private String region;
    private String address;
    private String place;
}
