package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.ModelNotFoundException;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper mapper;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        int page = pageNumber(from, size);
        List<User> users;
        if (ids != null) {
            users = userRepository.findAllById(ids);
        } else {
            users = userRepository.findAll(PageRequest.of(page, size)).getContent();
        }
        return mapper.toUserDtoList(users);
    }


    @Transactional
    @Override
    public UserDto addUser(NewUserRequest userRequest) {
        User user = mapper.toUser(userRequest);
        user = userRepository.save(user);
        return mapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        checkExistence(List.of(userId));
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(long userId) {
        return mapper.toUserDto(fromOptionalToUSer(userId));
    }

    private User fromOptionalToUSer(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ModelNotFoundException(String.format("User %s not found", userId), "Model not found"));
    }

    private void checkExistence(List<Long> ids) {
        if (ids != null) {
            for (Long id : ids) {
                if (!userRepository.existsById(id)) {
                    throw new ModelNotFoundException(String.format("User %s not found", id), "Model not found");
                }
            }
        }
    }

    private int pageNumber(int from, int size) {
        return from / size;
    }
}
