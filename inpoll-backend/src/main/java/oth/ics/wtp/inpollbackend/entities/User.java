package oth.ics.wtp.inpollbackend.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue private Long id;
    @Column(unique = true, nullable = false) private String username;
    private String password;
    @OneToMany(mappedBy = "creator")
    private List<Poll> createdPolls =  new ArrayList<>();

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() { return id; }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public List<Poll> getCreatedPolls() { return createdPolls; }

    public void setPassword(String password) { this.password = password; }

}
