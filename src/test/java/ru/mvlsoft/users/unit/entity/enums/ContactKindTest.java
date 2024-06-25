package ru.mvlsoft.users.unit.entity.enums;

import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.enums.ContactKind;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactKindTest {

    @Test
    void getCode() {
        assertTrue(ContactKind.PHONE.getCode().length() != 0);
    }
}
