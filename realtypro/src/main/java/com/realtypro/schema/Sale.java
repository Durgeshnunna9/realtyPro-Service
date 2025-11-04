package com.realtypro.schema;

import jakarta.persistence.*;


@Entity
@Table(name="sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="manager_id", nullable = false)
    private User manager;

    @OneToOne
    @JoinColumn(name="property_id", nullable = false, unique = true)
    private Property property;

    @Column(name="sale_amount" , nullable = false)
    private int saleAmount;

    @Column(name = "commission",nullable = false)
    private int commission;

    public Sale() {}

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) { this.user = user; }

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
}
