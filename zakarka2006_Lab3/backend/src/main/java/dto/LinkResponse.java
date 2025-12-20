package com.example.linkshortener.dto;

import com.example.linkshortener.model.Link;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LinkResponse {
    private Long id;
    private String code;
    private String shortUrl;
    private String originalUrl;
    private LocalDateTime createdAt;
    private Long clickCount;

    public static LinkResponse fromEntity(Link link, String baseUrl) {
        LinkResponse response = new LinkResponse();
        response.setId(link.getId());
        response.setCode(link.getCode());
        response.setShortUrl(baseUrl + "/" + link.getCode());
        response.setOriginalUrl(link.getOriginalUrl());
        response.setCreatedAt(link.getCreatedAt());
        response.setClickCount(link.getClickCount());
        return response;
    }
}
