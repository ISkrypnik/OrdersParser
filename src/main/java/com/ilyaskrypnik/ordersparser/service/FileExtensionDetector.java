package com.ilyaskrypnik.ordersparser.service;

import com.ilyaskrypnik.ordersparser.domain.SupportedFileExtensions;
import com.ilyaskrypnik.ordersparser.exception.UnsupportedExtensionException;
import org.springframework.stereotype.Service;

import static com.ilyaskrypnik.ordersparser.domain.SupportedFileExtensions.CSV;
import static com.ilyaskrypnik.ordersparser.domain.SupportedFileExtensions.JSON;

@Service
public class FileExtensionDetector {

    public SupportedFileExtensions getFileExtension(String fileName) throws UnsupportedExtensionException {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        if (!isExtensionSupported(fileExtension)) {
            throw new UnsupportedExtensionException("Unsupported file extension " + fileExtension);
        }

        if (fileExtension.equals(CSV.name())) {
            return CSV;
        } else {
            return JSON;
        }
    }

    private boolean isExtensionSupported(String fileExtension) {
        for (SupportedFileExtensions extension : SupportedFileExtensions.values()) {
            if (fileExtension.equals(extension.name())) {
                return true;
            }
        }
        return false;
    }
}
