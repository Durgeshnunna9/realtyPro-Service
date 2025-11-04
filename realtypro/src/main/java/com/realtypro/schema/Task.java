package com.realtypro.schema;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @ManyToOne // Multiple tasks assigned to each agent
    @JoinColumn(name="agent_id", nullable = false)
    private User agent;

    @ManyToOne // A manager assigns many tasks; each task belongs to one manager.
    @JoinColumn(name="manager_id", nullable = false)
    private User manager;

    @ManyToOne
    @JoinColumn(name="property_id", nullable = false)
    private Property property;

    @Column(name="task_name", nullable = false)
    private String taskName;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="status", nullable = false)
    private String status;

    @Column(name="assigned_date", nullable = false)
    private LocalDate assignedDate;

    // Constructors
    public Task() {}

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }
}
