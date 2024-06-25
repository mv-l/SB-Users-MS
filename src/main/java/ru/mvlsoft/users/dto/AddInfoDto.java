package ru.mvlsoft.users.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public final class AddInfoDto implements Serializable {
    Long id;
    Long userId;
    private UserAdditionalInfoKind kind;
    private String value;
    private Date validFromDate;
    private Date validToDate;

    public AddInfoDto(UserAdditionalInfoKind kind, String value) {
        this.kind = kind;
        this.value = value;
    }
}
