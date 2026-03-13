package top.kagg886.backend.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.kagg886.backend.config.UploadConfig;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {
    private final UploadConfig uploadConfig;
    public UploadController(UploadConfig uploadConfig) {
        this.uploadConfig = uploadConfig;
    }

    @PutMapping
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString().replace("-","");
        String ext = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        File file1 = new File(uploadConfig.getRoot(),uuid + "." + ext);

        try(OutputStream stream = Files.newOutputStream(file1.toPath())) {
            stream.write(file.getBytes());
        }

        return "/api/upload/" + uuid + "." + ext;
    }
}
