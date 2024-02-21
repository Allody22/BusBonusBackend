package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

//информация об одном отдельном автовокзале
@Data
@NoArgsConstructor
public class DepotInfo {
    private Long id; // ID автовокзала

    private String name; // Название автовокзала

    private String address; // Адрес автовокзала

    private String phones; // Контактные телефоны автовокзала

    private String site; // Сайт автовокзала

    private String workingTime; // Режим работы автовокзала

    private String info; // Дополнительная информация об автовокзале

    private String printInfo; // Информация о порядке печати билетов

    private String returnInfo; // Информация о порядке выполнения возвратов

    private String latitude; // Координаты автовокзала: широта

    private String longitude; // Координаты автовокзала: долгота

    private String timezone; // Временная зона, в которой находится автовокзал (хост автовокзала)

    private String engine; // Тип сервера, используемый автовокзалом

    private String version; // Версия сервера, используемая автовокзалом

    private String features; // Описание особых возможностей автовокзала

    private int ticketLimit; // Максимальное количество билетов, которое можно забронировать за один раз

    private int bookingTimeLimit; // Количество минут до отправления автобуса, за которое прекращается продажа билетов на рейс

    private boolean phoneRequired; // Признак обязательности ввода номера контактного телефона пассажира

    private boolean online; // Признак доступности автовокзала

    private String currencyCode; // Валюта, в которой работает вокзал

    private boolean updatableTicket; // Флаг возможности изменить данные в билете
}
