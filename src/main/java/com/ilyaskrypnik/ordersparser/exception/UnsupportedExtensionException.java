package com.ilyaskrypnik.ordersparser.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnsupportedExtensionException extends Exception {
    private final String description;
}
