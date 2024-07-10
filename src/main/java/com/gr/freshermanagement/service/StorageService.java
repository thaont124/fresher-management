package com.gr.freshermanagement.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    Path load(String filename);

    void store(MultipartFile file);

    void deleteAll();
    String formatFileURL(MultipartFile file);

    Path getUniqueDestinationFile(Path destinationFile);

    boolean isImage(MultipartFile file);

    String getPhotoURL(String fileName);


    Resource loadAsResource(String filename);
}