package com.ilyaskrypnik.ordersparser.service;

import com.ilyaskrypnik.ordersparser.domain.Order;
import com.ilyaskrypnik.ordersparser.dto.ProcessingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class ParsedOrderStorage {

    private static final Logger log = LoggerFactory.getLogger(ParsedOrderStorage.class);

    private final BlockingQueue<Map.Entry<Order, ProcessingData>> parsedOrders;

    public ParsedOrderStorage() {
        this.parsedOrders = new LinkedBlockingQueue<>();
    }

    public void addOrder(Order order, ProcessingData data) {
        try {
            log.info("Add order: {}, from file {}", order, data.getFileName());
            this.parsedOrders.put(new ProcessingEntry(order, data));
        } catch (InterruptedException e) {
            log.error("An error occurred while adding order to storage.", e);
        }
    }

    Map.Entry<Order, ProcessingData> getOrderWithInfo() throws InterruptedException {
        return parsedOrders.poll(10, TimeUnit.MILLISECONDS);
    }

    boolean isEmpty() {
        return parsedOrders.isEmpty();
    }

    private static final class ProcessingEntry implements Map.Entry<Order, ProcessingData> {
        private final Order order;
        private ProcessingData data;

        ProcessingEntry(Order order, ProcessingData data) {
            this.order = order;
            this.data = data;
        }

        @Override
        public Order getKey() {
            return order;
        }

        @Override
        public ProcessingData getValue() {
            return data;
        }

        @Override
        public ProcessingData setValue(ProcessingData value) {
            ProcessingData old = this.data;
            this.data = value;
            return old;
        }
    }
}
