package com.ua.driver;

import com.ua.driver.controller.DriverController;
import com.ua.driver.dao.DriverDAO;
import com.ua.driver.dao.impl.CarDAOH2Impl;
import com.ua.driver.dao.impl.CarDAOMySQLImpl;
import com.ua.driver.dao.impl.DriverDAOH2Impl;
import com.ua.driver.dao.impl.DriverDAOMySQLImpl;
import com.ua.driver.exception.DriverNotFoundException;
import com.ua.driver.exception.DuplicateDriverException;
import com.ua.driver.exception.WrongIdException;
import com.ua.driver.model.Car;
import com.ua.driver.model.Category;
import com.ua.driver.model.Driver;

import java.util.Arrays;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        CarDAOH2Impl carDAOH2 = new CarDAOH2Impl();
        DriverDAOH2Impl driverDAOH2 = new DriverDAOH2Impl();

        /*driverDAOH2.addDriver(new Driver("Sasha", "Kom", 15, Category.B));

        carDAOH2.addCar(new Car("Audi", 1998, 200));*/

        Driver d1 = new Driver("FN", UUID.randomUUID().toString(), 3, Category.C,
                Arrays.asList(new Car("Tesla", 2018, 278),
                        new Car("Jaguar", 2005, 189)));

        driverDAOH2.addDriver(d1);


        for (Driver driver : driverDAOH2.getAllDrivers()) {
            System.out.println(driver);
        }

        /*DriverDAO driverDAO = new DriverDAOMySQLImpl();

        try {
            driverDAO.addDriver(new Driver("Jack", "Wall", 5, Category.D));
        } catch (DuplicateDriverException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(driverDAO.getDriverById(7));
        } catch (WrongIdException e) {
            e.printStackTrace();
        }

        try {
            driverDAO.deleteDriver(2);
        } catch (DriverNotFoundException e) {
            e.printStackTrace();
        } catch (WrongIdException e) {
            e.printStackTrace();
        }

        Driver driver = new Driver("Bruce", "Lee", 11, Category.BE);
        driverDAO.updateDriver(driver);
        System.out.println(driverDAO.getAllDrivers());*/

        /*DriverController driverController = new DriverController();
        driverController.doWork();*/
    }
}
