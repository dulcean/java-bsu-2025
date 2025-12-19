package com.example.cookieclicker.service;

import com.example.cookieclicker.model.CookieClick;
import com.example.cookieclicker.repository.CookieClickRepository;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    private final CookieClickRepository repository;

    public CookieService(CookieClickRepository repository) {
        this.repository = repository;
    }

    public long registerClick() {
        repository.save(new CookieClick());
        return repository.count();
    }

    public long getCount() {
        return repository.count();
    }
}
