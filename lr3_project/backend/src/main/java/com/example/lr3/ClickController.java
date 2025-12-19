package com.example.lr3;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClickController {

    private final ClickRepository repository;

    @GetMapping("/click")
    public int click() {
        ClickEntity entity = repository.findById(1L).orElse(new ClickEntity());
        entity.setCount(entity.getCount() + 1);
        repository.save(entity);
        return entity.getCount();
    }

    @GetMapping("/status")
    public int status() {
        return repository.findById(1L).map(ClickEntity::getCount).orElse(0);
    }
}
