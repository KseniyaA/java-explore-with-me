package ru.practicum.ewm.user;

import java.util.List;

public interface UserService {
    User add(User user);

    List<User> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteById(long userId);
}
