package com.ilyaskrypnik.ordersparser.controller;

import com.ilyaskrypnik.ordersparser.service.reader.OrdersFileReader;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OrderParsingController {

    private List<OrdersFileReader> fileReaders;

    public OrderParsingController(List<OrdersFileReader> fileReaders) {
        this.fileReaders = fileReaders;
    }

    public void processFile(String filename) {
        for (OrdersFileReader fileReader : fileReaders) {
            fileReader.read(filename);
        }
    }
}
