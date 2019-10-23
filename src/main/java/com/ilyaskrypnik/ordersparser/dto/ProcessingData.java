package com.ilyaskrypnik.ordersparser.dto;

import lombok.Data;

/**
 * Класс-хранилище информации о процессинге Order.
 * Хранится результат процессинга, имя файла и номер строки в этом файле, с которой был прочитан Order.
 */
@Data
public class ProcessingData {
    private String result;
    private String fileName;
    private long lineNumber;

    /**
     * Конструктор
     * @param result результат процессинга,
     * @param fileName имя файла, из которого был прочитан Order,
     * @param lineNumber номер строки из файла, с которой был прочитан Order.
     */
    public ProcessingData(String result, String fileName, long lineNumber) {
        this.result = result;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }
}
