package ru.mvlsoft.users.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mvlsoft.users.entity.enums.ContactKind;

import static java.lang.Boolean.FALSE;

@Data
@NoArgsConstructor
public final class ContactDto {
    Long id;
    private ContactKind kind;
    private String value;
    private Boolean preferred = FALSE;
}
