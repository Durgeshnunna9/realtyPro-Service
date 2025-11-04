package com.realtypro.dto;

public class UserPerformanceResponse {

    private Long userId;
    private String fullName;
    private String role;

    // --- For Agents ---
    private long listings;
    private long pendingTasks;
    private double totalSales;

    // --- For Managers ---
    private long managedProperties;
    private long totalAgents; // optional (if you plan to add agent count)
    private long managerPendingTasks; // optional alias for clarity

    // --- Constructors ---
    public UserPerformanceResponse() {}

    // --- Getters and Setters ---
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getListings() {
        return listings;
    }

    public void setListings(long listings) {
        this.listings = listings;
    }

    public long getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(long pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public long getManagedProperties() {
        return managedProperties;
    }

    public void setManagedProperties(long managedProperties) {
        this.managedProperties = managedProperties;
    }

    public long getTotalAgents() {
        return totalAgents;
    }

    public void setTotalAgents(long totalAgents) {
        this.totalAgents = totalAgents;
    }

    public long getManagerPendingTasks() {
        return managerPendingTasks;
    }

    public void setManagerPendingTasks(long managerPendingTasks) {
        this.managerPendingTasks = managerPendingTasks;
    }
}
