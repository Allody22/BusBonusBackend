package ru.nsu.model.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.nsu.model.Documents;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user_data")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private final List<Documents> documents = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ownerData")
    @JsonIgnore
    @ToString.Exclude
    private Account ownerAccount; // Владельцем какого аккаунта является

    @Column(name = "date")
    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date date;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    //Отчество
    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "birth_date")
    @JsonFormat(timezone = "Asia/Novosibirsk")
    private Date birthDate;

    @Column(name = "gender")
    private String gender;

    public void addDocumentVersion(Documents documents) {
        this.documents.add(documents);
    }
}