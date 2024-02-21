package ru.nsu.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.nsu.model.Achievements;
import ru.nsu.model.Role;
import ru.nsu.model.TripBBId;
import ru.nsu.model.constants.EUserTypeStatus;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;


@Entity
@Table(name = "account")
@ToString
@Data
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_data_id", referencedColumnName = "id")
    @NotNull
    @JsonIgnore
    @ToString.Exclude
    private UserData ownerData;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "account_companions",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "user_data_id"))
    @ToString.Exclude
    @JsonIgnore
    private final List<UserData> userData = new ArrayList<>();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EUserTypeStatus status; // Active or not active

    @Column(name = "phone", unique = true)
    private String phone;

    @Email
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank
    @ToString.Exclude
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "bus_bonus_id", unique = true)
    private String busBonusId;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private final List<TripBBId> trips = new ArrayList<>();

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private final List<Achievements> achievements = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ToString.Exclude
    private final Set<Role> roles = new HashSet<>();

    public Account() {
        generateBusBonusId();
    }

    public void generateBusBonusId() {
        this.busBonusId = UUID.randomUUID().toString();
    }

    public void addUserData(UserData userData) {
        this.userData.add(userData);
    }
}
