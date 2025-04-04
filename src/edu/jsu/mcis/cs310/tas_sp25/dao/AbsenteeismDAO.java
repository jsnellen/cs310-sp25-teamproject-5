package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.Absenteeism;
import edu.jsu.mcis.cs310.tas_sp25.Employee;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Data Access Object (DAO) for managing absenteeism records in the database.
 */
public class AbsenteeismDAO {
    
    /** SQL query to find an absenteeism record by employee ID and pay period */
    private static final String QUERY_FIND = "SELECT * FROM absenteeism WHERE employeeid = ? AND payperiod = ?";
    
    /** SQL query to create or update an absenteeism record */
    private static final String QUERY_CREATE = "INSERT INTO absenteeism (employeeid, payperiod, percentage) VALUES (?, ?, ?) " +
                                               "ON DUPLICATE KEY UPDATE percentage = ?";

    /** DAO factory instance for obtaining database connections */
    private final DAOFactory daoFactory;

    /**
     * Constructs an AbsenteeismDAO with the specified DAOFactory.
     * 
     * @param daoFactory the DAOFactory instance to use for database connections
     */
    public AbsenteeismDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Retrieves an absenteeism record for a given employee and pay period.
     * 
     * @param employee the employee whose absenteeism record is to be retrieved
     * @param payPeriodStartDate the start date of the pay period
     * @return an Absenteeism object if found, otherwise null
     */
    public Absenteeism find(Employee employee, LocalDate payPeriodStartDate) {
        Absenteeism absenteeism = null;
        PreparedStatement ps = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, employee.getId());
                ps.setDate(2, Date.valueOf(payPeriodStartDate));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        HashMap<Object, Object> absenteeDetail = new HashMap<>();
                        absenteeDetail.put("employeeid", employee.getId());
                        absenteeDetail.put("startDate", payPeriodStartDate);
                        absenteeDetail.put("percentage", rs.getBigDecimal("percentage").setScale(2, RoundingMode.HALF_UP));
                        absenteeism = new Absenteeism(absenteeDetail);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

        return absenteeism;
    }

    /**
     * Inserts or updates an absenteeism record in the database.
     * 
     * @param absenteeism the Absenteeism object to be created or updated
     */
    public void create(Absenteeism absenteeism) {
        PreparedStatement ps = null;
        
        try {
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_CREATE);
                ps.setInt(1, absenteeism.getEmployeeid());
                ps.setDate(2, Date.valueOf(absenteeism.getStartDate()));
                ps.setBigDecimal(3, absenteeism.getPercentage());
                ps.setBigDecimal(4, absenteeism.getPercentage());
                
                System.err.println(ps);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }
}

   

