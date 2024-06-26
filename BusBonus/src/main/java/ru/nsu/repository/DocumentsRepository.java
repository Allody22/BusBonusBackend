package ru.nsu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.model.Documents;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Long> {
}
