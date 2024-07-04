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
    private final AddInfoRepository repository;

    public Iterable<AdditionalInfo> getAll(@NotNull Long userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional
    public AdditionalInfo create(@NotNull Long userId,
                                 @NotNull AdditionalInfo addInfo) {
        // ToDo Проверить одновременное добавление нескольких записей. М.б. проблемы из-за пустого id
        User user = userService.getRefById(userId); // Ошибка при отсутствии
        // Т.к. equals и hashCode используют только id, то сперва сохраняем в БД, чтобы присвоился id.
        addInfo.setUser(user);
        repository.save(addInfo);
        // И только после сохранения в БД добавляем к родителю.
        user.addAddInfo(addInfo);
        return addInfo;
    }

    public Optional<AdditionalInfo> getByIdAndUserId(@NotNull Long infoId,
                                                     @NotNull Long userId) {
        return repository.findByIdAndUserId(infoId, userId);
    }

    @Transactional
    public AdditionalInfo update(Long userId, Long infoId, AdditionalInfo info) {
        Optional<AdditionalInfo> dbInfo = repository.findByIdAndUserId(infoId, userId);
        if (dbInfo.isEmpty()) {
            return null;
        }
        // ToDo Решить проблему N+1
        dbInfo.get().update(info);
        return dbInfo.get();
    }

    public boolean checkExists(Long userId, Long infoId) {
        return repository.existsByIdAndUserId(infoId, userId);
    }

    @Transactional
    public void deleteByIdAndUserId(@NotNull Long infoId, @NotNull Long userId) {
        repository.deleteByIdAndUserId(infoId, userId);
    }
}
