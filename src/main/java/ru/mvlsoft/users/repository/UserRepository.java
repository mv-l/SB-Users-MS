package ru.mvlsoft.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mvlsoft.users.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}
