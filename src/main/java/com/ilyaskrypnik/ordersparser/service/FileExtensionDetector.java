package com.ilyaskrypnik.ordersparser.service;

import com.ilyaskrypnik.ordersparser.domain.SupportedFileExtensions;
import com.ilyaskrypnik.ordersparser.exception.UnsupportedExtensionException;
import org.springframework.stereotype.Service;

@Service
public class FileExtensionDetector {

    /**
     * Метод определяет расширение класса.
     * @param fileName Имя файла
     * @return возвращает объект SupportedFileExtension, если такое расширение файла поддерживается
     * @throws UnsupportedExtensionException выкидывает исключение, если такого расширения нет в SupportedFileExtension
     */
    public SupportedFileExtensions getFileExtension(String fileName) throws UnsupportedExtensionException {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();

        for (SupportedFileExtensions extension : SupportedFileExtensions.values()) {
            if (fileExtension.equals(extension.name())) {
                return extension;
            }
        }

        throw new UnsupportedExtensionException("Unsupported file extension " + fileExtension);
    }
}
