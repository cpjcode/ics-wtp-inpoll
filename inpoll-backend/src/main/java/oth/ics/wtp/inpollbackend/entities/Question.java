package oth.ics.wtp.inpollbackend.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {
    @Id @GeneratedValue private Long id;
    private String question;
    private QuestionType type;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();
    @ManyToOne private Poll poll;

    public Question() {}

    public Question(String question, QuestionType type, Poll poll) {
        this.question = question;
        this.type = type;
        this.poll = poll;
    }

    public Long getId() { return id; }

    public String getQuestion() { return question; }

    public QuestionType getType() { return type; }

    public Poll getPoll() { return poll; }

    public void setQuestion(String question) { this.question = question; }

    public void setType(QuestionType type) { this.type = type; }

}
