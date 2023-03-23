package com.nagarro.af.bookingtablesystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @Column(name = "date_hour", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime dateHour;
    @Column(name = "customers_no", nullable = false)
    private int customersNo;
    @Column(name = "tables_no", nullable = false)
    private int tablesNo;

    public Booking() {
    }

    public Booking(Customer customer, Restaurant restaurant, LocalDateTime dateHour, int customersNo, int tablesNo) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.dateHour = dateHour;
        this.customersNo = customersNo;
        this.tablesNo = tablesNo;
        Date bookingDate = getBookingDate();
        restaurant.getDateCapacityAvailability().putIfAbsent(
                bookingDate,
                new RestaurantCapacity(UUID.randomUUID(), restaurant.getMaxTablesNo(), restaurant.getMaxCustomersNo())
        );
    }

    public Date getBookingDate() {
        int day = this.getDateHour().getDayOfMonth();
        int month = this.getDateHour().getMonth().getValue();
        int year = this.getDateHour().getYear();
        LocalDate date = LocalDate.of(year, month, day);
        return Date.valueOf(date);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDateTime getDateHour() {
        return dateHour;
    }

    public void setDateHour(LocalDateTime dateHour) {
        this.dateHour = dateHour;
    }

    public int getCustomersNo() {
        return customersNo;
    }

    public void setCustomersNo(int customersNo) {
        this.customersNo = customersNo;
    }

    public int getTablesNo() {
        return tablesNo;
    }

    public void setTablesNo(int tablesNo) {
        this.tablesNo = tablesNo;
    }
}
