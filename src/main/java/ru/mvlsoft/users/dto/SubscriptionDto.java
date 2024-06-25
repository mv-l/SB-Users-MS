package ru.mvlsoft.users.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public final class SubscriptionDto implements Serializable {
    private Long id;
    private Long publisherId;
    private Long subscriberId;
    private Date validFromDate;
    private Date validToDate;
}
