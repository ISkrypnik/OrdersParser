package com.ilyaskrypnik.ordersparser.service.reader;

import com.ilyaskrypnik.ordersparser.domain.Order;
import com.ilyaskrypnik.ordersparser.dto.ProcessingData;
import com.ilyaskrypnik.ordersparser.exception.UnsupportedExtensionException;
import com.ilyaskrypnik.ordersparser.service.FileExtensionDetector;
import com.ilyaskrypnik.ordersparser.service.ParsedOrderStorage;
import com.ilyaskrypnik.ordersparser.service.ReadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.ilyaskrypnik.ordersparser.domain.SupportedFileExtensions.CSV;
import static com.ilyaskrypnik.ordersparser.dto.ProcessingResult.*;

@Service
public class CsvFileReader implements OrdersFileReader {

    private static final Logger log = LoggerFactory.getLogger(CsvFileReader.class);

    private final FileExtensionDetector fileExtensionDetector;
    private final ParsedOrderStorage parsedOrderStorage;
    private final ReadStatus readStatus;

    public CsvFileReader(FileExtensionDetector fileExtensionDetector, ParsedOrderStorage parsedOrderStorage,
                         ReadStatus readStatus) {
        this.fileExtensionDetector = fileExtensionDetector;
        this.parsedOrderStorage = parsedOrderStorage;
        this.readStatus = readStatus;
    }

    private String filename;

    @Async
    @Override
    public void read(String uri) {
        try {
            if (fileExtensionDetector.getFileExtension(uri) != CSV) {
                return;
            }
        } catch (UnsupportedExtensionException e) {
            log.error("Unsupported extension.", e);
            return;
        }
        this.filename = uri;

        try (Stream<String> lines = Files.lines(Paths.get(uri))) {
            lines.forEach(this::parseOrder);
        } catch (FileNotFoundException e) {
            log.error("File '{}' not found.", uri, e);
        } catch (Exception e) {
            log.error("An error occurred while reading file {}.", uri, e);
        } finally {
            readStatus.incrementAmountOfReadFiles();
        }
    }

    @Value("${csv.delimiter}")
    private String CSV_DELIMITER;

    private AtomicInteger currentLine = new AtomicInteger(0);

    private void parseOrder(String line) {
        currentLine.incrementAndGet();
        String[] orderParameters = line.split(CSV_DELIMITER);

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
            result = WRONG_NUMBER_FORMAT.getDescription();
        }

        parsedOrderStorage.addOrder(new Order(orderId, amount, currency, comment),
                new ProcessingData(result, this.filename, currentLine.longValue()));
    }
}
