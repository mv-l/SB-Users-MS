package ru.mvlsoft.users.unit.entity.converter;

import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.converter.SexConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mvlsoft.users.entity.enums.UserSex.FEMALE;
import static ru.mvlsoft.users.entity.enums.UserSex.MALE;

class SexConverterTest {
    final static private SexConverter converter = new SexConverter();

    @Test
    void convertToDatabaseColumn() {
        assertEquals(FEMALE.getCode(), converter.convertToDatabaseColumn(FEMALE));
        assertEquals(null, converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute() {
        assertEquals(MALE, converter.convertToEntityAttribute(MALE.getCode()));
        assertEquals(null, converter.convertToEntityAttribute(null));
    }
}
