package ru.mvlsoft.users.unit.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.dto.SubscriberDto;

import static org.junit.jupiter.api.Assertions.assertFalse;

class SubscriberDtoTest {

    @Test()
    void геттерыСеттеры() {
        SubscriberDto dto = new SubscriberDto();
        dto.setSubscriber_id(1L);
        dto.getSubscriber_id();
    }

    @Test
    void equalsAndHashCode() {
        // Всё что выше - чтобы для entity избежать ошибки prefab values
        EqualsVerifier.forClass(SubscriberDto.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void testToString() {
        assertFalse(new SubscriberDto().toString().isEmpty());
    }
}
