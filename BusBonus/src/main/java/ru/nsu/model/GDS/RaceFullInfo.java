package ru.nsu.model.GDS;

import lombok.Data;

import java.util.List;

@Data
public class RaceFullInfo {
    private DepotInfo depot;
    private Race race;
    private List<Stop> stops;
    private List<Seat> seats;
    private List<TicketType> ticketTypes;
    private List<DocType> docTypes;
}
