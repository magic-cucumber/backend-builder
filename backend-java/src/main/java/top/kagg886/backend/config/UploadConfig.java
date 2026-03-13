package top.kagg886.backend.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Slf4j
@Configuration
@Data
@ConfigurationProperties("upload")
public class UploadConfig implements WebMvcConfigurer {
    private String root;

    @PostConstruct
    public void init() {
        File file = new File(root);
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("上传目录创建失败");
        }
        log.info("上传目录ROOT: {}",file.getAbsolutePath());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + root + "/");
    }
}
