package routine.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import routine.log.domain.Place;

public interface PlaceRepository extends JpaRepository<Place,Long> {
}
