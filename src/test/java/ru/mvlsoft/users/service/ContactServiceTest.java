package ru.mvlsoft.users.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.mvlsoft.users.entity.Contact;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.repository.UserContactRepository;
import ru.mvlsoft.users.service.ContactService;
import ru.mvlsoft.users.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static ru.mvlsoft.users.entity.enums.ContactKind.EMAIL;
import static ru.mvlsoft.users.entity.enums.ContactKind.PHONE;

class ContactServiceTest {
    private final UserService userService = Mockito.mock();
    private final UserContactRepository contactRepository = Mockito.mock();
    private final ContactService contactService = new ContactService(userService, contactRepository);

    @Test
    void getAll() {
        User user1 = new User(1L, "user1", "a", "b");
        User user2 = new User(2L, "user1", "a", "b");
        List<Contact> contacts = List.of(
                new Contact(3L, user1, EMAIL, "aaa"),
                new Contact(4L, user1, PHONE, "123"),
                new Contact(5L, user1, PHONE, "456"),
                new Contact(6L, user2, PHONE, "999")
        );
        when(contactRepository.findAllByUserId(user2.getId())).thenReturn(List.of(contacts.getLast()));
        Iterable<Contact> foundContacts = contactService.getAll(user2.getId());
        assertEquals(1, ((Collection<?>) foundContacts).size());
        assertEquals(contacts.getLast(), foundContacts.iterator().next());
    }

    @Test
    void create() {
        User user = new User(1L, "user", "a", "b");
        Contact newContact = new Contact(EMAIL, "123");

        when(userService.getRefById(user.getId())).thenReturn(user);

        Contact storedContact = contactService.create(user.getId(), newContact);
        assertEquals(user, storedContact.getUser());
    }

    @Test
    void getByIdAndUserId() {
        Contact contact = new Contact(EMAIL, "123");
        when(contactRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(contact));
        Optional<Contact> foundContact = contactService.getByIdAndUserId(2L, 1L);
        assertTrue(foundContact.isPresent());
        assertEquals(contact, foundContact.get());
    }

    @Test
    void update() {
        Long userId = 1L;
        Long contactId = 2L;
        User user = new User(userId, "user", "a", "b");
        Contact oldContact = new Contact(contactId, user, EMAIL, "aaa");
        Contact newContact = new Contact(PHONE, "123");
        newContact.setPreferred(TRUE);
        when(contactRepository.getRefrenceByIdAndUserId(contactId, userId)).thenReturn(oldContact);

        Contact updatedContact = contactService.update(userId, contactId, newContact);
        assertTrue(oldContact.getId().equals(updatedContact.getId()) &&
                oldContact.getUser().getId().equals(updatedContact.getUser().getId()) &&
                newContact.getKind().equals(updatedContact.getKind()) &&
                newContact.getValue().equals(updatedContact.getValue()) &&
                newContact.getPreferred().equals(updatedContact.getPreferred())
        );
    }

    @Test
    void checkExists() {
        Long userId = 1L;
        Long contactId = 2L;
        when(contactRepository.existsByIdAndUserId(contactId, userId)).thenReturn(true);
        assertTrue(contactService.checkExists(userId, contactId));
    }

    @Test
    void deleteByIdAndUserId() {
        Long userId = 1L;
        Long contactId = 2L;
        doNothing().when(contactRepository).deleteByIdAndUserId(contactId, userId);
        contactService.deleteByIdAndUserId(contactId, userId);
    }
}
