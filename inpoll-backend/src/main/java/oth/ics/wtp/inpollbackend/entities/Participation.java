package oth.ics.wtp.inpollbackend.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Participation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User participant;

    @ManyToOne
    private Poll poll;

    @OneToMany(mappedBy = "participation", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Participation() {}

    public Participation(User participant, Poll poll) {
        this.participant = participant;
        this.poll = poll;
    }

    public Long getId() { return id; }

    public User getParticipant() { return participant; }

    public Poll getPoll() { return poll; }

    public List<Answer> getAnswers() { return answers; }

    public void setPoll(Poll poll) { this.poll = poll; }

    public void setParticipant(User participant) { this.participant = participant; }

}
