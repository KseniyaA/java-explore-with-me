package ru.practicum.ewm.category;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "category"/*, schema = "public"*/)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
