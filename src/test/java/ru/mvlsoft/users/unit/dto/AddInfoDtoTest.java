package ru.mvlsoft.users.unit.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.dto.AddInfoDto;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind.HOBBY;

class AddInfoDtoTest {
    @Test
    void конструктор() {
        AddInfoDto dto = new AddInfoDto();
        assertNotNull(dto);
        dto = new AddInfoDto(HOBBY, "value");
        assertNotNull(dto);
    }


    @Test()
    void геттерыСеттеры() {
        AddInfoDto dto = new AddInfoDto();
        dto.setId(1L);
        dto.getId();
        dto.setUserId(1L);
        dto.getUserId();
        dto.setKind(HOBBY);
        dto.getKind();
        dto.setValue("1");
        dto.getValue();
        dto.setValidFromDate(new Date());
        dto.getValidFromDate();
        dto.setValidToDate(new Date());
        dto.getValidToDate();
    }

    @Test
    void equalsAndHashCode() {
        // Всё что выше - чтобы для entity избежать ошибки prefab values
        EqualsVerifier.forClass(AddInfoDto.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void testToString() {
        assertFalse(new AddInfoDto().toString().isEmpty());
    }
}
