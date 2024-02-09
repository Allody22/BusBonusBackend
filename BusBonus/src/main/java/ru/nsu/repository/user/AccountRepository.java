package ru.nsu.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.model.user.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> getAccountByBusBonusId(String busBonusId);


    @Query(value = "SELECT * FROM account WHERE phone = :phone", nativeQuery = true)
    Optional<Account> getAccountByPhone(@Param("phone") String phone);

    Optional<Account> getAccountById(Long id);

    Optional<Account> findByPhone(String phone);

    boolean existsAccountByPhone(String phone);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.password = :password WHERE a.id = :id")
    void updatePasswordById(Long id, String password);


    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.phone = COALESCE(:phone,a.phone)," +
            "a.email = COALESCE(:email, a.email) WHERE a.id = :id")
    void updateAccountPhoneAndEmailById(@Param("id") Long id,
                                        @Param("phone") String phone,
                                        @Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET " +
            "a.email = COALESCE(:email, a.email) WHERE a.id = :id")
    void updateAccountEmailById(@Param("id") Long id,
                                @Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.status = 'ACTIVE' " +
            "WHERE a.id = :id")
    void makeAccountActive(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.status = 'NOT_ACTIVE' " +
            "WHERE a.id = :id")
    void makeAccountInActive(@Param("id") Long id);
}
