package ru.mvlsoft.users.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserAddInfoKindConverter implements AttributeConverter<UserAdditionalInfoKind, String> {

    @Override
    public String convertToDatabaseColumn(UserAdditionalInfoKind kind) {
        if (kind == null) {
            return null;
        }
        return kind.getCode();
    }

    @Override
    public UserAdditionalInfoKind convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(UserAdditionalInfoKind.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
