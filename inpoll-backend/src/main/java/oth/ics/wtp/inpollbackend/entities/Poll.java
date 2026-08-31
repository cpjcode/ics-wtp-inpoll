package oth.ics.wtp.inpollbackend.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Poll {
    @Id @GeneratedValue private Long id;
    @Column(unique = true, nullable = false) private String title;
    private String description;
    private Instant dueDate;
    private PollStatus status = PollStatus.ACTIVE;
    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations = new ArrayList<>();

    @ManyToOne private User creator;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @ManyToMany private List<User> invitedUsers = new ArrayList<>();

    public Poll() {}

    public Poll(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public Instant getDueDate() { return dueDate; }

    public PollStatus getStatus() { return status; }

    public User getCreator() { return creator; }

    public List<User> getInvitedUsers() { return invitedUsers; }

    public List<Question> getQuestions() { return questions; }

    public void setTitle(String title) { this.title = title; }

    public void setDescription(String description) { this.description = description; }

    public void setDueDate(Instant dueDate) { this.dueDate = dueDate; }

    public void setStatus(PollStatus status) { this.status = status; }

    public void setCreator(User creator) { this.creator = creator; }

    public void addInvitedUser(User user) { this.invitedUsers.add(user); }

    public void addQuestion(Question question) { this.questions.add(question); }

}