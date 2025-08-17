package routine.log.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "routines")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false, name = "location_label")
    String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_id"))
    private User user;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = true)
    private LocalDate endDate;



    @OneToOne(mappedBy = "routine",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY)
    private Place place;


    @OneToOne(mappedBy = "routine",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Time time;

    @OneToMany(mappedBy = "routine",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<RoutineState> states = new HashSet<>();


    @OneToMany(mappedBy = "routine",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Weekday> weekdays = new HashSet<>();


    public void clearWeekdays(){
        this.weekdays.clear();
    }
    public void addWeekday(DayOfWeek dayOfWeek) {
        Weekday weekday = Weekday.builder()
                .routine(this)
                .weekday(dayOfWeek)
                .build();

        this.weekdays.add(weekday);
    }
    public void setWeekdays(Collection<DayOfWeek> days){
        this.clearWeekdays();
        for(DayOfWeek d: new HashSet<>(days)){
            this.addWeekday(d);
        }

    }


    public void setPlace(Place p) {
        this.place = p;
        if (p != null) p.setRoutine(this);
    }
    public void setTime(Time t) {
        this.time = t;
        if (t != null) t.setRoutine(this);
    }
    public void addWeekday(Weekday w) {
        this.weekdays.add(w);
        w.setRoutine(this);
    }

}
