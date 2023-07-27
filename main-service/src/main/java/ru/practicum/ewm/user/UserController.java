package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserDtoRequest;
import ru.practicum.ewm.user.dto.UserDtoResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDtoResponse add(@RequestBody @Valid UserDtoRequest dto) {
        log.info("Получен запрос POST /admin/users с параметрами dto = {}", dto);
        User user = userService.add(UserMapper.toUser(dto));
        log.info("Создан user с id = {}", user.getId());
        return UserMapper.toUserDtoResponse(user);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<UserDtoResponse> getUsers(@RequestParam(required = false) List<Long> ids,
                               @RequestParam(defaultValue = "0") Integer from,
                               @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /admin/users с параметрами ids = {}, from = {}, size = {}", ids, from, size);
        List<User> users = userService.getUsers(ids, from, size);
        return users.stream().map(UserMapper::toUserDtoResponse).collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("userId") long userId) {
        log.info("Получен запрос DELETE /admin/users с параметрами id = {}", userId);
        userService.deleteById(userId);
    }
}
