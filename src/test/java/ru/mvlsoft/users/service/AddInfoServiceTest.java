package ru.mvlsoft.users.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.mvlsoft.users.entity.AdditionalInfo;
import ru.mvlsoft.users.repository.AddInfoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class AddInfoServiceTest {

    private final UserService userService = Mockito.mock();
    private final AddInfoRepository infoRepository = Mockito.mock();
    private final AddInfoService service = new AddInfoService(userService, infoRepository);

    @Test
    void updateUserAddInfo() {
        when(infoRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.empty());
        AdditionalInfo updatedInfo = service.update(1L, 2L, new AdditionalInfo());
        assertNull(updatedInfo);
    }

}
