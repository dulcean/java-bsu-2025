package com.example.minilab;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LabService {

    private final LabRepository repo;

    public LabService(LabRepository repo) {
        this.repo = repo;
    }

    public List<Lab> getAll() {
        return repo.findAll();
    }

    public Lab add(String title) {
        return repo.save(new Lab(title));
    }

    public Lab done(Long id) {
        Lab lab = repo.findById(id).orElseThrow();
        lab.setDone(true);
        return repo.save(lab);
    }
}
