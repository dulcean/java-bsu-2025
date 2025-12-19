package com.example.cookieclicker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cookie_clicks")
public class CookieClick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime clickedAt;

    public CookieClick() {
        this.clickedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getClickedAt() {
        return clickedAt;
    }
}
