package com.realtypro.dto;

import com.realtypro.utilities.Role;

public class UserDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Integer age;
    private String mobileNumber;
    private Float rating; // ✅ Added properly

    // ✅ Default constructor
    public UserDTO() {}

    // ✅ Constructor with all fields (including rating)
    public UserDTO(Long userId, String firstName, String lastName, String email,
                   Role role, Integer age, String mobileNumber, Float rating) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.age = age;
        this.mobileNumber = mobileNumber;
        this.rating = rating;
    }

    // ✅ Constructor without rating
    public UserDTO(Long userId, String firstName, String lastName, String email,
                   Role role, Integer age, String mobileNumber) {
        this(userId, firstName, lastName, email, role, age, mobileNumber, null);
    }

    // ✅ Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public Float getRating() { return rating; }
    public void setRating(Float rating) { this.rating = rating; }
}
