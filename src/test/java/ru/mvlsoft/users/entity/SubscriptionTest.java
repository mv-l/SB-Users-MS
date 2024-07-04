package ru.mvlsoft.users.entity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.Subscription;
import ru.mvlsoft.users.entity.User;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final String MARKED_NON_NULL_BUT_IS_NULL = "is marked non-null but is null";
    private static final String MUST_NOT_BE_BLANK = "must not be blank";

    private Subscription sub;

    @Test
    void конструкторБезПараметров() {
        sub = new Subscription();
        assertNotNull(sub);
    }

    @Test()
    void конструкторСПараметрами() {
        sub = new Subscription(new User(), new User());
        assertNotNull(sub);
        sub = new Subscription(1L, new User(), new User());
        assertNotNull(sub);
    }

    @Test()
    void nullПараметрыВКонструкторе() {
        Exception ex = assertThrows(NullPointerException.class, () ->
                new Subscription(null, new User())
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
        ex = assertThrows(NullPointerException.class, () ->
                new Subscription(new User(), null)
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
        ex = assertThrows(NullPointerException.class, () ->
                new Subscription(null, new User(), new User())
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
    }

    @Test()
    void дефолтныеЗначенияАтрибутовПослеКонструктора() {
        sub = new Subscription();
        assertNull(sub.getId());
        assertNull(sub.getPublisher());
        assertNull(sub.getSubscriber());
        assertNull(sub.getValidFromDate());
        assertNull(sub.getValidToDate());
        assertNull(sub.getVersion());
    }

    @Test()
    void геттерыСеттеры() {
        sub = new Subscription();
        sub.setId(1L);
        sub.getId();
        sub.setPublisher(new User());
        sub.getPublisher();
        sub.setSubscriber(new User());
        sub.getSubscriber();
        sub.setValidFromDate(new Date());
        sub.getValidFromDate();
        sub.setValidToDate(new Date());
        sub.getValidToDate();
        sub.setVersion(1L);
        sub.getVersion();
    }

    @Test
    void toStringTest() {
        assertFalse((new Subscription()).toString().isEmpty());
        assertFalse((new Subscription(1L, new User(), new User())).toString().isEmpty());
    }

    @Test
    void equalsAndHashCode() {
        User user1, user2;
        user1 = new User(1L, "user name", "first name", "last name");
        user2 = new User(2L, "username2", "first name", "last name");

        // Всё что выше - чтобы для entity избежать ошибки prefab values
        EqualsVerifier.forClass(Subscription.class)
                .withPrefabValues(User.class, user1, user2)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    void update() {
        Subscription sub1 = new Subscription(1L, new User(), new User());
        sub1.setValidFromDate(java.sql.Date.valueOf(LocalDate.of(1999, 12, 30)));
        sub1.setValidToDate(java.sql.Date.valueOf(LocalDate.of(1000, 1, 31)));
        sub1.setVersion(2L);

        Subscription sub2 = new Subscription(3L, new User(), new User());
        sub2.setValidFromDate(java.sql.Date.valueOf(LocalDate.of(2023, 2, 28)));
        sub2.setValidToDate(java.sql.Date.valueOf(LocalDate.of(2024, 3, 1)));
        sub2.setVersion(4L);

        sub1.update(sub2);

        // Должны измениться все поля кроме id, version
        assertTrue(
                sub1.getId() == 1L &&
                        sub1.getVersion() == 2L &&
                        sub1.getPublisher().equals(sub2.getPublisher()) &&
                        sub1.getSubscriber().equals(sub2.getSubscriber()) &&
                        sub1.getValidFromDate().equals(sub2.getValidFromDate()) &&
                        sub1.getValidToDate().equals(sub2.getValidToDate())
        );
    }

    @Test()
    void nullПараметрВupdate() {
        Exception ex = assertThrows(NullPointerException.class, () ->
                new Subscription().update(null)
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
    }
}
