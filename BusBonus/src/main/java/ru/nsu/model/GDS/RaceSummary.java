package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class RaceSummary {
    private DepotInfo depot;
    private Race race;
    private List<Stop> stops;
    private List<Seat> seats;
    private List<TicketType> ticketTypes;
    private List<DocType> docTypes;
}
