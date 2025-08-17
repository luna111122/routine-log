package routine.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import routine.log.domain.Time;

public interface TimeRepository extends JpaRepository<Time, Long> {
}
