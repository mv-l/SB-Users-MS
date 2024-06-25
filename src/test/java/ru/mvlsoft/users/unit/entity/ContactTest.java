package ru.mvlsoft.users.unit.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.Contact;
import ru.mvlsoft.users.entity.User;

import java.util.Set;

import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.*;
import static ru.mvlsoft.users.entity.enums.ContactKind.EMAIL;
import static ru.mvlsoft.users.entity.enums.ContactKind.PHONE;

class ContactTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final String MARKED_NON_NULL_BUT_IS_NULL = "is marked non-null but is null";
    private static final String MUST_NOT_BE_BLANK = "must not be blank";
    private Contact contact1;

    @Test
    void конструкторБезПараметров() {
        contact1 = new Contact();
        assertNotNull(contact1);
    }

    @Test()
    void конструкторСПараметрами() {
        contact1 = new Contact(PHONE, "aaaa");
        assertNotNull(contact1);
        contact1 = new Contact(new User(), PHONE, "aaaa");
        assertNotNull(contact1);
        contact1 = new Contact(1L, new User(), PHONE, "aaaa");
        assertNotNull(contact1);
    }

    @Test()
    void nullПараметрыВКонструкторе() {
        Exception ex = assertThrows(NullPointerException.class, () ->
                new Contact(null, "value")
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
        ex = assertThrows(NullPointerException.class, () ->
                new Contact(EMAIL, null)
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
        ex = assertThrows(NullPointerException.class, () ->
                new Contact(null, PHONE, "value")
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
        ex = assertThrows(NullPointerException.class, () ->
                new Contact(null, new User(), PHONE, "value")
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
    }

    @Test()
    void некорректныеПараметрыВКонструкторе() {
        contact1 = new Contact(new User(), PHONE, " ");
        Set<ConstraintViolation<Contact>> violations = validator.validate(contact1);
        assertTrue(violations.size() == 1 && violations.iterator().next().getMessage().contains(MUST_NOT_BE_BLANK));
    }

    @Test()
    void дефолтныеЗначенияАтрибутовПослеКонструктора() {
        contact1 = new Contact();
        assertNull(contact1.getId());
        assertNull(contact1.getUser());
        assertNull(contact1.getKind());
        assertNull(contact1.getValue());
        assertFalse(contact1.getPreferred());
    }

    @Test()
    void геттерыСеттеры() {
        contact1 = new Contact();
        contact1.setId(1L);
        contact1.getId();
        contact1.setUser(new User());
        contact1.getUser();
        contact1.setKind(PHONE);
        contact1.getKind();
        contact1.setValue("1");
        contact1.getValue();
        contact1.setVersion(1L);
        contact1.getVersion();
    }

    @Test
    void toStringTest() {
        assertFalse(new Contact(1L, new User(), PHONE, "value").toString().isEmpty());
        assertFalse(new Contact(1L, new User(1L, "a", "b", "c"), PHONE, "value").toString().isEmpty());
    }

    @Test
    void equalsAndHashCode() {
        User user1, user2;
        user1 = new User(1L, "user name", "first name", "last name");
        user2 = new User(2L, "username2", "first name", "last name");
        // Всё что выше - чтобы для entity избежать ошибки prefab values
        EqualsVerifier.forClass(Contact.class)
                .withPrefabValues(User.class, user1, user2)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    void update() {
        Contact contact1 = new Contact(1L, new User(), EMAIL, "aaa@aaa.com");
        contact1.setVersion(2L);

        User user2 = new User();
        Contact contact2 = new Contact(3L, user2, PHONE, "123");
        contact2.setPreferred(TRUE);
        contact2.setVersion(4L);

        contact1.update(contact2);

        // Должны измениться все поля кроме id, version
        assertTrue(
                contact1.getId() == 1L &&
                        contact1.getVersion().equals(2L) &&
                        contact1.getUser().equals(user2) &&
                        contact1.getKind().equals(contact2.getKind()) &&
                        contact1.getValue().equals(contact2.getValue()) &&
                        contact1.getPreferred().equals(contact2.getPreferred())
        );
    }

    @Test()
    void nullПараметрВupdate() {
        Exception ex = assertThrows(NullPointerException.class, () ->
                new Contact().update(null)
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
    }
}
