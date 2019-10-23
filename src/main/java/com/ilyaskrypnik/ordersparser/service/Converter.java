package com.ilyaskrypnik.ordersparser.service;

import com.google.gson.Gson;
import com.ilyaskrypnik.ordersparser.domain.Order;
import com.ilyaskrypnik.ordersparser.dto.ProcessedOrder;
import com.ilyaskrypnik.ordersparser.dto.ProcessingData;
import com.ilyaskrypnik.ordersparser.view.ConsoleView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Класс для конвертации объектов Order в ProcessedOrder.
 * Объекты для конвертации берутся из ParsedOrderStorage
 */
@Service
public class Converter {

    private static final Logger log = LoggerFactory.getLogger(Converter.class);

    private final ConsoleView consoleView;
    private final ParsedOrderStorage parsedOrderStorage;
    private final ReadStatus readStatus;

    private final Gson gson;

    public Converter(ConsoleView consoleView, ParsedOrderStorage parsedOrderStorage, ReadStatus readStatus) {
        this.consoleView = consoleView;
        this.parsedOrderStorage = parsedOrderStorage;
        this.readStatus = readStatus;
        this.gson = new Gson();
    }

    @Async
    public void processParsedOrders() {
        while (true) {
            try {
                Map.Entry<Order, ProcessingData> orderWithInfo = parsedOrderStorage.getOrderWithInfo();

                if (orderWithInfo != null) {
                    ProcessedOrder processedOrder = ProcessedOrder.builder()
                            .orderId(orderWithInfo.getKey().getOrderId())
                            .amount(orderWithInfo.getKey().getAmount())
                            .currency(orderWithInfo.getKey().getCurrency())
                            .comment(orderWithInfo.getKey().getComment())
                            .filename(orderWithInfo.getValue().getFileName())
                            .line(orderWithInfo.getValue().getLineNumber())
                            .result(orderWithInfo.getValue().getResult())
                            .build();

                    log.info("Processing order {}", gson.toJson(processedOrder));

                    consoleView.printProcessedOrder(processedOrder);

                    if ((readStatus.getAmountOfAllFiles() == readStatus.getAmountOfReadFiles()) &&
                            parsedOrderStorage.isEmpty()) {
                        return;
                    }
                }
            } catch (InterruptedException e) {
                log.error("Converter is interrupted.", e);
            }
        }
    }
}
