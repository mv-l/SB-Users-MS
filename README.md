Сервис "Пользователи".

### Сборка + юнит-тесты + интеграционные тесты. Для интеграционных нужен Docker.
```shell
mvn clean verify
```

### Пропуск тестов
```shell
mvn clean verify -DskipTests -DskipITs
```

В случае пропуска тестов Jacoco руганется на недостаточный процент покрытия тестами.

