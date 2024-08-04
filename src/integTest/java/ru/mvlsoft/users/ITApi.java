package ru.mvlsoft.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.mvlsoft.users.dto.AddInfoDto;
import ru.mvlsoft.users.dto.SubscriptionDto;
import ru.mvlsoft.users.dto.UserDto;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static ru.mvlsoft.users.Constants.rootPath;
import static ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind.HOBBY;
import static ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind.SOFT_SKILLS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITApi {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @LocalServerPort
    int port;

    String usersURI, userURI, addInfosURI, addInfoURI, contactsURI, contactURI, subsURI, subURI;
    WebTestClient testClient;

    UserDto createUser(int userNum) {
        User user = new User("User" + userNum, Integer.toString(userNum), Integer.toString(userNum));
        return restTemplate.postForEntity(usersURI, user, UserDto.class).getBody();
    }

    AddInfoDto createAddInfo(long userId, AddInfoDto addInfoDto) {
        return restTemplate.postForEntity(addInfosURI, addInfoDto, AddInfoDto.class, userId).getBody();
    }

    @BeforeEach
    void setUp() {
        usersURI = "http://localhost:" + port + rootPath;
        userURI = usersURI + "/{userId}";
        addInfosURI = userURI + "/addinfo";
        addInfoURI = addInfosURI + "/{infoId}";
        contactsURI = userURI + "/contacts";
        contactURI = contactsURI + "/{contactId}";
        subsURI = userURI + "/subscribers";
        subURI = userURI + "/subscribers/{subscriberId}";

        userRepository.deleteAll();
        testClient = WebTestClient.bindToServer().baseUrl(usersURI).build();
    }

    // ЮЗЕР
    @Test
    void юзерДолженСоздатьсяБезОшибокИВернутьURLНовогоЮзера() {
        UserDto newUser = new UserDto("UserOne", "one", "one");

        ResponseEntity<UserDto> response = restTemplate.postForEntity(usersURI, newUser, UserDto.class);
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());

        UserDto createdUser = response.getBody();
        assertNotNull(createdUser);

        assertNotNull(createdUser.getId());
        assertEquals(newUser.getUserName(), createdUser.getUserName());
        assertEquals(newUser.getFirstName(), createdUser.getFirstName());
        assertEquals(newUser.getLastName(), createdUser.getLastName());
    }

    @Test
    void всеЮзерыДолжныНачитатьсяБезОшибок() {
        createUser(1);
        createUser(2);
        createUser(3);
        ResponseEntity<User[]> response = restTemplate.getForEntity(usersURI, User[].class);
        User[] users = response.getBody();
        assertNotNull(users);
        assertEquals(3, users.length);
    }

    @Test
    void headДляВсехЮзеровЗапрещен() {
        testClient.head().exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void deleteДляВсехЮзеровЗапрещен() {
        testClient.delete().exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void putДляВсехЮзеровЗапрещен() {
        testClient.put().exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void patchДляВсехЮзеровЗапрещен() {
        testClient.patch().exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void единственныйЮзерДолженНачитатьсяБезОшибок() {
        // when
        UserDto userDto = createUser(1);
        // then
        assertNotNull(userDto);
        testClient.get().uri(userURI, userDto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .hasSize(1)
                .consumeWith(u -> {
                    assertNotNull(u.getResponseBody());
                    UserDto user = u.getResponseBody().getFirst();
                    assertEquals(userDto.getUserName(), user.getUserName());
                    assertEquals(userDto.getFirstName(), user.getFirstName());
                    assertEquals(userDto.getLastName(), user.getLastName());
                });
    }

    @Test
    void headДляЕдинственногоЮзераДолженНачитатьсяБезОшибок() {
        // when
        UserDto userDto = createUser(1);
        // then
        assertNotNull(userDto);
        testClient.head().uri(userURI, userDto.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void postДляЮзераЗапрещен() {
        testClient.post().uri(userURI, 1L).exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void patchДляЮзераЗапрещен() {
        testClient.patch().uri(userURI, 1L).exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void существующийЮзерДолженИзменятьсяБезОшибок() {
        // given: есть существующий юзер
        UserDto existedUser = createUser(1);
        assertNotNull(existedUser);

        // when: изменяется част его полей
        UserDto editedUser = new UserDto("UserTwo", "two", existedUser.getLastName());
        testClient.put().uri(userURI, existedUser.getId())
                .contentType(APPLICATION_JSON)
                .bodyValue(editedUser)
                .exchange().expectStatus().isOk();

        // then: Пользователь должен измениться правильно
        testClient.get().uri(userURI, existedUser.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .hasSize(1)
                .consumeWith(u -> {
                    assertNotNull(u.getResponseBody());
                    UserDto user = u.getResponseBody().getFirst();
                    assertEquals(editedUser.getUserName(), user.getUserName());
                    assertEquals(editedUser.getFirstName(), user.getFirstName());
                    assertEquals(existedUser.getLastName(), user.getLastName());
                });
    }

    @Test
    void существующийЮзерДолженУдалятьсяБезОшибок() {
        // given: есть несколько юзеров
        createUser(1);
        createUser(2);
        UserDto user3 = createUser(3);
        assertNotNull(user3);
        testClient.get().exchange().expectStatus().isOk().expectBodyList(UserDto.class).hasSize(3);

        // when: если удалить одного из них
        testClient.delete().uri(userURI, user3.getId()).exchange().expectStatus().isOk();

        // then: Юзеров должно стать на одного меньше
        testClient.get().exchange().expectStatus().isOk().expectBodyList(UserDto.class).hasSize(2);
    }


    // ДОП.ИНФОРМАЦИЯ
    @Test
    void допИнфоДляСуществующегоЮзераДолжнаДобавитьсяИВернутьURI() {
        // given
        UserDto user = createUser(1);
        assertNotNull(user);

        // when
        AddInfoDto infoDto = new AddInfoDto(HOBBY, "Drinking");
        WebTestClient.ResponseSpec resp = testClient.post()
                .uri(addInfosURI, user.getId())
                .bodyValue(infoDto)
                .exchange();

        // then
        resp.expectStatus().isCreated();

        resp.expectBodyList(AddInfoDto.class).hasSize(1).consumeWith(i -> {
            assertNotNull(i.getResponseBody());
            AddInfoDto createdInfo = i.getResponseBody().getFirst();
            assertNotNull(createdInfo.getId());
            assertEquals(user.getId(), createdInfo.getUserId());
            assertEquals(infoDto.getKind(), createdInfo.getKind());
            assertEquals(infoDto.getValue(), createdInfo.getValue());
            assertEquals(infoDto.getValidFromDate(), createdInfo.getValidFromDate());
            assertEquals(infoDto.getValidToDate(), createdInfo.getValidToDate());
        });

        resp.expectHeader().value("Location", s -> {
            assertTrue(s.matches("^" + usersURI + "/\\d+/addinfo/\\d+$"));
        });
    }

    @Test
    void всяДопИнфаДляЮзераДолжнаНачитаться() {
        // given: Два юзера. У первого две доп.информации, у второго одна.
        UserDto user1 = createUser(1);
        assertNotNull(user1);
        UserDto user2 = createUser(2);
        assertNotNull(user2);
        AddInfoDto user1Info1 = new AddInfoDto(HOBBY, "Drinking");
        testClient.post().uri(addInfosURI, user1.getId()).bodyValue(user1Info1).exchange();
        AddInfoDto user1Info2 = new AddInfoDto(SOFT_SKILLS, "Ignoring people");
        testClient.post().uri(addInfosURI, user1.getId()).bodyValue(user1Info2).exchange();
        AddInfoDto user2Info1 = new AddInfoDto(HOBBY, "To sleep");
        testClient.post().uri(addInfosURI, user2.getId()).bodyValue(user2Info1).exchange();

        // when: Начитываем информацию первого юзера.
        WebTestClient.ResponseSpec resp;
        resp = testClient.get().uri(addInfosURI, user1.getId()).exchange();

        // then: должно быть два элемента.
        resp.expectStatus().isOk();
        resp.expectBodyList(AddInfoDto.class).hasSize(2);
    }

    @Test
    void headДляВсейДопИнформацииЗапрещен() {
        testClient.head().uri(addInfosURI, 1L).exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void deleteВсейДопИнформацииЗапрещен() {
        testClient.delete().uri(addInfosURI, 1L).exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void putВсейДопИнформацииЗапрещен() {
        testClient.put().uri(addInfosURI, 1L).exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void patchВсейДопИнформацииЗапрещен() {
        testClient.patch().uri(addInfosURI, 1L).exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void конкретнаяДопИнфаДолжнаНачитаться() {
        // given
        UserDto user1 = createUser(1);
        UserDto user2 = createUser(2);
        AddInfoDto user1Info1 = createAddInfo(user1.getId(), new AddInfoDto(HOBBY, "Drinking"));
        createAddInfo(user2.getId(), new AddInfoDto(SOFT_SKILLS, "aaaa"));
        createAddInfo(user2.getId(), new AddInfoDto(HOBBY, "bbb"));

        // when
        WebTestClient.ResponseSpec resp = testClient.get().uri(addInfoURI, user1.getId(), user1Info1.getId()).exchange();
        // then
        resp.expectStatus().isOk().expectBodyList(AddInfoDto.class).hasSize(1).consumeWith(i -> {
            assertNotNull(i.getResponseBody());
            AddInfoDto info = i.getResponseBody().getFirst();
            assertEquals(user1.getId(), info.getUserId());
            assertEquals(user1Info1.getId(), info.getId());
            assertEquals(user1Info1.getKind(), info.getKind());
            assertEquals(user1Info1.getValue(), info.getValue());
        });
    }

    @Test
    void postКонкретнойОднойДопИнформацииЗапрещен() {
        testClient.post().uri(addInfoURI, 1L, 2L).exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void patchКонкретнойДопИнформацииЗапрещен() {
        testClient.patch().uri(addInfoURI, 1L, 2L).exchange().expectStatus().isEqualTo(METHOD_NOT_ALLOWED);
    }

    @Test
    void существующаяДопИнфаДолжнаИзменятьсяБезОшибок() {
        // given: есть существующий юзер и его доп.инфа
        UserDto user = createUser(1);
        assertNotNull(user);
        AddInfoDto storedInfo = createAddInfo(user.getId(), new AddInfoDto(HOBBY, "Drinking"));

        // when: доп.инфа изменяется
        AddInfoDto editedInfo = new AddInfoDto(HOBBY, "Skiing");
        testClient.put().uri(addInfoURI, user.getId(), storedInfo.getId())
                .bodyValue(editedInfo)
                .exchange().expectStatus().isOk();

        // then: Данные должны измениться правильно
        testClient.get().uri(addInfoURI, user.getId(), storedInfo.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AddInfoDto.class)
                .hasSize(1)
                .consumeWith(u -> {
                    assertNotNull(u.getResponseBody());
                    AddInfoDto info = u.getResponseBody().getFirst();
                    assertEquals(user.getId(), info.getUserId());
                    assertEquals(storedInfo.getId(), info.getId());
                    assertEquals(storedInfo.getKind(), info.getKind());
                    assertEquals(editedInfo.getValue(), info.getValue());
                });
    }

    @Test
    void существующаяДопИнфаДолжнаУдалятьсяБезОшибок() {
        // given
        UserDto user1 = createUser(1);
        UserDto user2 = createUser(2);
        AddInfoDto user1Info1 = createAddInfo(user1.getId(), new AddInfoDto(HOBBY, "Drinking"));
        createAddInfo(user1.getId(), new AddInfoDto(SOFT_SKILLS, "aaaa"));
        createAddInfo(user2.getId(), new AddInfoDto(HOBBY, "ccc"));

        // when
        testClient.delete().uri(addInfoURI, user1.getId(), user1Info1.getId()).exchange().expectStatus().isOk();

        // then
        testClient.get().uri(addInfosURI, user1.getId()).exchange().expectStatus().isOk().expectBodyList(AddInfoDto.class).hasSize(1);
    }


    // КОНТАКТЫ


    // ПОДПИСКИ
    @Test
    void должнаБезОшибкиДобавитьсяПодпискаМеждуДвумяСуществующимиЮзерами() {
        UserDto user1 = createUser(1);
        UserDto user2 = createUser(2);
        testClient.post().uri(subURI, user1.getId(), user2.getId())
                .exchange().expectStatus().isCreated();
    }

    @Test
    void существующиеПодпискидолжнаНачитыватьсяБезОшибки() {
        // given
        UserDto user1 = createUser(1);
        UserDto user2 = createUser(2);
        UserDto user3 = createUser(3);
        // Юзер2 подписан на Юзера1
        testClient.post().uri(subURI, user1.getId(), user2.getId()).exchange().expectStatus().isCreated();
        // Юзер3 подписан на Юзера1
        testClient.post().uri(subURI, user1.getId(), user3.getId()).exchange().expectStatus().isCreated();
        // Юзер3 подписан на Юзера2
        testClient.post().uri(subURI, user2.getId(), user3.getId()).exchange().expectStatus().isCreated();

        // then
        // У Юзера1 д.б. два подписчика
        testClient.get().uri(subsURI, user1.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SubscriptionDto.class)
                .hasSize(2);
    }
}
