package com.example.linkshortener.controller;

import com.example.linkshortener.dto.CreateLinkRequest;
import com.example.linkshortener.dto.LinkResponse;
import com.example.linkshortener.model.Link;
import com.example.linkshortener.service.LinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;


    @PostMapping("/api/links")
    public ResponseEntity<LinkResponse> createLink(
            @Valid @RequestBody CreateLinkRequest request,
            HttpServletRequest httpRequest) {
        
        Link link = linkService.createShortLink(request.getUrl());
        String baseUrl = getBaseUrl(httpRequest);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(LinkResponse.fromEntity(link, baseUrl));
    }


    @GetMapping("/api/links")
    public ResponseEntity<List<LinkResponse>> getAllLinks(HttpServletRequest httpRequest) {
        String baseUrl = getBaseUrl(httpRequest);
        List<LinkResponse> links = linkService.getAllLinks().stream()
                .map(link -> LinkResponse.fromEntity(link, baseUrl))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(links);
    }


    @GetMapping("/api/links/{code}")
    public ResponseEntity<LinkResponse> getLinkInfo(
            @PathVariable String code,
            HttpServletRequest httpRequest) {
        
        return linkService.getByCode(code)
                .map(link -> ResponseEntity.ok(LinkResponse.fromEntity(link, getBaseUrl(httpRequest))))
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/api/links/{code}")
    public ResponseEntity<Void> deleteLink(@PathVariable String code) {
        if (linkService.deleteByCode(code)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        return linkService.getAndTrackClick(code)
                .map(link -> ResponseEntity
                        .status(HttpStatus.FOUND)
                        .location(URI.create(link.getOriginalUrl()))
                        .<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }

    private String getBaseUrl(HttpServletRequest request) {

        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        String forwardedHost = request.getHeader("X-Forwarded-Host");
        
        if (forwardedProto != null && forwardedHost != null) {
            return forwardedProto + "://" + forwardedHost;
        }
        

        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        
        if ((scheme.equals("http") && serverPort != 80) ||
            (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        
        return url.toString();
    }
}
