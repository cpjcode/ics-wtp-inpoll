package oth.ics.wtp.inpollbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Answer {
    @Id
    @GeneratedValue
    private Long id;
    private String text;

    @ManyToOne
    private Participation participation;

    @ManyToOne
    private Question question;

    public Answer() {}

    public Long getId() { return id; }

    public String getText() { return text; }

    public Question getQuestion() { return question; }

    public void setText(String text) { this.text = text; }

    public void setParticipation(Participation participation) { this.participation = participation; }

    public void setQuestion(Question question) { this.question = question; }

}
