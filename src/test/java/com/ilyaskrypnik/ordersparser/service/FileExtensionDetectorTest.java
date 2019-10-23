package com.ilyaskrypnik.ordersparser.service;

import com.ilyaskrypnik.ordersparser.domain.SupportedFileExtensions;
import com.ilyaskrypnik.ordersparser.exception.UnsupportedExtensionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class FileExtensionDetectorTest {

    private static final Logger log = LoggerFactory.getLogger(FileExtensionDetectorTest.class);

    private static final String CSV_FILE_NAME = "file.csv";
    private static final String JSON_FILE_NAME = "file.json";
    private static final String XLS_FILE_NAME = "file.xls";

    private static FileExtensionDetector extensionDetector;

    @BeforeAll
    static void setUp() {
        extensionDetector = new FileExtensionDetector();
    }

    @Test
    void testCsvExtension() {
        try {
            assertEquals(SupportedFileExtensions.CSV, extensionDetector.getFileExtension(CSV_FILE_NAME));
        } catch (UnsupportedExtensionException e) {
            log.error("CSV extension test error.", e);
        }
    }

    @Test
    void testJsonExtension() {
        try {
            assertEquals(SupportedFileExtensions.JSON, extensionDetector.getFileExtension(JSON_FILE_NAME));
        } catch (UnsupportedExtensionException e) {
            log.error("JSON extension test error.", e);
        }
    }

    @Test
    void testXlsExtension() {
        assertThrows(UnsupportedExtensionException.class, () -> extensionDetector.getFileExtension(XLS_FILE_NAME));
    }
}