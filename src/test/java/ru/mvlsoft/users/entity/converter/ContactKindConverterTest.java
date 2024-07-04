package ru.mvlsoft.users.entity.converter;

import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.converter.ContactKindConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mvlsoft.users.entity.enums.ContactKind.EMAIL;
import static ru.mvlsoft.users.entity.enums.ContactKind.PHONE;

class ContactKindConverterTest {
    final static private ContactKindConverter converter = new ContactKindConverter();

    @Test
    void convertToDatabaseColumn() {
        assertEquals(EMAIL.getCode(), converter.convertToDatabaseColumn(EMAIL));
        assertEquals(null, converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToEntityAttribute() {
        assertEquals(PHONE, converter.convertToEntityAttribute(PHONE.getCode()));
        assertEquals(null, converter.convertToEntityAttribute(null));
    }
}
