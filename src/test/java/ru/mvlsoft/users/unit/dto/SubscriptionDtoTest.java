package ru.mvlsoft.users.unit.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.dto.SubscriptionDto;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;

class SubscriptionDtoTest {

    @Test()
    void геттерыСеттеры() {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(1L);
        dto.getId();
        dto.setPublisherId(1L);
        dto.getPublisherId();
        dto.setSubscriberId(1L);
        dto.getSubscriberId();
        dto.setValidFromDate(new Date());
        dto.getValidFromDate();
        dto.setValidToDate(new Date());
        dto.getValidToDate();
    }

    @Test
    void equalsAndHashCode() {
        // Всё что выше - чтобы для entity избежать ошибки prefab values
        EqualsVerifier.forClass(SubscriptionDto.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void testToString() {
        assertFalse(new SubscriptionDto().toString().isEmpty());
    }
}
