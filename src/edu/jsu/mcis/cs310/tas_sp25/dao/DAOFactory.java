/**
 * DAOFactory is responsible for creating and managing DAO instances
 * and maintaining a single database connection.
 * It uses configuration properties (like database URL, username, and password)
 * to establish the connection and return DAOs for different entities.
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.sql.*;

public final class DAOFactory {

    private static final String PROPERTY_URL = "url";
    private static final String PROPERTY_USERNAME = "username";
    private static final String PROPERTY_PASSWORD = "password";

    private final String url, username, password;
    
    private Connection conn = null;

    /**
     * Constructs a DAOFactory using a prefix to read database connection properties.
     *
     * @param prefix the prefix used to locate the database properties
     */
    public DAOFactory(String prefix) {

        DAOProperties properties = new DAOProperties(prefix);

        this.url = properties.getProperty(PROPERTY_URL);
        this.username = properties.getProperty(PROPERTY_USERNAME);
        this.password = properties.getProperty(PROPERTY_PASSWORD);

        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

    }

    /**
     * Returns the current database connection used by DAOs.
     *
     * @return the established {@link Connection}
     */
    Connection getConnection() {
        return conn;
    }

    /**
     * Returns an instance of {@link BadgeDAO}.
     *
     * @return BadgeDAO instance
     */
    public BadgeDAO getBadgeDAO() {
        return new BadgeDAO(this);
    }

    /**
     * Returns an instance of {@link DepartmentDAO}.
     *
     * @return DepartmentDAO instance
     */
    public DepartmentDAO getDepartmentDAO() {
        return new DepartmentDAO(this);
    }

    /**
     * Returns an instance of {@link PunchDAO}.
     *
     * @return PunchDAO instance
     */
    public PunchDAO getPunchDAO() {
        return new PunchDAO(this);
    }

    /**
     * Returns an instance of {@link ShiftDAO}.
     *
     * @return ShiftDAO instance
     */
    public ShiftDAO getShiftDAO() {
        return new ShiftDAO(this);
    }

    /**
     * Returns an instance of {@link EmployeeDAO}.
     *
     * @return EmployeeDAO instance
     */
    public EmployeeDAO getEmployeeDAO() {
        return new EmployeeDAO(this);
    }

    /**
     * Returns an instance of {@link AbsenteeismDAO}.
     *
     * @return AbsenteeismDAO instance
     */
    public AbsenteeismDAO getAbsenteeismDAO() {
        return new AbsenteeismDAO(this);
    }
}

