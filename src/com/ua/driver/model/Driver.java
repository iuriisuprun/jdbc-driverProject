package com.ua.driver.model;

import java.util.List;

public class Driver {

    public static final String ID = "id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EXPERIENCE = "experience";
    public static final String CATEGORY = "category";

    private int id;
    private String firstName;
    private String lastName;
    private int experience;
    private Category category;
    private List<Car> cars;

    public Driver() {
    }

    public Driver(int id) {
        this.id = id;
    }

    public Driver(String firstName, String lastName, int experience, Category category) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.experience = experience;
        this.category = category;
    }

    public Driver(String firstName, String lastName, int experience, Category category, List<Car> cars) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.experience = experience;
        this.category = category;
        this.cars = cars;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", experience=" + experience +
                ", category='" + category + '\'' +
                ", cars=" + cars +
                '}';
    }
}
