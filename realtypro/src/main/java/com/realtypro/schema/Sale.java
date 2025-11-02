package com.realtypro.schema;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name="sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;

    @ManyToOne
    @JoinColumn(name="agent_id", nullable = false)
    private Agent agent;

    @ManyToOne
    @JoinColumn(name="manager_id", nullable = false)
    private Manager manager;

    @OneToOne
    @JoinColumn(name="property_id", nullable = false, unique = true)
    private Property property;

    @Column(name="sale_amount" , nullable = false)
    private int saleAmount;

    @Column(name = "commission",nullable = false)
    private int commission;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    public Sale() {}

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public int getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(int saleAmount) {
        this.saleAmount = saleAmount;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }
}
