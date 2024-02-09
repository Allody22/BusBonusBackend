package ru.nsu.model.operations;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.nsu.model.user.Account;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "operation_account")
@Data
public class OperationAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "method_name_ref")
    private MethodList methodNameRef;

    @Column(name = "description", length = 800)
    private String description;

    @ManyToOne
    @JoinColumn(name = "account_ref")
    private Account accountRef;
}
