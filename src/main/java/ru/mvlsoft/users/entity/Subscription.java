package ru.mvlsoft.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_subscriptions")
@SoftDelete
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Subscription implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    @SequenceGenerator(name = "users_sequence", allocationSize = 10)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @ManyToOne
    private User publisher;

    @NotNull
    @ManyToOne
    private User subscriber;

    @Temporal(TemporalType.DATE)
    private Date validFromDate;

    @Temporal(TemporalType.DATE)
    private Date validToDate;

    @Version
    private Long version;

    public Subscription(@NonNull User publisher, @NonNull User subscriber) {
        this.publisher = publisher;
        this.subscriber = subscriber;
    }

    public Subscription(@NonNull Long id, User publisher, User subscriber) {
        this(publisher, subscriber);
        this.id = id;
    }

    public void update(@NonNull Subscription newData) {
        // id не меняем
        setPublisher(newData.getPublisher());
        setSubscriber(newData.getSubscriber());
        setValidFromDate(newData.getValidFromDate());
        setValidToDate(newData.getValidToDate());
    }

    // Используем свой метод вместо Lombook @ToString т.к. при между классом User и другими классами есть двусторонняя
    // связь и их методы toString входят в бесконечный цикл до EOM.
    @Override
    public String toString() {
        return "UserSubscription{" +
                "id=" + id +
                ", publisher=" + (publisher == null ? "null" : publisher.getId()) +
                ", subscriber=" + (subscriber == null ? "null" : subscriber.getId()) +
                ", validFromDate=" + validFromDate +
                ", validToDate=" + validToDate +
                '}';
    }
}
