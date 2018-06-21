package com.ua.driver.controller;

import com.ua.driver.exception.DriverNotFoundException;
import com.ua.driver.exception.DuplicateDriverException;
import com.ua.driver.exception.WrongIdException;
import com.ua.driver.model.Category;
import com.ua.driver.model.Driver;
import com.ua.driver.service.DriverService;

import java.util.Scanner;

public class DriverController {

    private DriverService driverService = new DriverService();

    Scanner scanner = new Scanner(System.in);

    public void doWork() {
        showHelloMessage();
        while (true) {
            showMenu();
            makeChoice();
        }
    }

    private void showMenu() {
        System.out.println("Make your choice: ");
        System.out.println("1. Add driver.");
        System.out.println("2. Show list of drivers.");
        System.out.println("3. Get driver by id.");
        System.out.println("4. Update driver.");
        System.out.println("5. Delete driver by id.");
        System.out.println("\n0. Exit");
    }

    private void makeChoice() {
        int choice = scanner.nextInt();
        switch (choice) {
            case 1: {
                addDriver();
                break;
            }
            case 2: {
                getAllDrivers();
                break;
            }
            case 3: {
                getDriverById();
                break;
            }
            case 4: {
                updateDriver();
                break;
            }
            case 5: {
                deleteDriver();
                break;
            }
            case 0: {
                System.out.println("Goodbye!!!");
                System.exit(1);
            }
            default: {
                System.out.println("Wrong choice!");
            }
        }
    }

    private void addDriver() {
        Driver driver = new Driver();
        System.out.println("\nEnter data of new driver, please:");
        System.out.println("Enter first name: ");
        String firstName = scanner.next();
        System.out.println("Enter last name: ");
        String lastName = scanner.next();
        System.out.println("Enter experience: ");
        int experience = scanner.nextInt();
        System.out.println("Enter category: ");
        String category = scanner.next();

        driver.setFirstName(firstName);
        driver.setLastName(lastName);
        driver.setExperience(experience);
        driver.setCategory(Category.valueOf(category));

        try {
            driverService.addDriver(driver);
        } catch (DuplicateDriverException e) {
            System.err.println("Duplicate driver!");
        }
    }

    private void getAllDrivers() {
        driverService.printDrivers(driverService.getAllDrivers());
    }

    private void getDriverById() {
        System.out.println("Enter id of a driver, please:");
        int id = scanner.nextInt();
        try {
            System.out.println(driverService.getDriverById(id));
        } catch (WrongIdException e) {
            e.printStackTrace();
        }
    }

    private void updateDriver() {
        getAllDrivers();
        System.out.println("Enter id of a driver for updating:");
        int id = scanner.nextInt();
        try {
            for (Driver driver : driverService.getAllDrivers()) {
                if (driver.getId() == id){
                    System.out.println("Enter new first name:");
                    String firstName = scanner.next();
                    System.out.println("Enter new last name:");
                    String lastName = scanner.next();
                    System.out.println("Enter new experience:");
                    int experience = scanner.nextInt();
                    System.out.println("Enter new category:");
                    String category = scanner.next();
                    driver.setFirstName(firstName);
                    driver.setLastName(lastName);
                    driver.setExperience(experience);
                    driver.setCategory(Category.valueOf(category));
                    driverService.updateDriver(driverService.getDriverById(id));
                }
            }
        } catch (WrongIdException e) {
            System.err.println("Wrong id!");
        }
    }

    private void deleteDriver() {
        System.out.println("Enter id of a driver for deleting:");
        int id = scanner.nextInt();
        try {
            driverService.deleteDriver(id);
        } catch (DriverNotFoundException e) {
            System.err.println("Wrong id!");
        }
    }

    private void showHelloMessage() {
        System.out.println("*********************");
        System.out.println("**    Driver DB    **");
        System.out.println("*********************");
        System.out.println();
    }
}
