package ru.nsu.repository.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.model.operations.OperationAccount;

@Repository
public interface OperationAccountRepository extends JpaRepository<OperationAccount, Long> {
}
