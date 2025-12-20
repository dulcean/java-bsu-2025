package com.example.linkshortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class CreateLinkRequest {
    
    @NotBlank(message = "URL не может быть пустым")
    @URL(message = "Некорректный формат URL")
    private String url;
}
