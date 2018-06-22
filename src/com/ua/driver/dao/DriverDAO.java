package com.ua.driver.dao;

import com.ua.driver.exception.DriverNotFoundException;
import com.ua.driver.exception.DuplicateDriverException;
import com.ua.driver.exception.WrongIdException;
import com.ua.driver.model.Driver;

import java.util.List;

public interface DriverDAO {

    void addDriver(Driver driver) throws DuplicateDriverException;

    List<Driver> getAllDrivers();

    Driver getDriverById(int driverId) throws WrongIdException;

    void updateDriver(Driver driver);

    void deleteDriver(int driverId) throws DriverNotFoundException, WrongIdException;

    Driver findByLastName(String name);

    public void deleteAll();
}
