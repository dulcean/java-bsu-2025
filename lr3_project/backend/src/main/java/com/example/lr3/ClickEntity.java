package com.example.lr3;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ClickEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int count;
}
