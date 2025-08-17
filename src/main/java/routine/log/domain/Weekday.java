package routine.log.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.Objects;

@Entity
@Table(name = "routine_weekdays",
        uniqueConstraints = @UniqueConstraint(columnNames = {"routine_id", "weekday"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Weekday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rs_routine_id"))
    private Routine routine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek weekday;


}
