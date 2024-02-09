package ru.nsu.model;


import lombok.*;
import ru.nsu.model.user.Account;

import javax.persistence.*;

@Entity
@Table(name = "achievements")
@NoArgsConstructor
@ToString
@Getter
@Setter
@AllArgsConstructor
public class Achievements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}
