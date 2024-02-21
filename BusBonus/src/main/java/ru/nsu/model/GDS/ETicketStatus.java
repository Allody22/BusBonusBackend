package ru.nsu.model.GDS;

import lombok.ToString;

@ToString
public enum ETicketStatus {
    STATUS_BOOKED,
    STATUS_CANCELLED,
    STATUS_RETURNED,
    STATUS_SOLD;
}
