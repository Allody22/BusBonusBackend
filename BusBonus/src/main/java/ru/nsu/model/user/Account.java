package ru.nsu.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.nsu.model.Achievements;
import ru.nsu.model.Role;
import ru.nsu.model.TripBBId;
import ru.nsu.model.constants.EUserTypeStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "account")
@ToString
@Data
@NoArgsConstructor
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

    public String generateBusBonusId() {
        String DIGITS = "0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom RANDOM = new SecureRandom();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sb.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
            }
            if (i < 4 - 1) {
                sb.append('-');
            }
        }
        return sb.toString();
    }

    public void addUserData(UserData userData) {
        this.userData.add(userData);
    }
}
