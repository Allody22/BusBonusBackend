package ru.nsu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.model.constants.TicketCategories;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketCategoriesRepository extends JpaRepository<TicketCategories, Long> {

    @Transactional
    @Query(value = "SELECT category FROM ticket_categories", nativeQuery = true)
    List<String> getAll();

    Optional<TicketCategories> findTicketCategoriesByCategory(String category);
}
