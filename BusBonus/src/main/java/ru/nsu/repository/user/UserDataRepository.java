package ru.nsu.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.model.user.UserData;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE UserData ud SET  ud.name = COALESCE(:name, ud.name)," +
            "ud.lastName = COALESCE(:lastName, ud.lastName), ud.patronymic = COALESCE(:patronymic, ud.patronymic), " +
            "ud.birthDate = COALESCE(:birthDate, ud.birthDate), ud.gender = COALESCE(:gender, ud.gender) " +
            "WHERE ud.id = :dataId")
    void updateUserData(
            @Param("name") String name,
            @Param("lastName") String lastName,
            @Param("patronymic") String patronymic,
            @Param("birthDate") Date birthDate,
            @Param("gender") String gender,
            @Param("dataId") Long dataId);

    List<UserData> findAllByOwnerAccountIsNull();

    Optional<UserData> findById(Long id);
}
