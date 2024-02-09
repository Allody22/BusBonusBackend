package ru.nsu.model.constants;

import lombok.ToString;

@ToString
public enum ETripStatuses {
    DONE, // Сделан
    CANCELLED, // Отменён
    UNKNOWN, // Неизвестно
    ON_THE_WAY // В пути
}
