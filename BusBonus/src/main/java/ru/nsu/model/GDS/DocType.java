package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

//Разные типы рассмотреть, указать
@Data
@NoArgsConstructor
public class DocType {

    private String code; // Код типа документа

    private String name; // Название типа документа

    private String type; // Код типа документа по стандарту Минтранса

    private String benefitCode; // Идентификатор льготы
}
