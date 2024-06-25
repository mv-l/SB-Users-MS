package ru.mvlsoft.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mvlsoft.users.entity.Contact;

import java.util.Optional;

public interface UserContactRepository extends JpaRepository<Contact, Long> {
    Iterable<Contact> findAllByUserId(Long id);

    Optional<Contact> findByIdAndUserId(Long id, Long userId);

    Contact getRefrenceByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);
}
