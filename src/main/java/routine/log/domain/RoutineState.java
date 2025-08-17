package routine.log.domain;

import jakarta.persistence.*;
import lombok.*;
import routine.log.State;

import java.time.LocalDate;

@Entity
@Table(name = "routine_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoutineState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_routineState_routine"))
    private Routine routine;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private State status = State.MISSED;

    @Column(name = "occurence_date", nullable = false)
    private LocalDate occurenceDate;

}
