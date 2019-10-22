package com.ilyaskrypnik.ordersparser.dto;

import lombok.Data;

@Data
public class ProcessingData {
    private String result;
    private String fileName;
    private long lineNumber;

    public ProcessingData(String result, String fileName, long lineNumber) {
        this.result = result;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }
}
