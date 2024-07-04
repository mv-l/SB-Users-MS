package ru.mvlsoft.users.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.mvlsoft.users.entity.AdditionalInfo;
import ru.mvlsoft.users.entity.Contact;
import ru.mvlsoft.users.entity.Subscription;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.entity.enums.ContactKind;
import ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class UserRepositoryIT {

    static final String INSERT_USER_SQL = """
            INSERT INTO users (id, user_name, first_name, last_name, registration_date)
            VALUES (:id, :userName, :firstName, :lastName, :regDate)""";
    static final String INSERT_USER_CONTACT_SQL = """
            INSERT INTO user_contacts (id, user_id, kind, value)
            VALUES (:id, :userId, :kind, :value)""";
    static final String INSERT_USER_ADDINFO_SQL = """
            INSERT INTO user_add_info (id, user_id, kind, value)
            VALUES (:id, :userId, :kind, :value)""";
    static final String INSERT_USER_SUBSCRIPTION_SQL = """
            INSERT INTO user_subscriptions (id, subscriber_user_id, publisher_user_id)
            VALUES (:id, :subscriber, :publisher)""";
    static final String LAT = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String CYR = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    static final String DIGITS = "0123456789";

    @Autowired
    UserRepository userRepo;

    @Autowired
    SubscriptionRepository subsRepo;

    @Autowired
    EntityManager em;
    @Autowired
    private DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

    /****** Вспомогательные методы ******/
    String randomString(boolean isLat, int minSize, int maxSize) {
        return RandomStringUtils.random(new Random().nextInt(minSize, maxSize), isLat ? LAT : CYR);
    }

    String randomString(boolean isLat) {
        return randomString(isLat, 5, 31);
    }

    // Генерация случайных данных для пользователя с указанным или случайным кол-вом контактов и доп.информации.
    User generateRandomUser(int contactCount, int addInfoCount) {
        final Random rnd = new Random();

        // Случайно выбираем на латиннице или на кириллице генерировать ФИО.
        boolean latOrCyr = (rnd.nextInt(2) == 0);
        User user = new User(randomString(true), randomString(latOrCyr), randomString(latOrCyr));

        int count = (contactCount < 0) ? rnd.nextInt(10) : contactCount;
        // Генерируем нужное число контактов
        for (int i = 0; i < count; i++) {
            Contact contact = new Contact();
            user.addContact(contact);
            boolean kind = rnd.nextInt(2) == 0;
            contact.setKind(kind ? ContactKind.PHONE : ContactKind.EMAIL);
            if (kind) {
                contact.setValue(randomString(true, 5, 15) + "@" + randomString(true, 5, 15) + ".com");
            } else {
                contact.setValue(RandomStringUtils.random(rnd.nextInt(10, 11), DIGITS));
            }
        }

        count = (addInfoCount < 0) ? rnd.nextInt(10) : addInfoCount;
        // Генерируем нужное число доп.данных
        for (int i = 0; i < count; i++) {
            AdditionalInfo info = new AdditionalInfo();
            user.addAddInfo(info);
            boolean kind = rnd.nextInt(2) == 0;
            info.setKind(kind ? UserAdditionalInfoKind.HOBBY : UserAdditionalInfoKind.SOFT_SKILLS);
            info.setValue(randomString(false, 50, 200));
        }

        return user;
    }

    // Генерация случайных данных для пользователя без контактов и без доп.информации.
    User generateRandomUser() {
        return generateRandomUser(0, 0);
    }

    // Генерация нужного кол-ва юзеров со случайными данными, без контактов и без доп.информации.
    List<User> generateRandomUsers(int userCount) {
        List<User> list = new ArrayList<>(userCount);
        for (int i = 0; i < userCount; i++) {
            User user = generateRandomUser();
            // Чтобы гарантировать уникальность UserName
            user.setUserName(user.getUserName() + i);
            list.add(user);
        }
        return list;
    }

    Long getSequenceNextValueFromDB() {
        return (Long) em.createNativeQuery("SELECT nextval('users_sequence')").getSingleResult();
    }

    // Через рефлексию устанавливаем ID чтобы equals и hashCode правильно работали
    void setEntityId(Class entityClass, Object entityObject, Long value) {
        Field field;
        try {
            field = entityClass.getDeclaredField("id");
            field.setAccessible(true);
            field.set(entityObject, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось установить значение Id для сущности класса " + entityClass);
        }
    }

    // Запись в БД подписки одного юзера на другого.
    public void rawCreateUserSubscription(User subscriber, User publisher) {
        em.createNativeQuery(INSERT_USER_SUBSCRIPTION_SQL)
                .setParameter("id", getSequenceNextValueFromDB())
                .setParameter("subscriber", subscriber.getId())
                .setParameter("publisher", publisher.getId())
                .executeUpdate();
        //subscriber.addSubscription(publisher);
    }

    // Запись в БД нужного кол-ва юзеров со случайными данными, с нужным или случайным кол-вом контактов и доп.информации.
    public List<User> rawCreateUsers(int userCount, int contactCount, int addInfoCount) {
        final Query insUserQuery = em.createNativeQuery(INSERT_USER_SQL);
        final Query insUserContact = em.createNativeQuery(INSERT_USER_CONTACT_SQL);
        final Query insUserAddInfo = em.createNativeQuery(INSERT_USER_ADDINFO_SQL);

        // Добавляем 4 юзера
        List<User> users = new ArrayList<>(userCount);
        // Генерируем 4 случайных юзера
        for (int i = 1; i <= userCount; i++) {
            User user = generateRandomUser(contactCount, addInfoCount);
            users.add(user);
            // Через рефлексию устанавливаем ID чтобы equals и hashCode правильно работали
            setEntityId(User.class, user, getSequenceNextValueFromDB());

            insUserQuery.setParameter("id", user.getId());
            insUserQuery.setParameter("userName", user.getUserName());
            insUserQuery.setParameter("firstName", user.getFirstName());
            insUserQuery.setParameter("lastName", user.getLastName());
            insUserQuery.setParameter("regDate", user.getRegistrationDate());
            insUserQuery.executeUpdate();

            for (Contact contact : user.getContacts()) {
                setEntityId(Contact.class, contact, getSequenceNextValueFromDB());
                insUserContact.setParameter("id", contact.getId());
                insUserContact.setParameter("userId", users.getLast().getId());
                insUserContact.setParameter("kind", contact.getKind().getCode());
                insUserContact.setParameter("value", contact.getValue());
                insUserContact.executeUpdate();
            }

            for (AdditionalInfo addInfo : user.getAddInfos()) {
                setEntityId(AdditionalInfo.class, addInfo, getSequenceNextValueFromDB());
                insUserAddInfo.setParameter("id", addInfo.getId());
                insUserAddInfo.setParameter("userId", users.getLast().getId());
                insUserAddInfo.setParameter("kind", addInfo.getKind().getCode());
                insUserAddInfo.setParameter("value", addInfo.getValue());
                insUserAddInfo.executeUpdate();
            }
        }
        //TestTransaction.flagForCommit();
        return users;
    }

    // Запись в БД нужного кол-ва юзеров со случайными данными, со случайным кол-вом контактов и доп.информации.
    List<User> rawCreateUsers(int userCount) {
        return rawCreateUsers(userCount, -1, -1);
    }

    /****** Тесты ******/
//    @BeforeEach
//    void setUp() {
//        Query query = em.createNativeQuery("TRUNCATE TABLE user_subscriptions, user_add_info, user_contacts, users CASCADE");
//        query.executeUpdate();
//    }

//    @Test
//    void поискЮзераПоЛогину() {
//        List<User> list = rawCreateUsers(3);
//        User userToFind = list.get(2);
//        User foundUser = userRepo.findByUserName(userToFind.getUserName());
//        assertEquals(userToFind, foundUser);
//    }
//
//    @Test
//    void дляПеречисленияПолВБДЗаписываетсяПравильноеЗначение() {
//        final Query query = em.createNativeQuery("SELECT sex FROM users WHERE id = :id", String.class);
//
//        // Пишем в БД
//        User user = generateRandomUser();
//        user.setSex(UserSex.MALE);
//        userRepo.saveAndFlush(user);
//
//        // А теперь выбираем "сырые" данные нативным запросом
//        query.setParameter("id", user.getId());
//        String sex = (String) query.getSingleResult();
//
//        // Проверяем
//        assertEquals(user.getSex().getCode(), sex);
//
//        // Всё то же самое для другого пола
//        user = generateRandomUser();
//        user.setSex(UserSex.FEMALE);
//        userRepo.saveAndFlush(user);
//
//        query.setParameter("id", user.getId());
//        sex = (String) query.getSingleResult();
//
//        assertEquals(user.getSex().getCode(), sex);
//    }
//
//    @Test
//    void всеСвязанныеДанныеПравильноСчитываются() {
//        final List<User> users = rawCreateUsers(3);
//
//        // Юзер3 подписан на Юзера1.
//        rawCreateUserSubscription(users.get(2), users.getFirst());
//
//        for (User rawUser : users) {
//            final User jpaUser = userRepo.findById(rawUser.getId()).orElseThrow();
//            assertEquals(rawUser.getContacts().size(), jpaUser.getContacts().size());
//            assertEquals(rawUser.getAddInfos().size(), jpaUser.getAddInfos().size());
//            //assertEquals(rawUser.getSubscriptions().size(), jpaUser.getSubscriptions().size());
//        }
//    }
//
////    @Test
////    void юзерНеМожетПодписатьсяНаСебя() {
////        final User user = generateRandomUser(0, 0);
////        user.addSubscription(user);
////        assertThrows(DataIntegrityViolationException.class, () -> userRepo.saveAndFlush(user));
////    }
//
//    @Test
//    void логинЮзераУникальныйБезУчетаРегистра() {
//        final List<User> users = generateRandomUsers(2);
//        // Ставим Юзеру1 имя в нижнем регистре.
//        users.getFirst().setUserName(users.getFirst().getUserName().toLowerCase());
//        // Ставим Юзеру2 такое же имя как у Юзера1, но в другом регистре.
//        users.get(1).setUserName(users.getFirst().getUserName().toUpperCase());
//        // Проверяем что сработал уникальный индекс.
//        assertThrows(DataIntegrityViolationException.class, () -> userRepo.saveAllAndFlush(users));
//    }
//
//    @Test
//    void мягкоеУдалениеКорректноРаботает() {
//        final Query query = em.createNativeQuery("SELECT deleted FROM users WHERE id = :id", Boolean.class);
//
//        // Пишем в БД
//        final User user = generateRandomUser();
//        userRepo.saveAndFlush(user);
//
//        // А теперь выбираем "сырые" данные нативным запросом и проверяем.
//        query.setParameter("id", user.getId());
//        Boolean deleted = (Boolean) query.getSingleResult();
//        assertFalse(deleted);
//
//        // Удаляем юзера
//        userRepo.delete(user);
//        em.flush();
//
//        // Проверяем
//        deleted = (Boolean) query.getSingleResult();
//        assertTrue(deleted);
//
//        // Проверяем что пользователь теперь НЕ начитывается через JPA
//        final List<User> users = userRepo.findAll();
//        assertEquals(0, users.size());
//    }
//
//    @Test
//    void номерВерсииСтрокРастет() {
//        // Пишем в БД
//        final User user = generateRandomUser();
//        userRepo.saveAndFlush(user);
//        user.setStatus("aaaa");
//        userRepo.saveAndFlush(user);
//        user.addContact(new Contact(user, ContactKind.EMAIL, "aaa@aaa.com"));
//        userRepo.saveAndFlush(user);
//
//        // А теперь выбираем "сырые" данные нативным запросом и проверяем.
//        Query query = em.createNativeQuery("SELECT version FROM users WHERE id = :id", Long.class);
//        query.setParameter("id", user.getId());
//        long version = (long) query.getSingleResult();
//        assertNotNull(version);
//        assertTrue(version == 1);
//
//        query = em.createNativeQuery("SELECT version FROM user_contacts WHERE user_id = :id", Long.class);
//        query.setParameter("id", user.getId());
//        version = (long) query.getSingleResult();
//        assertNotNull(version);
//        assertTrue(version == 0);
//    }
//
//    @Test
//    void оптимистическаяБлокировкаРаботает() {
//        // Пишем в БД
//        final User user = generateRandomUser();
//        userRepo.saveAndFlush(user);
//
//        // Меняем версию через прямой SQL
//        em.createNativeQuery("UPDATE users SET version = version+1 WHERE id = :id")
//                .setParameter("id", user.getId())
//                .executeUpdate();
//
//        // А теперь делаем обновление данных и ожидаем ошибку
//        user.setAvatarId(1L);
//        assertThrows(ObjectOptimisticLockingFailureException.class, () -> userRepo.saveAndFlush(user));
//
//    }
    @Test
    void подписка1() {
        User user1 = generateRandomUser();
        User user2 = generateRandomUser();
        User user3 = generateRandomUser();
        userRepo.saveAll(List.of(user1, user2, user3));

        Subscription sub = new Subscription(user1, user2);
        subsRepo.save(sub);
        sub = new Subscription(user1, user3);
        subsRepo.save(sub);
        em.flush();

        em.detach(user1);
        User dbUser1 = userRepo.findById(user1.getId()).get();

        assertEquals(dbUser1.getSubscribers().size(), 2);
    }
}
