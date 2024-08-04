package ru.mvlsoft.users.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.AdditionalInfo;
import ru.mvlsoft.users.entity.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind.HOBBY;
import static ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind.SOFT_SKILLS;

class AdditionalInfoTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final String MARKED_NON_NULL_BUT_IS_NULL = "is marked non-null but is null";
    private static final String MUST_NOT_BE_BLANK = "must not be blank";

    private AdditionalInfo info;

    @Test
    void конструкторБезПараметров() {
        info = new AdditionalInfo();
        assertNotNull(info);
    }

    @Test()
    void конструкторСПараметрами() {
        info = new AdditionalInfo(HOBBY, "value");
        assertNotNull(info);
        info = new AdditionalInfo(new User(), SOFT_SKILLS, "value");
        assertNotNull(info);
        info = new AdditionalInfo(1L, new User(), SOFT_SKILLS, "value");
        assertNotNull(info);
    }

    @Test()
    void nullПараметрыВКонструкторе() {
        Exception ex = assertThrows(NullPointerException.class, () ->
                new AdditionalInfo(null, "value")
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));

        ex = assertThrows(NullPointerException.class, () ->
                new AdditionalInfo(HOBBY, null)
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));

        ex = assertThrows(NullPointerException.class, () ->
                new AdditionalInfo(null, HOBBY, "value")
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));

        ex = assertThrows(NullPointerException.class, () ->
                new AdditionalInfo(null, new User(), HOBBY, "value")
        );
        assertTrue(ex.getMessage().contains(MARKED_NON_NULL_BUT_IS_NULL));
    }

    @Test()
    void некорректныеПараметрыВКонструкторе() {
        info = new AdditionalInfo(new User(), HOBBY, " ");
        Set<ConstraintViolation<AdditionalInfo>> violations = validator.validate(info);
        assertTrue(violations.size() == 1 && violations.iterator().next().getMessage().contains(MUST_NOT_BE_BLANK));
    }

    @Test()
    void дефолтныеЗначенияАтрибутовПослеКонструктора() {
        info = new AdditionalInfo();
        assertNull(info.getId());
        assertNull(info.getUser());
        assertNull(info.getKind());
        assertNull(info.getValue());
        assertNull(info.getValidFromDate());
        assertNull(info.getValidToDate());
        assertNull(info.getVersion());
    }

    @Test()
    void геттерыСеттеры() {
        info = new AdditionalInfo();
        info.setId(1L);
        info.getId();
        info.setUser(new User());
        info.getUser();
        info.setKind(HOBBY);
        info.getKind();
        info.setValue("1");
        info.getValue();
        info.setValidFromDate(new Date());
        info.getValidFromDate();
        info.setValidToDate(new Date());
        info.getValidToDate();
        info.setVersion(1L);
        info.getVersion();
    }

    @Test
    void toStringTest() {
        assertFalse(new AdditionalInfo(1L, new User(), HOBBY, "value").toString().isEmpty());
    }

    @Test
    void equalsAndHashCode() {
        User user1, user2;
        user1 = new User(1L, "user name", "first name", "last name");
        user2 = new User(2L, "username2", "first name", "last name");

        // Всё что выше - чтобы для entity избежать ошибки prefab values
        EqualsVerifier.forClass(AdditionalInfo.class)
                .withPrefabValues(User.class, user1, user2)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    void update() {
        AdditionalInfo info1 = new AdditionalInfo(1L, new User(), HOBBY, "aaa");
        info1.setValidFromDate(java.sql.Date.valueOf(LocalDate.of(1999, 12, 30)));
        info1.setValidToDate(java.sql.Date.valueOf(LocalDate.of(1000, 1, 31)));
        info1.setVersion(2L);

        AdditionalInfo info2 = new AdditionalInfo(3L, new User(), SOFT_SKILLS, "bbb");
        info2.setValidFromDate(java.sql.Date.valueOf(LocalDate.of(2023, 2, 28)));
        info2.setValidToDate(java.sql.Date.valueOf(LocalDate.of(2024, 3, 1)));
        info2.setVersion(4L);

        info1.update(info2);

        // Должны измениться все поля кроме id, version
        assertTrue(
                info1.getId() == 1L &&
                        info1.getVersion() == 2L &&
                        info1.getUser().equals(info2.getUser()) &&
                        info1.getKind().equals(info2.getKind()) &&
                        info1.getValue().equals(info2.getValue()) &&
                        info1.getValidFromDate().equals(info2.getValidFromDate()) &&
                        info1.getValidToDate().equals(info2.getValidToDate())
        );
    }
}
