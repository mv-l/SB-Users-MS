package ru.mvlsoft.users.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.mvlsoft.users.entity.enums.UserSex;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class SexConverter implements AttributeConverter<UserSex, String> {

    @Override
    public String convertToDatabaseColumn(UserSex sex) {
        if (sex == null) {
            return null;
        }
        return sex.getCode();
    }

    @Override
    public UserSex convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(UserSex.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
