package ru.mvlsoft.users.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.mvlsoft.users.entity.enums.ContactKind;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ContactKindConverter implements AttributeConverter<ContactKind, String> {

    @Override
    public String convertToDatabaseColumn(ContactKind kind) {
        if (kind == null) {
            return null;
        }
        return kind.getCode();
    }

    @Override
    public ContactKind convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(ContactKind.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
