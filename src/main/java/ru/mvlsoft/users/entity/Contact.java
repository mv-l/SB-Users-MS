package ru.mvlsoft.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import ru.mvlsoft.users.entity.enums.ContactKind;

import java.io.Serializable;

import static jakarta.persistence.GenerationType.SEQUENCE;
import static java.lang.Boolean.FALSE;

@Entity
@Table(name = "user_contacts")
@SoftDelete
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "users_sequence")
    @SequenceGenerator(name = "users_sequence", allocationSize = 10)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private ContactKind kind;

    @NotBlank
    private String value;

    private Boolean preferred = FALSE;

    @Version
    private Long version;

    public Contact(@NonNull ContactKind kind, @NonNull String value) {
        this.kind = kind;
        this.value = value;
    }

    public Contact(@NonNull User user, ContactKind kind, String value) {
        this(kind, value);
        this.user = user;
    }

    public Contact(@NonNull Long id, User user, ContactKind kind, String value) {
        this(user, kind, value);
        this.id = id;
    }

    public void update(@NonNull Contact newData) {
        setKind(newData.getKind());
        setValue(newData.getValue());
        setPreferred(newData.getPreferred());
    }

    // Используем свой метод вместо Lombook @ToString т.к. при между классом User и другими классами есть двусторонняя
    // связь и их методы toString входят в бесконечный цикл до EOM.
    @Override
    public String toString() {
        return "UserContact{" +
                "id=" + id +
                ", user=" + (user == null ? "null" : user.getId()) +
                ", kind=" + kind +
                ", value='" + value + '\'' +
                ", preferred=" + preferred +
                '}';
    }
}
