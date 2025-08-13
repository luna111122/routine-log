package routine.log.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routine")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
