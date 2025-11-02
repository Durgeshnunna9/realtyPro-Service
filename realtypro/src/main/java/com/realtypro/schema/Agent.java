package com.realtypro.schema;

import jakarta.persistence.*;

@Entity
@Table(name="agents")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agentId;

    @OneToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name = "rating", nullable = false)
    private int rating;

    public Agent() {}

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
