package com.ua.driver.dao.impl;

import com.ua.driver.dao.CarDAO;
import com.ua.driver.exception.CarNotFoundException;
import com.ua.driver.model.Car;
import com.ua.driver.model.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.ua.driver.dao.impl.ConnectionFactory.getInstance;

public class CarDAOMySQLImpl implements CarDAO{

    private static final String CREATE_DRIVER_TABLE = "CREATE TABLE IF NOT EXISTS drivers (" +
            com.ua.driver.model.Driver.ID + " INT(11) PRIMARY KEY AUTO_INCREMENT," +
            com.ua.driver.model.Driver.FIRST_NAME + " VARCHAR(255)," +
            com.ua.driver.model.Driver.LAST_NAME + " VARCHAR(255) UNIQUE," +
            com.ua.driver.model.Driver.EXPERIENCE + " VARCHAR(255)," +
            com.ua.driver.model.Driver.CATEGORY + " VARCHAR(3)," +
            ");";

    private static final String CREATE_CAR_TABLE = "CREATE TABLE IF NOT EXISTS cars (" +
            Car.ID + " INT(11) PRIMARY KEY AUTO_INCREMENT," +
            Car.MAX_SPEED + " DOUBLE(11)," +
            Car.MODEL + " VARCHAR(255)," +
            Car.YEAR + " INT(4)," +
            "driver_id INT(11) REFERENCES drivers(id)" +
            ");";

    private static final String INSERT_CAR = String.format("INSERT INTO cars(%s, %s, %s, %s)" +
            " VALUES (?, ?, ?, ?);", Car.MAX_SPEED, Car.MODEL, Car.YEAR, Car.DRIVER_ID);

    private static final String GET_ALL_CARS = "SELECT * FROM cars";

    private static final String CLEAN_ALL_CARS = "TRUNCATE TABLE cars";

    private static final String DELETE_CAR_BY_ID = String.format("DELETE FROM cars WHERE %s = ?;", Car.ID);

    private static final String GET_CAR = "SELECT * FROM cars WHERE id = ?";

    private Connection connection;
    private PreparedStatement pst = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public CarDAOMySQLImpl() {
        createTableIfNotExists();
    }

    @Override
    public void addCar(Car car) {

        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(INSERT_CAR);
            pst.setDouble(1, car.getMaxSpeed());
            pst.setString(2, car.getModel());
            pst.setInt(3, car.getYear());
            pst.setInt(4, car.getDriver().getId());
            pst.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
    }

    @Override
    public List<Car> getAllCars() {
        List<Car> result = new ArrayList<>();
        try {
            connection = getInstance().getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(GET_ALL_CARS);

            while (rs.next()) {
                Car car = new Car();
                car.setId(rs.getInt(Car.ID));
                car.setMaxSpeed(rs.getDouble(Car.MAX_SPEED));
                car.setModel(rs.getString(Car.MODEL));
                car.setYear(rs.getInt(Car.YEAR));

                result.add(car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeResultSet(rs);
            getInstance().closeStatement(stmt);
            getInstance().closeConnection(connection);
        }
        return result;
    }

    @Override
    public Car getCarById(int carId) {
        //incorrect
        Car car = new Car();
        try {

            connection = getInstance().getConnection();
            pst = connection.prepareStatement(GET_CAR);
//            stmt = connection.createStatement();
            pst.setInt(1, carId);
            rs = pst.executeQuery();
            while (rs.next()) {

                car.setMaxSpeed(rs.getDouble(Car.MAX_SPEED));
                car.setModel(rs.getString(Car.MODEL));
                car.setYear(rs.getInt(Car.YEAR));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeResultSet(rs);
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
        return car;
    }

    @Override
    public void updateCar(Car car) {

    }

    @Override
    public void deleteCar(int carId) throws CarNotFoundException {
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(DELETE_CAR_BY_ID);
            pst.setInt(1, carId);
            int result = pst.executeUpdate();
            if (result == 0) {
                throw new CarNotFoundException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeConnection(connection);
            getInstance().closePreparedStatement(pst);
        }
    }

    @Override
    public void deleteAll() {
        try {
            connection = getInstance().getConnection();
            stmt = connection.createStatement();
            stmt.executeUpdate(CLEAN_ALL_CARS);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeStatement(stmt);
            getInstance().closeConnection(connection);
        }
    }

    private void createTableIfNotExists() {
        try {
            connection = getInstance().getConnection();
            stmt = connection.createStatement();
            stmt.executeUpdate(CREATE_DRIVER_TABLE);
            stmt.executeUpdate(CREATE_CAR_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeStatement(stmt);
            getInstance().closeConnection(connection);
        }
    }
}
