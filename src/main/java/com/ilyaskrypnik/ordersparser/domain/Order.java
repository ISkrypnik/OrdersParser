package com.ilyaskrypnik.ordersparser.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public class Order {
    @NonNull
    private final Long orderId;
    @NonNull
    private final Long amount;
    @NonNull
    private final String currency;
    @NonNull
    private final String comment;
}
