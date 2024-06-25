package ru.mvlsoft.users.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getRefById(Long id) {
        return userRepository.getReferenceById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteAllUsers() {
        userRepository.deleteAllInBatch();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User updateUser(Long id, User newData) {
        User user = getRefById(id);
        user.update(newData);
        return user;
    }

    public boolean checkUserExists(Long id) {
        return userRepository.existsById(id);
    }

    public void deleteUser(Long id) {
        checkUserExists(id);
        userRepository.deleteById(id);
    }
}
