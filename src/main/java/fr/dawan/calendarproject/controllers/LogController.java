package fr.dawan.calendarproject.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Value("${app.storagefolder}")
    private String storageFolder;

    public String getStorageFolder() {
        return storageFolder;
    }

    public void setStorageFolder(String storageFolder) {
        this.storageFolder = storageFolder;
    }

    @GetMapping(produces = "application/octet-stream")
    public ResponseEntity<Resource> getLogFile() throws IOException {
        File file = new File(storageFolder + "/app.log");

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=app.log");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok().headers(header).contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
    }

}