package com.example.haruapp.global.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class EmailTemplateLoader {

    public String load(String templateName, Map<String, Object> variables) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/" + templateName);
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
            }

            return content;
        } catch (IOException e) {
            throw new RuntimeException("이메일 템플릿 로딩 실패: " + templateName, e);
        }
    }

}
