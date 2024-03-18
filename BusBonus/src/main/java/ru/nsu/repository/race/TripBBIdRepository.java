package ru.nsu.repository.race;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.etraffic.busbonus.model.races.TripBBId;

@Repository
public interface TripBBIdRepository extends JpaRepository<TripBBId, Long> {
}
