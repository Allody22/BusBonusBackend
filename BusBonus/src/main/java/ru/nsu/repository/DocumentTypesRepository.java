package ru.nsu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.model.DocumentTypes;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentTypesRepository extends JpaRepository<DocumentTypes, Long> {

    Optional<DocumentTypes> findByType(String type);

    @Transactional
    @Query(value = "SELECT type FROM document_types", nativeQuery = true)
    List<String> getAll();
}
