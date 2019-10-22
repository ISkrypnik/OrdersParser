package com.ilyaskrypnik.ordersparser.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessedOrder {
    private final Long orderId;
    private final Long amount;
    private final String currency;
    private final String comment;
    private final String filename;
    private final Long line;
    private final String result;
}
