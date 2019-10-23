package com.ilyaskrypnik.ordersparser.view;

import com.ilyaskrypnik.ordersparser.dto.ProcessedOrder;

public interface View {
    void printProcessedOrder(ProcessedOrder processedOrder);
}
