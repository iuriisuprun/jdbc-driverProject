package com.ua.driver.dao;

import com.ua.driver.controller.DriverController;
import com.ua.driver.dao.impl.DriverDAOMySQLImpl;
import com.ua.driver.exception.DriverNotFoundException;
import com.ua.driver.exception.DuplicateDriverException;
import com.ua.driver.exception.WrongIdException;
import com.ua.driver.model.Category;
import com.ua.driver.model.Driver;

public class Main {

    public static void main(String[] args) {

        DriverDAO driverDAO = new DriverDAOMySQLImpl();

        /*try {
            driverDAO.addDriver(new Driver("Jack", "Wall", 5, Category.D));
        } catch (DuplicateDriverException e) {
            e.printStackTrace();
        }*/

        /*
        try {
            System.out.println(driverDAO.getDriverById(7));
        } catch (WrongIdException e) {
            e.printStackTrace();
        }*/

        /*try {
            driverDAO.deleteDriver(2);
        } catch (DriverNotFoundException e) {
            e.printStackTrace();
        }*/

        /*Driver driver = new Driver("Bruce", "Lee", 11, Category.BE);
        driverDAO.updateDriver(driver);
        System.out.println(driverDAO.getAllDrivers());
        */


        DriverController driverController = new DriverController();
        driverController.doWork();
    }
}
