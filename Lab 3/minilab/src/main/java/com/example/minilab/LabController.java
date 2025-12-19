package com.example.minilab;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/labs")
public class LabController {

    private final LabService service;

    public LabController(LabService service) {
        this.service = service;
    }

    @GetMapping
    public List<Lab> all() {
        return service.getAll();
    }

    @PostMapping
    public Lab add(@RequestParam String title) {
        return service.add(title);
    }

    @PostMapping("/{id}/done")
    public Lab done(@PathVariable Long id) {
        return service.done(id);
    }
}
