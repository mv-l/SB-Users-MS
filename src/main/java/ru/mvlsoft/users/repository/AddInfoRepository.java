package ru.mvlsoft.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mvlsoft.users.entity.AdditionalInfo;

import java.util.Optional;

public interface AddInfoRepository extends JpaRepository<AdditionalInfo, Long> {
    Optional<AdditionalInfo> findByIdAndUserId(Long id, Long userId);

    Iterable<AdditionalInfo> findAllByUserId(Long userId);

    boolean existsByIdAndUserId(Long Id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}
