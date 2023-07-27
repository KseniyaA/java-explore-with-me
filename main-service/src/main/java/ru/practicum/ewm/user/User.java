package ru.practicum.ewm.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users"/*, schema = "public"*/)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;
}
