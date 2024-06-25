package ru.mvlsoft.users.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mvlsoft.users.entity.AdditionalInfo;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.repository.AddInfoRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddInfoService {

    private final UserService userService;
    private final AddInfoRepository addInfoRepository;

    public Iterable<AdditionalInfo> getAll(@NotNull Long userId) {
        return addInfoRepository.findAllByUserId(userId);
    }

    @Transactional
    public AdditionalInfo create(@NotNull Long userId,
                                 @NotNull AdditionalInfo addInfo) {
        // ToDo Проверить одновременное добавление нескольких записей. М.б. проблемы из-за пустого id
        User user = userService.getRefById(userId); // Ошибка при отсутствии
        user.addAddInfo(addInfo);
        userService.save(user);
        return addInfo;
    }

    public Optional<AdditionalInfo> getByIdAndUserId(@NotNull Long infoId,
                                                     @NotNull Long userId) {
        return addInfoRepository.findByIdAndUserId(infoId, userId);
    }

    @Transactional
    public AdditionalInfo update(Long userId, Long infoId, AdditionalInfo info) {
        Optional<AdditionalInfo> dbInfo = addInfoRepository.findByIdAndUserId(infoId, userId);
        if (dbInfo.isEmpty()) {
            return null;
        }
        // ToDo Решить проблему N+1
        dbInfo.get().update(info);
        return dbInfo.get();
    }

    public boolean checkExists(Long userId, Long infoId) {
        return addInfoRepository.existsByIdAndUserId(infoId, userId);
    }

    @Transactional
    public void deleteByIdAndUserId(@NotNull Long infoId, @NotNull Long userId) {
        addInfoRepository.deleteByIdAndUserId(infoId, userId);
    }
}
