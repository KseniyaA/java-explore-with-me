package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.user.valid.ValidateUserName;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidateUserName
public class UserDtoRequest {
    @NotBlank(message = "Поле email не должно быть пустым")
    @Email(message = "Не верный формат электронной почты")
    @Size(min = 6, max = 254)
    private String email;

    @NotBlank(message = "Поле name не должно быть пустым")
    @Size(min = 2, max = 250)
    private String name;
}
