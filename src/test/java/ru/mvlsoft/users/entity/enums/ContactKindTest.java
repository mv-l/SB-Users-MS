package ru.mvlsoft.users.entity.enums;

import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.entity.enums.ContactKind;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactKindTest {

    @Test
    void getCode() {
        assertFalse(ContactKind.PHONE.getCode().isEmpty());
    }
}
