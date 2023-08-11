package ru.practicum.ewm.compilation;

import lombok.*;
import ru.practicum.ewm.event.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "compilation"/*, schema = "public"*/)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinTable(name = "compilation_event",
            joinColumns = @JoinColumn(name = "compilation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id")
    )
    private Set<Event> events;

    private Boolean pinned;

    private String title;
}
