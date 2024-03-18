package ru.nsu.repository.race;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.model.races.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
