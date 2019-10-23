package com.ilyaskrypnik.ordersparser.service.reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.ilyaskrypnik.ordersparser.domain.Order;
import com.ilyaskrypnik.ordersparser.dto.ProcessingData;
import com.ilyaskrypnik.ordersparser.dto.ProcessingResult;
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
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ilyaskrypnik.ordersparser.domain.SupportedFileExtensions.JSON;
import static com.ilyaskrypnik.ordersparser.dto.ProcessingResult.*;

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
        this.gson = new GsonBuilder().registerTypeAdapter(Long.class, new LongTypeAdapter()).create();
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
            Type listType = new TypeToken<List<Order>>() {
            }.getType();
            List<Order> orders = gson.fromJson(jsonReader, listType);

            for (Order order : orders) {
                currentLine.incrementAndGet();
                ProcessingResult result;
                result = checkOrderFields(order);
                parsedOrderStorage.addOrder(order, new ProcessingData(result.getDescription(), uri, currentLine.longValue()));
            }
        } catch (FileNotFoundException e) {
            log.error("File '{}' not found.", uri, e);
        } catch (NumberFormatException e) {
            log.error(WRONG_NUMBER_FORMAT.getDescription(), e);
        } catch (Exception e) {
            log.error("An error occurred while reading file {}.", uri, e);
        } finally {
            readStatus.incrementAmountOfReadFiles();
        }
    }

    private ProcessingResult checkOrderFields(Order order) {
        if (order.getOrderId() == null || order.getAmount() == null || order.getCurrency() == null || order.getComment() == null) {
            return NULL_ORDER_PARAM;
        }
        if (order.getOrderId() == -1 || order.getAmount() == -1) {
            return WRONG_NUMBER_FORMAT;
        }
        return OK;
    }

    private static class LongTypeAdapter extends TypeAdapter<Long> {
        @Override
        public void write(JsonWriter out, Long value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.value(value);
        }

        @Override
        public Long read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            String stringValue = reader.nextString();
            try {
                return Long.valueOf(stringValue);
            } catch (NumberFormatException e) {
                return -1L;
            }
        }
    }
}
