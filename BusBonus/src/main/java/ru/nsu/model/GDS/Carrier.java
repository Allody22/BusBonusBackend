package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Carrier {
    private String name;

    private String director;

    private String phone;

    private String email;

    private String site;

    private String workMode;
}
