package com.ua.driver.dao.impl;

import com.ua.driver.dao.DriverDAO;
import com.ua.driver.exception.DriverNotFoundException;
import com.ua.driver.exception.DuplicateDriverException;
import com.ua.driver.exception.WrongIdException;
import com.ua.driver.model.Category;
import com.ua.driver.model.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.ua.driver.dao.impl.ConnectionFactory.getInstance;

public class DriverDAOMySQLImpl implements DriverDAO {

    private static final String CREATE_DRIVER_TABLE = "CREATE TABLE IF NOT EXISTS drivers (" +
            Driver.ID + " INT(11) PRIMARY KEY AUTO_INCREMENT," +
            Driver.FIRST_NAME + " VARCHAR(255)," +
            Driver.LAST_NAME + " VARCHAR(255) UNIQUE," +
            Driver.EXPERIENCE + " INT(2)," +
            Driver.CATEGORY + " VARCHAR(255)" +
            ")";
    private static final String INSERT_DRIVER = String.format("INSERT INTO drivers (%s, %s, %s, %s) VALUES (?, ?, ?, ?);",
            Driver.FIRST_NAME, Driver.LAST_NAME, Driver.EXPERIENCE, Driver.CATEGORY);
    private static final String GET_ALL_DRIVERS = "SELECT * FROM drivers";
    private static final String DELETE_DRIVER_BY_ID = String.format("DELETE FROM drivers WHERE %s=?", Driver.ID);
    private static final String FIND_DRIVER_BY_ID = String.format("SELECT * FROM drivers WHERE %s=?", Driver.ID);
    private static final String UPDATE_DRIVER_BY_ID = String.format("UPDATE drivers SET %s=?, %s=?, %s=?, %s=?" +
            " WHERE %s=?", Driver.FIRST_NAME, Driver.LAST_NAME, Driver.EXPERIENCE, Driver.CATEGORY, Driver.LAST_NAME);

    private Connection connection;
    private PreparedStatement pst = null;
    private Statement stmt = null;
    private ResultSet rs;

    public DriverDAOMySQLImpl() {
        createTable();
    }

    @Override
    public void addDriver(Driver driver) throws DuplicateDriverException {
        for (Driver temp : getAllDrivers()) {
            if (temp.getLastName().equals(driver.getLastName())) {
                throw new DuplicateDriverException();
            }
        }
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(INSERT_DRIVER);
            pst.setString(1, driver.getFirstName());
            pst.setString(2, driver.getLastName());
            pst.setInt(3, driver.getExperience());
            pst.setString(4, driver.getCategory().name());
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
            while (rs.next()) {
                Driver driver = new Driver();
                driver.setId(rs.getInt(Driver.ID));
                driver.setFirstName(rs.getString(Driver.FIRST_NAME));
                driver.setLastName(rs.getString(Driver.LAST_NAME));
                driver.setExperience(rs.getInt(Driver.EXPERIENCE));
                driver.setCategory(Category.valueOf(rs.getString(Driver.CATEGORY)));
                result.add(driver);
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
    public Driver getDriverById(int driverId) throws WrongIdException {
        Driver driver = new Driver();
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(FIND_DRIVER_BY_ID);
            pst.setInt(1, driverId);
            pst.execute();
            rs = pst.executeQuery();
            if (rs.next()) {
                driver.setId(rs.getInt(Driver.ID));
                driver.setFirstName(rs.getString(Driver.FIRST_NAME));
                driver.setLastName(rs.getString(Driver.LAST_NAME));
                driver.setExperience(rs.getInt(Driver.EXPERIENCE));
                driver.setCategory(Category.valueOf(rs.getString(Driver.CATEGORY)));
            } else {
                throw new WrongIdException();
            }
            return driver;
        } catch (SQLException e) {
            e.printStackTrace();
            return driver;
        } finally {
            getInstance().closeResultSet(rs);
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
    }

    @Override
    public void updateDriver(Driver driver) {
        int i = 0;
        for (Driver tempDriver : getAllDrivers()) {
            if (tempDriver.getLastName().equals(driver.getLastName())) {
                try {
                    connection = getInstance().getConnection();
                    pst = connection.prepareStatement(UPDATE_DRIVER_BY_ID);
                    pst.setString(1, driver.getFirstName());
                    pst.setString(2, driver.getLastName());
                    pst.setInt(3, driver.getExperience());
                    pst.setString(4, driver.getCategory().toString());
                    pst.setString(5, driver.getLastName());
                    int rowsUpdated = pst.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("An existing driver was updated successfully!");
                    }
                    i = 1;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    getInstance().closePreparedStatement(pst);
                    getInstance().closeConnection(connection);
                }
            }
        }
        if (i == 0) {
            try {
                addDriver(driver);
            } catch (DuplicateDriverException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteDriver(int driverId) throws DriverNotFoundException {
        try {
            connection = getInstance().getConnection();
            pst = connection.prepareStatement(DELETE_DRIVER_BY_ID);
            pst.setInt(1, driverId);
            pst.execute();
            int result = pst.executeUpdate();
            if (result == 0) {
                throw new DriverNotFoundException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closePreparedStatement(pst);
            getInstance().closeConnection(connection);
        }
    }

    private void createTable() {
        try {
            connection = getInstance().getConnection();
            stmt = connection.createStatement();
            stmt.executeUpdate(CREATE_DRIVER_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            getInstance().closeStatement(stmt);
            getInstance().closeConnection(connection);
        }
    }
}
