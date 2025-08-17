package routine.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import routine.log.domain.RoutineState;

import java.time.LocalDate;
import java.util.List;

public interface RoutineStateRepository extends JpaRepository<RoutineState,Long> {

    @Query("""
        select s
        from RoutineState s
        where s.routine.id in :ids
          and s.occurenceDate = :date
    """)
    List<RoutineState> findByRoutineIdsAndDate(
            @Param("ids") List<Long> ids,
            @Param("date") LocalDate date);
}
