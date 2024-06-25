package ru.mvlsoft.users.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mvlsoft.users.entity.Contact;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.repository.UserContactRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final UserService userService;
    private final UserContactRepository contactRepository;

    public Iterable<Contact> getAll(@NotNull Long userId) {
        return contactRepository.findAllByUserId(userId);
    }

    @Transactional
    public Contact create(@NotNull Long userId,
                          @NotNull Contact contact) {
        User user = userService.getRefById(userId);
        user.addContact(contact);
        return contact;
    }

    public Optional<Contact> getByIdAndUserId(@NotNull Long contactId,
                                              @NotNull Long userId) {
        return contactRepository.findByIdAndUserId(contactId, userId);
    }

    @Transactional
    public Contact update(Long userId, Long contactId, Contact newData) {
        Contact contact = contactRepository.getRefrenceByIdAndUserId(contactId, userId);
        // ToDo Решить проблему N+1
        contact.update(newData);
        return contact;
    }

    public boolean checkExists(Long userId, Long contactId) {
        return contactRepository.existsByIdAndUserId(contactId, userId);
    }

    @Transactional
    public void deleteByIdAndUserId(@NotNull Long id, @NotNull Long userId) {
        contactRepository.deleteByIdAndUserId(id, userId);
    }
}
