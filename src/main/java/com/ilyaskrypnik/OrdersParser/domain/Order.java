package com.ilyaskrypnik.OrdersParser.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public class Order {
    @NonNull
    private final Long id;
    @NonNull
    private final Long Amount;
    @NonNull
    private final String currency;
    @NonNull
    private final String comment;
}
