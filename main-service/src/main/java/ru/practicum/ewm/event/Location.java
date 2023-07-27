package ru.practicum.ewm.event;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "location"/*, schema = "public"*/)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double lat;

    private Double lon;
}
