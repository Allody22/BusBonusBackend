package ru.nsu.repository.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.model.operations.OperationCodeNames;

import java.util.Optional;

@Repository
public interface CodeOperationNamesRepository extends JpaRepository<OperationCodeNames, Long> {

    Optional<OperationCodeNames> findByName(String name);
}