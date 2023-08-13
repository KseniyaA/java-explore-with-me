package ru.practicum.ewm.event;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {
    private Double lat;

    private Double lon;
}
