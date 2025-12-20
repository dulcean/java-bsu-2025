package com.roulette.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "question")
    private String question;

    @Column(name = "option_a")
    @JsonProperty("optionA")
    private String optionA;

    @Column(name = "option_b")
    @JsonProperty("optionB")
    private String optionB;

    @Column(name = "option_c")
    @JsonProperty("optionC")
    private String optionC;

    @Column(name = "option_d")
    @JsonProperty("optionD")
    private String optionD;

    @Column(name = "correct_answer")
    @JsonProperty("correctAnswer")
    private String correctAnswer;

    @Column(name = "is_learned")
@JsonProperty("isLearned") 
private boolean isLearned; 

public boolean isLearned() {
    return isLearned;
}
    
    @Column(name = "teacher_name")
    @JsonProperty("teacherName")
    private String teacherName; 
}