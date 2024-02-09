package ru.nsu.repository.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.model.operations.MethodList;

import java.util.Optional;

@Repository
public interface MethodListRepository extends JpaRepository<MethodList, Long> {

    Optional<MethodList> findByMethodName(String name);
}
