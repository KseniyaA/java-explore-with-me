package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.EntityNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public User add(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public List<User> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("id").ascending());
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(page).getContent();
        } else {
            return userRepository.findByIdIn(ids, page).getContent();
        }
    }

    @Transactional
    @Override
    public void deleteById(long userId) {
        getById(userId);
        userRepository.deleteById(userId);
    }

    private User getById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new EntityNotFoundException("Пользователь с id = " + userId + " не существует");
        });
    }
}
