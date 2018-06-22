package com.ua.driver.dao.impl;

import com.ua.driver.dao.DriverDAO;
import com.ua.driver.exception.DriverNotFoundException;
import com.ua.driver.exception.DuplicateDriverException;
import com.ua.driver.exception.WrongIdException;
import com.ua.driver.model.Car;
import com.ua.driver.model.Category;
import com.ua.driver.model.Driver;

import java.sql.*;
import java.util.*;

import static com.ua.driver.dao.impl.ConnectionFactory.getInstance;

public class DriverDAOH2Impl implements DriverDAO {

    private static final String INSERT_DRIVER = String.format("INSERT INTO drivers (%s, %s, %s, %s) VALUES (?, ?, ?, ?);",
            Driver.FIRST_NAME, Driver.LAST_NAME, Driver.EXPERIENCE, Driver.CATEGORY);
    private static final String GET_ALL_DRIVERS = "SELECT * FROM drivers" +
            " INNER JOIN cars ON drivers.id = cars.driver_id;";
    private static final String DELETE_DRIVER_BY_ID = String.format("DELETE FROM drivers WHERE %s=?", Driver.ID);
    private static final String GET_DRIVER_BY_ID = String.format("SELECT * FROM drivers WHERE %s=?", Driver.ID);
    private static final String UPDATE_DRIVER = String.format("UPDATE drivers SET %s=?, %s=?, %s=?, %s=?" +
            " WHERE %s=?", Driver.FIRST_NAME, Driver.LAST_NAME, Driver.EXPERIENCE, Driver.CATEGORY, Driver.LAST_NAME);
    private static final String CHECK_LAST_NAME = String.format("SELECT * FROM drivers WHERE %s = ?", Driver.LAST_NAME);
    private static final String CLEAN_ALL_DRIVERS = "TRUNCATE TABLE drivers";

    private Connection connection;
    private PreparedStatement pst = null;
    private Statement stmt = null;
    private ResultSet rs;

    private CarDAOH2Impl carDAO;

    public DriverDAOH2Impl() {
        carDAO = new CarDAOH2Impl();
    }

    @Override
    public void addDriver(Driver driver) {
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(INSERT_DRIVER,
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, driver.getFirstName());
            pst.setString(2, driver.getLastName());
            pst.setInt(3, driver.getExperience());
            pst.setString(4, driver.getCategory().name());
            pst.execute();
            rs = pst.getGeneratedKeys();
            rs.next();
            Driver newDriver = new Driver(rs.getInt(1));
            List<Car> cars = driver.getCars();
            if (driver.getCars() != null){
                for (int i = 0; i < cars.size(); i++) {
                    cars.get(i).setDriver(newDriver);
                    carDAO.addCar(cars.get(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeResultSet(rs);
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
    }

    @Override
    public List<Driver> getAllDrivers() {
        List<Driver> result = new ArrayList<>();
        try {
            connection = getInstance().getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(GET_ALL_DRIVERS);
            Map<Integer, Driver> driverMap = new TreeMap<>();
            while (rs.next()) {
                /*for (int i = 1; i < 10; i++) {
                    System.out.println(rs.getObject(i) + "\t");
                }
                System.out.println();*/

                int id = rs.getInt(Driver.ID);
                Driver driver = driverMap.get(id);

                if (driver == null) {
                    driver = new Driver();
                    driver.setId(rs.getInt(Driver.ID));
                    driver.setFirstName(rs.getString(Driver.FIRST_NAME));
                    driver.setLastName(rs.getString(Driver.LAST_NAME));
                    driver.setExperience(rs.getInt(Driver.EXPERIENCE));
                    driver.setCategory(Category.valueOf(rs.getString(Driver.CATEGORY)));
                    driver.setCars(new ArrayList<>());
                    driverMap.put(id, driver);
                }
                Car car = new Car();
                car.setId(rs.getInt(6));
                car.setMaxSpeed(rs.getDouble(Car.MAX_SPEED));
                car.setModel(rs.getString(Car.MODEL));
                car.setYear(rs.getInt(Car.YEAR));
                driver.getCars().add(car);
            }
            result.addAll(driverMap.values());
        }catch (SQLException e){
            e.printStackTrace();
        } finally {
            getInstance().closeResultSet(rs);
            getInstance().closeStatement(stmt);
            getInstance().closeConnection(connection);
        }
        return result;
    }

    @Override
    public Driver getDriverById(int driverId) {
        Driver driver = new Driver();
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(GET_DRIVER_BY_ID);
//            stmt = connection.createStatement();
            pst.setInt(1, driverId);
            rs = pst.executeQuery();

            while (rs.next()) {
                driver.setId(rs.getInt(Driver.ID));
                driver.setFirstName(rs.getString(Driver.FIRST_NAME));
                driver.setLastName(rs.getString(Driver.LAST_NAME));
                driver.setExperience(rs.getInt(Driver.EXPERIENCE));
                driver.setCategory(Category.valueOf(rs.getString(Driver.CATEGORY)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeResultSet(rs);
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
        return driver;
    }

    @Override
    public void updateDriver(Driver driver) {
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(UPDATE_DRIVER);
            pst.setString(1, driver.getFirstName());
            pst.setString(2, driver.getLastName());
            pst.setInt(3, driver.getExperience());
            pst.setString(4, driver.getCategory().name());
            pst.setInt(5, driver.getId());
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
    }

    @Override
    public void deleteDriver(int driverId) throws DriverNotFoundException, WrongIdException {
        try {
            if (driverId < 1) {
                throw new WrongIdException();
            }
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(DELETE_DRIVER_BY_ID);
            pst.setInt(1, driverId);
            int result = pst.executeUpdate();
            if (result == 0) {
                throw new DriverNotFoundException();
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
            stmt.executeUpdate(CLEAN_ALL_DRIVERS);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeStatement(stmt);
            getInstance().closeConnection(connection);
        }
    }

    @Override
    public Driver findByLastName(String name) {
        Driver driver = new Driver();
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(CHECK_LAST_NAME);
            pst.setString(1, name);
            rs = pst.executeQuery();
            while (rs.next()) {
                driver.setId(rs.getInt(Driver.ID));
                driver.setFirstName(rs.getString(Driver.FIRST_NAME));
                driver.setLastName(rs.getString(Driver.LAST_NAME));
                driver.setExperience(rs.getInt(Driver.EXPERIENCE));
                driver.setCategory(Category.valueOf(rs.getString(Driver.CATEGORY)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeResultSet(rs);
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
        return driver;
    }

//    private void createTableIfNotExists(){
//        try {
//            connection = getInstance().getConnection();
//            stmt = connection.createStatement();
//            stmt.executeUpdate(CREATE_DRIVER_TABLE);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            getInstance().closeStatement(stmt);
//            getInstance().closeConnection(connection);
//        }
//    }
}
