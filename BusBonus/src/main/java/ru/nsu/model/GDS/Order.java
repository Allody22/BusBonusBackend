package ru.nsu.model.GDS;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Order {
    private Integer id;
    private User user;
    private Company agent;
    private Double total;
    private String paymentMethod;
    private EOrderStatus status;
    private String created;
    private List<Ticket> tickets;
}
