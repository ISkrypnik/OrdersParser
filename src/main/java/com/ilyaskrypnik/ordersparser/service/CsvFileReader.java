package com.ilyaskrypnik.ordersparser.service;

import com.google.gson.Gson;
import com.ilyaskrypnik.ordersparser.dto.ProcessedOrder;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.ilyaskrypnik.ordersparser.dto.ProcessingResult.*;

@Service
public class CsvFileReader implements OrdersFileReader {

    private final Gson gson;

    public CsvFileReader() {
        this.gson = new Gson();
    }

    private String filename;

    @Override
    public void read(String uri) {
        this.filename = uri;

        try (Stream<String> lines = Files.lines(Paths.get(uri))) {
            lines.forEach(this::printResult);
        } catch (Exception e) {
            //TODO add logger
        }
    }

    private static final String CSV_DELIMITER = ",";

    private AtomicInteger currentLine = new AtomicInteger(0);

    private void printResult(String line) {
        currentLine.incrementAndGet();
        String[] orderParameters = line.split(CSV_DELIMITER);

        System.out.println(gson.toJson(getProcessedOrder(orderParameters)));
    }

    private ProcessedOrder getProcessedOrder(String[] orderParameters) {
        ProcessedOrder processedOrder;

        String result = OK.getDescription();
        long orderId = -1;
        long amount = -1;
        String currency = "";
        String comment = "";

        try {
            orderId = Long.parseLong(orderParameters[0]);
            amount = Long.parseLong(orderParameters[1]);
            currency = orderParameters[2];
            comment = orderParameters[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            result = WRONG_COLUMN_AMOUNT.getDescription();
        } catch (NumberFormatException e) {
            result = WRONG_NUMBER.getDescription();
        }

        processedOrder = ProcessedOrder.builder()
                .id(orderId)
                .amount(amount)
                .currency(currency)
                .comment(comment)
                .filename(this.filename)
                .line(currentLine.longValue())
                .result(result)
                .build();

        return processedOrder;
    }
}
