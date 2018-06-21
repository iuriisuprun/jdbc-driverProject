package com.ua.driver.service;

import com.ua.driver.dao.DriverDAO;
import com.ua.driver.dao.impl.DriverDAOMySQLImpl;
import com.ua.driver.exception.DriverNotFoundException;
import com.ua.driver.exception.DuplicateDriverException;
import com.ua.driver.exception.WrongIdException;
import com.ua.driver.model.Driver;

import java.util.List;

public class DriverService {

    private DriverDAO driverDAO = new DriverDAOMySQLImpl();

    public void addDriver(Driver driver) throws DuplicateDriverException {
        driverDAO.addDriver(driver);
    }

    public List<Driver> getAllDrivers() {
        return driverDAO.getAllDrivers();
    }

    public void printDrivers(List<Driver> drivers) {
        int i = 1;
        for (Driver driver : drivers) {
            System.out.println(i++ + "\t" + driver);
        }
    }

    public Driver getDriverById(int driverId) throws WrongIdException {
        return driverDAO.getDriverById(driverId);
    }

    public void updateDriver(Driver driver) {
        driverDAO.updateDriver(driver);
    }

    public void deleteDriver(int driverId) throws DriverNotFoundException {
        driverDAO.deleteDriver(driverId);
    }
}
