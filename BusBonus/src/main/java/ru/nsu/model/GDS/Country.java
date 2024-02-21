package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

//Информация об отдельной стране
@Data
@NoArgsConstructor
public class Country {

    private Integer id;

    private String name; //Название страны в том виде, в каком оно записано в справочнике Защитыинфотранса

    private String fullName;

    private String code;
}
