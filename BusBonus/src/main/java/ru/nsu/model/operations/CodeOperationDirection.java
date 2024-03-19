package ru.nsu.model.operations;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Table(name = "code_operation_direction")
@NoArgsConstructor
@ToString
@Data
public class CodeOperationDirection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "direction")
    private String direction;
}
