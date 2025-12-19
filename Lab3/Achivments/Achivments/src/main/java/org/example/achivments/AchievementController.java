package org.example.achivments;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/achiv")
@CrossOrigin(origins = "*")
public class AchievementController {

    private final AchievementRepository repository;

    public AchievementController(AchievementRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Achievement> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Achievement add(@RequestBody Achievement a) {
        return repository.save(a);
    }
}