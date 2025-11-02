package com.realtypro.dto;

public class PerformanceDTO {
    private String month;
    private int deals;
    private double revenue;
    private double commission;

    // --- Constructors ---
    public PerformanceDTO() {}
    public PerformanceDTO(String month, int deals, double revenue, double commission) {
        this.month = month;
        this.deals = deals;
        this.revenue = revenue;
        this.commission = commission;
    }

    // --- Getters and Setters ---
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public int getDeals() { return deals; }
    public void setDeals(int deals) { this.deals = deals; }

    public double getRevenue() { return revenue; }
    public void setRevenue(double revenue) { this.revenue = revenue; }

    public double getCommission() { return commission; }
    public void setCommission(double commission) { this.commission = commission; }
}
