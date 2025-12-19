package com.example.cookieclicker.controller;

import com.example.cookieclicker.service.CookieService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cookie")
@CrossOrigin(origins = "http://localhost")
public class CookieController {

    private final CookieService service;

    public CookieController(CookieService service) {
        this.service = service;
    }

    @PostMapping("/click")
    public long click() {
        return service.registerClick();
    }

    @GetMapping("/count")
    public long count() {
        return service.getCount();
    }
}
