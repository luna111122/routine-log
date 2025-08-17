package routine.log.domain;

import jakarta.persistence.*;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;


@Entity
@Table(name = "places")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter


public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "POINT", nullable = false)
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Point center;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_places_routine"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Routine routine;
}
