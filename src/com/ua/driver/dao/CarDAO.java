package com.ua.driver.dao;

import com.ua.driver.exception.CarNotFoundException;
import com.ua.driver.model.Car;

import java.util.List;

public interface CarDAO {

    void addCar(Car car);

    List<Car> getAllCars();

    Car getCarById(int carId);

    void updateCar(Car car);

    void deleteCar(int carId) throws CarNotFoundException;

    void deleteAll();

}