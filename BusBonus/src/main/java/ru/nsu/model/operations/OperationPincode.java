package ru.nsu.model.operations;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "operation_pincode")
public class OperationPincode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date date;

    @Column(name = "login_user")
    private String loginUser;

    @ManyToOne
    @JoinColumn(name = "operation_direction")
    private CodeOperationDirection operationDirection;

    @ManyToOne
    @JoinColumn(name = "operation_name")
    private OperationCodeNames operationName;

    @Column(name = "pin_code_data")
    private String pinCodeData;
}