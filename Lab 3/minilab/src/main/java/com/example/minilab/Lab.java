package com.example.minilab;

import jakarta.persistence.*;

@Entity
public class Lab {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private boolean done;

    public Lab() {}

    public Lab(String title) {
        this.title = title;
        this.done = false;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
}
