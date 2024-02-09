package ru.nsu.model.operations;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "method_list")
public class MethodList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date date;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "method_ver")
    private int methodVer;
}
