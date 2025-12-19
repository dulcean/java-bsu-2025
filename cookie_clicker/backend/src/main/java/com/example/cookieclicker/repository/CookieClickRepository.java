package com.example.cookieclicker.repository;

import com.example.cookieclicker.model.CookieClick;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookieClickRepository extends JpaRepository<CookieClick, Long> {
}
