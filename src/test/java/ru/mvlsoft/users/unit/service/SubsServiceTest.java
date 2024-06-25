package ru.mvlsoft.users.unit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.mvlsoft.users.entity.Subscription;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.repository.SubscriptionRepository;
import ru.mvlsoft.users.service.SubsService;
import ru.mvlsoft.users.service.UserService;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.when;

class SubsServiceTest {
    private final UserService userService = Mockito.mock();
    private final SubscriptionRepository subsRepository = Mockito.mock();
    private final SubsService subsService = new SubsService(userService, subsRepository);

    @Test
    void createSubscriber() {
        Long publisherId = 1L;
        Long subscriberId = 2L;

        when(userService.getRefById(publisherId)).thenReturn(new User(publisherId, "user1", "a", "b"));
        when(userService.getRefById(subscriberId)).thenReturn(new User(subscriberId, "user2", "a", "b"));
        when(subsRepository.save(Mockito.any(Subscription.class))).thenReturn(new Subscription());

        subsService.createSubscriber(publisherId, subscriberId);
    }

    @Test
    void getAllPublisherSubscribers() {
        Long id = 1L;
        List<Subscription> mockSubs = List.of(
                new Subscription(new User(), new User()),
                new Subscription(new User(), new User()),
                new Subscription(new User(), new User())
        );
        when(subsRepository.findAllByPublisherId(id)).thenReturn(mockSubs);

        Iterable<Subscription> subs = subsService.getAllSubscribers(id);
        Assertions.assertEquals(mockSubs.size(), ((Collection<?>) subs).size());
    }
}
