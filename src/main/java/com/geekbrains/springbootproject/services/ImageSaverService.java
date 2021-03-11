package com.geekbrains.springbootproject.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageSaverService {
    private static final String UPLOADED_FOLDER = "./resources/static/images/";

    private final Logger logger = LoggerFactory.getLogger(ImageSaverService.class);

    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "";
        }
        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();
        try {
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            file.transferTo(path);
        } catch (IOException e) {
            logger.error("Ошибка при загрузке картинки: {}", e.getMessage());
        }
        return fileName;
    }
}
