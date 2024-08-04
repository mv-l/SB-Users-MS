package ru.mvlsoft.users.entity.converter;

import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.converter.UserAddInfoKindConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind.HOBBY;
import static ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind.SOFT_SKILLS;

class UserAddInfoKindConverterTest {
    final static private UserAddInfoKindConverter converter = new UserAddInfoKindConverter();

    @Test
    void convertToDatabaseColumn() {
        assertEquals(HOBBY.getCode(), converter.convertToDatabaseColumn(HOBBY));
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute() {
        assertEquals(SOFT_SKILLS, converter.convertToEntityAttribute(SOFT_SKILLS.getCode()));
        assertNull(converter.convertToEntityAttribute(null));
    }
}
