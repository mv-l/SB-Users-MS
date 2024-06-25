package ru.mvlsoft.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mvlsoft.users.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Iterable<Subscription> findAllByPublisherId(Long publisherId);
}
