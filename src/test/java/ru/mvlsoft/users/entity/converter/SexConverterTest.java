package ru.mvlsoft.users.entity.converter;

import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.converter.SexConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.mvlsoft.users.entity.enums.UserSex.FEMALE;
import static ru.mvlsoft.users.entity.enums.UserSex.MALE;

class SexConverterTest {
    final static private SexConverter converter = new SexConverter();

    @Test
    void convertToDatabaseColumn() {
        assertEquals(FEMALE.getCode(), converter.convertToDatabaseColumn(FEMALE));
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute() {
        assertEquals(MALE, converter.convertToEntityAttribute(MALE.getCode()));
        assertNull(converter.convertToEntityAttribute(null));
    }
}
