package ru.nsu.model.GDS;


import lombok.ToString;

@ToString
public enum EOrderStatus {
    STATUS_BOOKED,
    STATUS_CREATED,
    STATUS_ERROR,
    STATUS_RETURNED,
    STATUS_SOLD;
}
