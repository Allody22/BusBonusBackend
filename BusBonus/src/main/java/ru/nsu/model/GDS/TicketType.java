package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketType {
    private String code;
    private String name;
    private Double price;
}
