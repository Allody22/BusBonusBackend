package ru.nsu.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewTicketsRequest {

    @NotBlank
    @Schema(description = "Уникальный бас бонус айди к аккаунту", required = true)
    private String busBonusId;

    @Schema(description = "Дата покупки. Сейчас автоматически ставится текущее время и можно не отправлять этот аргумент")
    private Date purchaseDate;

    @Schema(description = "Статус рейса", example = "Продажа")
    private String raceStatus;

    @Schema(description = "Названия номера маршрута", example = "575Б")
    private String raceNumber;

    @Schema(description = "Название маршрута", example = "Новосибирск ЖД Вокзал - Барнаул АВ")
    private String raceName;

    @Schema(description = "Уникальный айди рейса", example = "141599834:769449:20240228:649:16")
    @NotBlank
    private String raceUid;

    @Schema(description = "Название станции отправления", example = "Новосибирск ЖД 2")
    private String dispatchPoint;


    @Schema(description = "Дата и время прибытия", example = "2024-02-28 13:19:00")
    private String arrivalDate;

    @Schema(description = "Адрес отправления", example = "nул. Большевистская, 12/1")
    private String dispatchAddress;


    @Schema(description = "Набор билетов привязанных к данному чеку")
    private List<UserTicketByBBId> userTicketByBBIdList;
}
