package com.ilyaskrypnik.ordersparser.view;

import com.google.gson.Gson;
import com.ilyaskrypnik.ordersparser.dto.ProcessedOrder;
import org.springframework.stereotype.Component;

@Component
public class ConsoleView implements View {

    private final Gson gson;

    public ConsoleView() {
        this.gson = new Gson();
    }

    @Override
    public void printProcessedOrder(ProcessedOrder processedOrder) {
        System.out.println(gson.toJson(processedOrder));
    }
}
