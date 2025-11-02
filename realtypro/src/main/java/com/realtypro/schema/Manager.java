package com.realtypro.schema;

import jakarta.persistence.*;

@Entity
@Table(name="managers")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Manager() {}

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
