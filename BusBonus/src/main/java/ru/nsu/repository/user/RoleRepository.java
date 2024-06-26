package ru.nsu.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.model.Role;
import ru.nsu.model.constants.ERole;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);

    @Transactional
    @Query(value = "SELECT name FROM roles", nativeQuery = true)
    Optional<List<String>> getAllBy();

}