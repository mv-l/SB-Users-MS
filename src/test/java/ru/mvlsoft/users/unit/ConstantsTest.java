package ru.mvlsoft.users.unit;

import org.junit.jupiter.api.Test;
import ru.mvlsoft.users.Constants;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConstantsTest {

    @Test
    void constants() {
        assertNotNull(new Constants());
    }

}
