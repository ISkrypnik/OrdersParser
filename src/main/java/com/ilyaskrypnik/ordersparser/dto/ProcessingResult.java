package com.ilyaskrypnik.ordersparser.dto;

import lombok.Getter;

@Getter
public enum ProcessingResult {

    OK("OK"),
    WRONG_NUMBER("Wrong id or amount format. It should be number!"),
    WRONG_COLUMN_AMOUNT("Wrong column amount in input file. It should be 4!");

    private String description;

    ProcessingResult(String description) {
        this.description = description;
    }
}
