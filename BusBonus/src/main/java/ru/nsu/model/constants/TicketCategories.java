package ru.nsu.model.constants;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.GenerationType;


@Entity
@Table(name = "ticket_categories")
@NoArgsConstructor
@ToString
@Data
public class TicketCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    private String category;
}