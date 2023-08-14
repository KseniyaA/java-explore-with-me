package ru.practicum.ewm.location;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "location"/*, schema = "public"*/)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocationParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double lat;

    private Double lon;

    private Double radius;

    private String name;
}
