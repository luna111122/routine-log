package routine.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import routine.log.domain.Routine;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    List<Routine> findAllByUserId(Long userId);





}
