package com.ilyaskrypnik.ordersparser.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ProcessedOrder {
    @NonNull
    private final Long id;
    @NonNull
    private final Long amount;
    @NonNull
    private final String currency;
    @NonNull
    private final String comment;
    @NonNull
    private final String filename;
    @NonNull
    private final Long line;
    @NonNull
    private final String result;
}
