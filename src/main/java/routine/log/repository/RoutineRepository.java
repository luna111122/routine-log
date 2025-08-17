package routine.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import routine.log.domain.Routine;

import java.time.LocalDate;
import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    List<Routine> findAllByUserId(Long userId);


    @Query("""
      select distinct r
      from Routine r
      join r.weekdays w
      left join fetch r.place
      left join fetch r.time t
      where r.user.id = :userId
        and w.weekday = :day
        and r.startDate <= :target
            and (r.endDate is null or r.endDate > :target)
          order by t.startTime
    """)
    List<Routine> findDaily(@Param("userId") Long userId,
                            @Param("day") java.time.DayOfWeek day,
                            @Param("target")LocalDate target);



}
