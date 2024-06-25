package ru.mvlsoft.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import ru.mvlsoft.users.entity.enums.UserSex;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.SEQUENCE;
import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "users")
@SoftDelete
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Serializable {
    // Аннотации типа @Column(nullable = false) на полях не используем.
    // Т.к. структура БД все равно формируется скриптами через Liquibase а не через Hibernate.

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "users_sequence")
    @SequenceGenerator(name = "users_sequence", allocationSize = 10)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String userName;

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    private String title;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    private UserSex sex;

    private Long avatarId;

    private String status;

    @NotNull
    @Temporal(TIMESTAMP)
    private Date registrationDate = new Date();

    @Version
    private Long version;

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Setter(AccessLevel.NONE)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Setter(AccessLevel.NONE)
    private Set<AdditionalInfo> addInfos = new HashSet<>();

    @OneToMany(mappedBy = "publisher", cascade = ALL)
    @Setter(AccessLevel.NONE)
    private Set<Subscription> subscribers = new HashSet<>();

    @OneToMany(mappedBy = "subscriber", cascade = ALL)
    @Setter(AccessLevel.NONE)
    private Set<Subscription> publishers = new HashSet<>();

    public User(@NonNull String userName, @NonNull String firstName, @NonNull String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(@NonNull Long id, String userName, String firstName, String lastName) {
        this(userName, firstName, lastName);
        this.id = id;
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setUser(this);
    }

    public void addAddInfo(AdditionalInfo info) {
        this.addInfos.add(info);
        info.setUser(this);
    }

    public void addSubscription(Subscription sub) {
        this.subscribers.add(sub);
    }

    public void update(@NotNull User newData) {
        // id не меняем
        setUserName(newData.getUserName());
        setFirstName(newData.getFirstName());
        setMiddleName(newData.getMiddleName());
        setLastName(newData.getLastName());
        setTitle(newData.getTitle());
        setBirthDate(newData.getBirthDate());
        setSex(newData.getSex());
        setAvatarId(newData.getAvatarId());
        setStatus(newData.getStatus());
        setRegistrationDate(newData.getRegistrationDate());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", title='" + title + '\'' +
                ", birthDate=" + birthDate +
                ", sex=" + sex +
                ", avatarId=" + avatarId +
                ", status='" + status + '\'' +
                ", registrationDate=" + registrationDate +
                ", version=" + version +
                ", contacts=" + contacts.stream().map(Contact::toString).toList() +
                ", addInfos=" + addInfos.stream().map(AdditionalInfo::toString).toList() +
                ", subscribers=" + subscribers.stream().map(s -> s.getSubscriber().getId()).toList() +
                ", publishers=" + publishers.stream().map(s -> s.getPublisher().getId()).toList() +
                '}';
    }
}
