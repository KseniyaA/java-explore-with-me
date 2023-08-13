package ru.practicum.ewm.user;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.dto.UserDtoRequest;
import ru.practicum.ewm.user.dto.UserDtoResponse;
import ru.practicum.ewm.user.dto.UserShortDtoResponse;

@UtilityClass
public class UserMapper {
    public User toUser(UserDtoRequest dto) {
        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .build();
    }

    public UserDtoResponse toUserDtoResponse(User user) {
        return UserDtoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserShortDtoResponse toUserShortDtoResponse(User user) {
        return UserShortDtoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
