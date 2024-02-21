package ru.nsu.model.GDS;


import lombok.ToString;

@ToString
public enum ERaceStatus {

    STATUS_UNKNOWN,
    STATUS_ON_SALE,
    STATUS_CANCELED,
    STATUS_DISPATCHED,
    STATUS_BOARDING,
    STATUS_SUSPENDED;
}
