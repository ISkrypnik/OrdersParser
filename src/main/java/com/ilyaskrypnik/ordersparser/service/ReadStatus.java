package com.ilyaskrypnik.ordersparser.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ReadStatus {
    private AtomicInteger amountOfAllFiles = new AtomicInteger(0);
    private AtomicInteger amountOfReadFiles = new AtomicInteger(0);

    public void incrementAmountOfFiles() {
        amountOfAllFiles.incrementAndGet();
    }

    public void incrementAmountOfReadFiles() {
        amountOfReadFiles.incrementAndGet();
    }

    public long getAmountOfAllFiles() {
        return amountOfAllFiles.longValue();
    }

    public long getAmountOfReadFiles() {
        return amountOfReadFiles.longValue();
    }

    public void setAmountOfAllFiles(int amountOfAllFiles) {
        this.amountOfAllFiles = new AtomicInteger(amountOfAllFiles);
    }
}
