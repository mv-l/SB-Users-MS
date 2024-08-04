package ru.mvlsoft.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_add_info")
@SoftDelete
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdditionalInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    @SequenceGenerator(name = "users_sequence", allocationSize = 10)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private UserAdditionalInfoKind kind;

    @NotBlank
    private String value;

    @Temporal(TemporalType.DATE)
    private Date validFromDate;

    @Temporal(TemporalType.DATE)
    private Date validToDate;

    @Version
    private Long version;

    public AdditionalInfo(@NonNull UserAdditionalInfoKind kind, @NonNull String value) {
        this.kind = kind;
        this.value = value;
    }

    public AdditionalInfo(@NonNull User user, UserAdditionalInfoKind kind, String value) {
        this(kind, value);
        this.user = user;
    }

    public AdditionalInfo(@NonNull Long id, User user, UserAdditionalInfoKind kind, String value) {
        this(user, kind, value);
        this.id = id;
    }

    public void update(AdditionalInfo info) {
        this.setKind(info.getKind());
        this.setValue(info.getValue());
        this.setValidFromDate(info.getValidFromDate());
        this.setValidToDate(info.getValidToDate());
    }

    // Используем свой метод вместо Lombook @ToString т.к. при между классом User и другими классами есть двусторонняя
    // связь и их методы toString входят в бесконечный цикл до EOM.
    @Override
    public String toString() {
        return "UserAdditionalInfo{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", kind=" + kind +
                ", value='" + value + '\'' +
                ", validFromDate=" + validFromDate +
                ", validToDate=" + validToDate +
                '}';
    }
}
