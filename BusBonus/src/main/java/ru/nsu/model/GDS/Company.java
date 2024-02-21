package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Company {

    private Integer id; // Идентификатор организации

    private String fullName; // Полное название организации

    private String inn; // ИНН организации

    private Double markup; // Процент наценки при продаже билетов. При значении null или 0 наценки нет

    private Double extra; // Сумма наценки при продаже билетов. При значении null или 0 наценки нет

    private String currencyCode; // Символьный код валюты. При значении null или 0 наценки нет
}
