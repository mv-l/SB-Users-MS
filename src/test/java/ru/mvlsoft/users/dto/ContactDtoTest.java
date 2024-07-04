package ru.mvlsoft.users.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.dto.ContactDto;

import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static ru.mvlsoft.users.entity.enums.ContactKind.EMAIL;

class ContactDtoTest {

    @Test()
    void геттерыСеттеры() {
        ContactDto dto = new ContactDto();
        dto.setId(1L);
        dto.getId();
        dto.setKind(EMAIL);
        dto.getKind();
        dto.setValue("1");
        dto.getValue();
        dto.setPreferred(TRUE);
        dto.getPreferred();
    }

    @Test
    void equalsAndHashCode() {
        // Всё что выше - чтобы для entity избежать ошибки prefab values
        EqualsVerifier.forClass(ContactDto.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void testToString() {
        assertFalse(new ContactDto().toString().isEmpty());
    }
}
