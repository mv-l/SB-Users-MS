package ru.mvlsoft.users.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mvlsoft.users.entity.enums.UserSex;

import java.util.Date;

@Data
@NoArgsConstructor
public final class UserDto {
    Long id;
    private String userName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String title;
    private Date birthDate;
    private UserSex sex;
    private Long avatarId;
    private String status;
    private Date registrationDate = new Date();

    public UserDto(String userName, String firstName, String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
