package com.ilyaskrypnik.ordersparser;

import com.ilyaskrypnik.ordersparser.controller.OrderParsingController;
import com.ilyaskrypnik.ordersparser.service.Converter;
import com.ilyaskrypnik.ordersparser.service.ReadStatus;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@EnableAsync
@SpringBootApplication
public class OrdersParserApplication implements ApplicationRunner {

    private final Converter converter;
    private final OrderParsingController orderParsingController;
    private final ReadStatus readStatus;

    public OrdersParserApplication(Converter converter, OrderParsingController orderParsingController,
                                   ReadStatus readStatus) {
        this.converter = converter;
        this.orderParsingController = orderParsingController;
        this.readStatus = readStatus;
    }

    public static void main(String[] args) {
        SpringApplication.run(OrdersParserApplication.class, args).close();
    }

    @Override
    public void run(ApplicationArguments args) {
        converter.processParsedOrders();
        List<String> arguments = args.getNonOptionArgs();
		readStatus.setAmountOfAllFiles(arguments.size());
        for (String filename : arguments) {
            orderParsingController.processFile(filename);
        }
    }
}
