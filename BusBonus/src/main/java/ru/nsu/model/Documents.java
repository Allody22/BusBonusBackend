package ru.nsu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.nsu.model.constants.DocumentTypes;
import ru.nsu.model.user.UserData;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "documents")
@NoArgsConstructor
@ToString
@Data
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private UserData user;

    @Column(name = "citizenship")
    private String citizenship; //гражданство

    @OneToOne
    @JoinColumn(name = "type")
    @JsonIgnore
    @ToString.Exclude
    private DocumentTypes documentType; // Тип документа

    //Номер документа
    @Column(name = "document_number")
    private String documentNumber;

    //Серия документа
    @Column(name = "document_series")
    private String documentSeries;

    //Орган выдавший документ
    @Column(name = "issuing_authority")
    private String issuingAuthority;

    //Дата получения документа
    @Column(name = "issue_date")
    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date issueDate;

    //Дата окончания действия документа
    @Column(name = "expiration_date")
    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date expirationDate;

    @Column(name = "additional_data")
    private String additionalData;

    @Column(name = "confirmation_of_benefits")
    private String confirmationOfBenefits;

    @Column(name = "version")
    private int version;

    @OneToOne
    @JoinColumn(name = "ticket_category")
    @JsonIgnore
    @ToString.Exclude
    private TicketCategories ticketCategory; // Категория документа
}
