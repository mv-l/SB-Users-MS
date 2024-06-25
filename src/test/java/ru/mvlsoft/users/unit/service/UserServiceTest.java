package ru.mvlsoft.users.unit.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.repository.UserRepository;
import ru.mvlsoft.users.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository repository = Mockito.mock();
    private final UserService service = new UserService(repository);
    List<User> users = new ArrayList<>();
    private Long sequence = 0L;

    Long getSequenceNextVal() {
        return ++sequence;
    }

    @Test
    void getAllUsers() {
        users.clear();
        users.add(new User());
        users.add(new User());
        users.add(new User());

        when(repository.findAll()).thenReturn(users);
        Iterable<User> users = service.getAllUsers();

        assertEquals(3, ((Collection<?>) users).size());
    }

    @Test
    void deleteAllUsers() {
        doNothing().when(repository).deleteAllInBatch();
        service.deleteAllUsers();
    }

    @Test
    void createUser() {
        User newUser = new User();

        User mockUser = new User();
        mockUser.update(newUser); // копируем в mockUser все поля из user
        mockUser.setId(getSequenceNextVal()); // имитируем присвоение id
        mockUser.setVersion(getSequenceNextVal()); // имитируем присвоение версии

        when(repository.save(newUser)).thenReturn(mockUser);

        User createdUser = service.createUser(newUser);
        assertNotNull(createdUser);
    }

    @Test
    void findUserById() {
        User user1 = new User(1L, "user1", "a", "b");
        User user2 = new User(2L, "user2", "a", "b");

        when(repository.findById(user2.getId())).thenReturn(Optional.of(user2));

        User foundUser = service.findUserById(user2.getId());
        assertEquals(foundUser, user2);

    }

    @Test
    void updateUser() {}

    @Test
    void checkUserExistsById() {}

    @Test
    void deleteUser() {}
}
