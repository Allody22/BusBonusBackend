package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Seat {
    private String code;
    private String name;
    private String type;
}
