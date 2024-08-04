package ru.mvlsoft.users.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mvlsoft.users.entity.Subscription;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.repository.SubscriptionRepository;

@Service
@RequiredArgsConstructor
public class SubsService {

    private final UserService userService;
    private final SubscriptionRepository repository;

    @Transactional
    public void createSubscriber(@NotNull Long publisherId,
                                 @NotNull Long subscriberId) {
        User publisher = userService.getRefById(publisherId);
        User subscriber = userService.getRefById(subscriberId);
        Subscription subscription = new Subscription(publisher, subscriber);
        repository.save(subscription);
        publisher.getSubscribers().add(subscription);
        subscriber.getPublishers().add(subscription);
    }

    public Iterable<Subscription> getAllSubscribers(@NotNull Long publisherUserId) {
        return repository.findAllByPublisherId(publisherUserId);
    }
}
