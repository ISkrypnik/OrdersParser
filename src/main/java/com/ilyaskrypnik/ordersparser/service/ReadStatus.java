package com.ilyaskrypnik.ordersparser.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс-хранилище статуса чтения файлов.
 * Хранит в себе общее количество файлов и количество прочитанных файлов.
 */
@Component
public class ReadStatus {
    private AtomicInteger amountOfAllFiles = new AtomicInteger(0);
    private AtomicInteger amountOfReadFiles = new AtomicInteger(0);

    public void incrementAmountOfReadFiles() {
        amountOfReadFiles.incrementAndGet();
    }

    long getAmountOfAllFiles() {
        return amountOfAllFiles.longValue();
    }

    long getAmountOfReadFiles() {
        return amountOfReadFiles.longValue();
    }

    public void setAmountOfAllFiles(int amountOfAllFiles) {
        this.amountOfAllFiles = new AtomicInteger(amountOfAllFiles);
    }
}
