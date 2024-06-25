package ru.mvlsoft.users.contoller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mvlsoft.users.dto.SubscriptionDto;
import ru.mvlsoft.users.entity.Subscription;
import ru.mvlsoft.users.service.SubsService;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.http.HttpStatus.CREATED;
import static ru.mvlsoft.users.Constants.rootPath;

@RestController
@RequestMapping(rootPath + "/{userId}")
@RequiredArgsConstructor
public class SubsriptionController {

    private final SubsService service;
    @Autowired
    private final ModelMapper mapper;

    @PostMapping("/subscribers/{subscriberId}")
    @ResponseStatus(CREATED)
    void addSubscription(@NotNull @PathVariable Long userId,
                         @NotNull @PathVariable Long subscriberId) {
        service.createSubscriber(userId, subscriberId);
    }

    @GetMapping("/subscribers")
    Iterable<SubscriptionDto> getAllPublisherSubscribers(@NotNull @PathVariable Long userId) {
        Iterable<Subscription> subs = service.getAllSubscribers(userId);
        return StreamSupport.stream(subs.spliterator(), false)
                .map(o -> mapper.map(o, SubscriptionDto.class))
                .collect(Collectors.toList());
    }
}
