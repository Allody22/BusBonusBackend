package ru.nsu.model.GDS;

import lombok.ToString;

@ToString
public enum ERaceType {
    TYPE_UNKNOWN,
    TYPE_INTERURBAN,
    TYPE_INTERREGIONAL,
    TYPE_INTERNATIONAL,
    TYPE_SUBURBAN;
}
