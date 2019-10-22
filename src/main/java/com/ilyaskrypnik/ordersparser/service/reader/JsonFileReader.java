package com.ilyaskrypnik.ordersparser.service.reader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ilyaskrypnik.ordersparser.domain.Order;
import com.ilyaskrypnik.ordersparser.dto.ProcessingData;
import com.ilyaskrypnik.ordersparser.exception.UnsupportedExtensionException;
import com.ilyaskrypnik.ordersparser.service.FileExtensionDetector;
import com.ilyaskrypnik.ordersparser.service.ParsedOrderStorage;
import com.ilyaskrypnik.ordersparser.service.ReadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ilyaskrypnik.ordersparser.domain.SupportedFileExtensions.JSON;
import static com.ilyaskrypnik.ordersparser.dto.ProcessingResult.OK;

@Service
public class JsonFileReader implements OrdersFileReader {

    private static final Logger log = LoggerFactory.getLogger(JsonFileReader.class);

    private final FileExtensionDetector fileExtensionDetector;
    private final ParsedOrderStorage parsedOrderStorage;
    private final ReadStatus readStatus;

    private final Gson gson;

    public JsonFileReader(FileExtensionDetector fileExtensionDetector, ParsedOrderStorage parsedOrderStorage,
                          ReadStatus readStatus) {
        this.fileExtensionDetector = fileExtensionDetector;
        this.parsedOrderStorage = parsedOrderStorage;
        this.readStatus = readStatus;
        this.gson = new Gson();
    }

    private AtomicInteger currentLine = new AtomicInteger(0);

    @Async
    @Override
    public void read(String uri) {

        try {
            if (fileExtensionDetector.getFileExtension(uri) != JSON) {
                return;
            }
        } catch (UnsupportedExtensionException e) {
            log.error("Unsupported extension.", e);
            return;
        }

        try {
            JsonReader jsonReader = new JsonReader(new FileReader(uri));
            Type listType = new TypeToken<List<Order>>(){}.getType();
            List<Order> orders = gson.fromJson(jsonReader, listType);

            for (Order order : orders) {
                currentLine.incrementAndGet();
                parsedOrderStorage.addOrder(order, new ProcessingData(OK.getDescription(), uri, currentLine.longValue()));
            }
        } catch (FileNotFoundException e) {
            log.error("File '{}' not found.", uri, e);
        } catch (Exception e) {
            log.error("An error occurred while reading file {}.", uri, e);
        } finally {
            readStatus.incrementAmountOfReadFiles();
        }
    }
}
