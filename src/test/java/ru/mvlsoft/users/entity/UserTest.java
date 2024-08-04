package ru.mvlsoft.users.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.AdditionalInfo;
import ru.mvlsoft.users.entity.Contact;
import ru.mvlsoft.users.entity.Subscription;
import ru.mvlsoft.users.entity.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.mvlsoft.users.entity.enums.ContactKind.EMAIL;
import static ru.mvlsoft.users.entity.enums.ContactKind.PHONE;
import static ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind.HOBBY;
import static ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind.SOFT_SKILLS;
import static ru.mvlsoft.users.entity.enums.UserSex.FEMALE;
import static ru.mvlsoft.users.entity.enums.UserSex.MALE;

class UserTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final String MARKED_NON_NULL_BUT_IS_NULL = "is marked non-null but is null";
    private static final String MUST_NOT_BE_BLANK = "must not be blank";

    private User user;

    @Test
    void конструкторБезПараметров() {
        user = new User();
        assertNotNull(user);
    }

    @Test()
    void конструкторСПараметрами() {
        user = new User("user name", "first name", "last name");
        assertNotNull(user);
        user = new User(1L, "user name", "first name", "last name");
        assertNotNull(user);
    }

    @Test()
    void nullПараметрыВКонструкторе() {
        Exception ex = assertThrows(NullPointerException.class, () ->
                new User(null, "first name", "last name")
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
        ex = assertThrows(NullPointerException.class, () ->
                new User("user name", null, "last name")
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
        ex = assertThrows(NullPointerException.class, () ->
                new User("user name", "first name", null)
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
        ex = assertThrows(NullPointerException.class, () ->
                new User(null, "user name", "first name", "last name")
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
    }

    @Test()
    void некорректныеПараметрыВКонструкторе() {
        user = new User(" ", "first name", "last name");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.size() == 1 && violations.iterator().next().getMessage().contains(MUST_NOT_BE_BLANK));

        user = new User("username", "", "last name");
        violations = validator.validate(user);
        assertTrue(violations.size() == 1 && violations.iterator().next().getMessage().contains(MUST_NOT_BE_BLANK));

        user = new User("username", "first name", "   ");
        violations = validator.validate(user);
        assertTrue(violations.size() == 1 && violations.iterator().next().getMessage().contains(MUST_NOT_BE_BLANK));
    }

    @Test()
    void дефолтныеЗначенияАтрибутовПослеКонструктора() {
        user = new User("user name", "first name", "last name");
        assertNotNull(user.getRegistrationDate());
        assertNull(user.getId());
        assertNull(user.getMiddleName());
        assertNull(user.getTitle());
        assertNull(user.getBirthDate());
        assertNull(user.getSex());
        assertNull(user.getAvatarId());
        assertNull(user.getStatus());
        assertNull(user.getVersion());
        assertTrue(user.getContacts() != null && user.getContacts().isEmpty());
        assertTrue(user.getAddInfos() != null && user.getAddInfos().isEmpty());
        assertTrue(user.getSubscribers() != null && user.getSubscribers().isEmpty());
        assertTrue(user.getPublishers() != null && user.getPublishers().isEmpty());
    }

    @Test()
    void геттерыСеттеры() {
        user = new User();
        user.setId(1L);
        user.getId();
        user.setUserName("1");
        user.getUserName();
        user.setFirstName("1");
        user.getFirstName();
        user.setMiddleName("1");
        user.getMiddleName();
        user.setLastName("1");
        user.getLastName();
        user.setTitle("1");
        user.getTitle();
        user.setBirthDate(new Date());
        user.getBirthDate();
        user.setSex(MALE);
        user.getSex();
        user.setAvatarId(1L);
        user.getAvatarId();
        user.setStatus("1");
        user.getStatus();
        user.setRegistrationDate(new Date());
        user.getRegistrationDate();
        user.setVersion(1L);
        user.getVersion();
        user.getContacts();
        user.getAddInfos();
        user.getSubscribers();
        user.getPublishers();
    }

    @Test
    void toStringTest() {
        assertFalse(new User().toString().isEmpty());
    }

    @Test
    void equalsAndHashCode() {
        User user1, user2, user3;
        user1 = new User(1L, "user name", "first name", "last name");
        user2 = new User(2L, "username2", "first name", "last name");
        user3 = new User(3L, "username3", "first name", "last name");

        Contact contact1, contact2;
        contact1 = new Contact(4L, user1, EMAIL, "aaa@aaa.com");
        contact2 = new Contact(5L, user1, PHONE, "+74957771010");

        AdditionalInfo info1, info2;
        info1 = new AdditionalInfo(6L, user1, HOBBY, "aaa");
        info2 = new AdditionalInfo(7L, user1, SOFT_SKILLS, "bbb");

        Subscription sub1, sub2;
        sub1 = new Subscription(8L, user1, user2);
        sub2 = new Subscription(9L, user1, user3);

        // Всё что выше - чтобы для entity избежать ошибки prefab values
        EqualsVerifier.forClass(User.class)
                .withPrefabValues(Contact.class, contact1, contact2)
                .withPrefabValues(AdditionalInfo.class, info1, info2)
                .withPrefabValues(Subscription.class, sub1, sub2)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    void addContact() {
        User user = new User(1L, "", "", "");
        Contact contact = new Contact();
        user.addContact(contact);
        assertEquals(1, user.getContacts().size());
        assertTrue(user.getContacts().contains(contact));
        assertEquals(user, contact.getUser());
    }

    @Test
    void addAddInfo() {
        User user = new User(1L, "", "", "");
        AdditionalInfo info = new AdditionalInfo();
        user.addAddInfo(info);
        assertEquals(1, user.getAddInfos().size());
        assertTrue(user.getAddInfos().contains(info));
        assertEquals(user, info.getUser());
    }

    @Test
    void addSubscription() {
        User user = new User(1L, "", "", "");
        Subscription sub = new Subscription();
        user.addSubscription(sub);
        assertEquals(1, user.getSubscribers().size());
        assertTrue(user.getSubscribers().contains(sub));
    }

    @Test
    void update() {
        User user1 = new User(1L, "username1", "firstname1", "lastname1");
        user1.setMiddleName("middlename1");
        user1.setTitle("title1");
        user1.setBirthDate(java.sql.Date.valueOf(LocalDate.of(1980, 1, 1)));
        user1.setSex(MALE);
        user1.setAvatarId(123L);
        user1.setStatus("AFK");
        user1.setRegistrationDate(java.sql.Date.valueOf(LocalDate.of(2024, 1, 31)));
        user1.setVersion(2L);

        User user2 = new User(3L, "username2", "firstname2", "lastname2");
        user2.setMiddleName("middlename2");
        user2.setTitle("title2");
        user2.setBirthDate(java.sql.Date.valueOf(LocalDate.of(1999, 12, 30)));
        user2.setSex(FEMALE);
        user2.setAvatarId(456L);
        user2.setStatus("unset");
        user2.setRegistrationDate(java.sql.Date.valueOf(LocalDate.of(2024, 2, 28)));
        user2.setVersion(4L);

        user1.update(user2);

        // Должны измениться все поля кроме id, version
        assertTrue(
                user1.getId() == 1L &&
                        user1.getVersion() == 2L &&
                        user1.getUserName().equals(user2.getUserName()) &&
                        user1.getFirstName().equals(user2.getFirstName()) &&
                        user1.getMiddleName().equals(user2.getMiddleName()) &&
                        user1.getLastName().equals(user2.getLastName()) &&
                        user1.getTitle().equals(user2.getTitle()) &&
                        user1.getBirthDate().equals(user2.getBirthDate()) &&
                        user1.getSex().equals(user2.getSex()) &&
                        user1.getAvatarId().equals(user2.getAvatarId()) &&
                        user1.getStatus().equals(user2.getStatus()) &&
                        user1.getRegistrationDate().equals(user2.getRegistrationDate())
        );
    }
}
