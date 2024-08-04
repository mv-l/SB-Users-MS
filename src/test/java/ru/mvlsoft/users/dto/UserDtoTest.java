package ru.mvlsoft.users.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.dto.UserDto;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.mvlsoft.users.entity.enums.UserSex.MALE;

class UserDtoTest {
    private UserDto dto;

    @Test
    void конструктор() {
        dto = new UserDto();
        assertNotNull(dto);
        dto = new UserDto("a", "b", "c");
        assertNotNull(dto);
    }

    @Test()
    void геттерыСеттеры() {
        dto = new UserDto();
        dto.setId(1L);
        dto.getId();
        dto.setUserName("1");
        dto.getUserName();
        dto.setFirstName("1");
        dto.getFirstName();
        dto.setMiddleName("1");
        dto.getMiddleName();
        dto.setLastName("1");
        dto.getLastName();
        dto.setTitle("1");
        dto.getTitle();
        dto.setBirthDate(new Date());
        dto.getBirthDate();
        dto.setSex(MALE);
        dto.getSex();
        dto.setAvatarId(1L);
        dto.getAvatarId();
        dto.setStatus("1");
        dto.getStatus();
        dto.setRegistrationDate(new Date());
        dto.getRegistrationDate();
    }

    @Test
    void equalsAndHashCode() {
        // Всё что выше - чтобы для entity избежать ошибки prefab values
        EqualsVerifier.forClass(UserDto.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void testToString() {
        assertFalse(new UserDto().toString().isEmpty());
    }
}
