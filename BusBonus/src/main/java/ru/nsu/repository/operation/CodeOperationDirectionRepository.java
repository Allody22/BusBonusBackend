package ru.nsu.repository.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.model.operations.CodeOperationDirection;

import java.util.Optional;

@Repository
public interface CodeOperationDirectionRepository extends JpaRepository<CodeOperationDirection, Long> {

    Optional<CodeOperationDirection> findByDirection(String direction);
}