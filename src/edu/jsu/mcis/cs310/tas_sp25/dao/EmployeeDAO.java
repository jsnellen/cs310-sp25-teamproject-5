/**
 * Data Access Object (DAO) class for accessing Employee records in the database.
 * Provides methods for retrieving employee data by ID or Badge.
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.util.HashMap;

public class EmployeeDAO {

    /** SQL query to find an employee by employee ID. */
    private static final String QUERY_FIND = "SELECT * FROM employee WHERE id = ?";
    
    /** SQL query to find an employee by badge ID. */
    private static final String QUERY_FIND_BADGE = "SELECT * FROM employee WHERE badgeid = ?";

    /** DAOFactory used to obtain database connections and other DAOs. */
    private final DAOFactory daoFactory;

    /**
     * Constructs an EmployeeDAO using the provided DAOFactory.
     *
     * @param daoFactory the DAOFactory to obtain database connections
     */
    EmployeeDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Retrieves an Employee from the database using the employee's ID.
     *
     * @param id the ID of the employee to retrieve
     * @return an Employee object, or null if no match is found
     * @throws DAOException if a database access error occurs
     */
    public Employee find(int id) {

        Employee employee = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);

                boolean hasresults = ps.execute();

                if (hasresults) {
                    rs = ps.getResultSet();

                    while (rs.next()) {
                        HashMap<String, String> EmployeeDetail = new HashMap<>();
                        EmployeeDetail.put("id", rs.getString("id"));
                        EmployeeDetail.put("badgeId", rs.getString("badgeid"));
                        EmployeeDetail.put("firstName", rs.getString("firstname"));
                        EmployeeDetail.put("middleName", rs.getString("middlename"));
                        EmployeeDetail.put("lastName", rs.getString("lastname"));
                        EmployeeDetail.put("employeeTypeId", rs.getString("employeetypeid"));
                        EmployeeDetail.put("departmentId", rs.getString("departmentid"));
                        EmployeeDetail.put("shiftId", rs.getString("shiftid"));
                        EmployeeDetail.put("active", rs.getString("active"));
                        EmployeeDetail.put("inactive", rs.getString("inactive"));
                        
                        employee = new Employee(EmployeeDetail);
                    }
                }
            }

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }

        return employee;
    }

    /**
     * Retrieves an Employee from the database using a Badge object.
     *
     * @param b the Badge associated with the employee to retrieve
     * @return an Employee object, or null if no match is found
     * @throws DAOException if a database access error occurs
     */
    public Employee find(Badge b) {

        Employee employee = null;
        String b1 = b.getId();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND_BADGE);
                ps.setString(1, b1);

                boolean hasresults = ps.execute();

                if (hasresults) {
                    rs = ps.getResultSet();

                    while (rs.next()) {
                        HashMap<String, String> EmployeeDetail = new HashMap<>();
                        EmployeeDetail.put("id", rs.getString("id"));
                        EmployeeDetail.put("badgeId", rs.getString("badgeid"));
                        EmployeeDetail.put("firstName", rs.getString("firstname"));
                        EmployeeDetail.put("middleName", rs.getString("middlename"));
                        EmployeeDetail.put("lastName", rs.getString("lastname"));
                        EmployeeDetail.put("employeeTypeId", rs.getString("employeetypeid"));
                        EmployeeDetail.put("departmentId", rs.getString("departmentid"));
                        EmployeeDetail.put("shiftId", rs.getString("shiftid"));
                        EmployeeDetail.put("active", rs.getString("active"));
                        EmployeeDetail.put("inactive", rs.getString("inactive"));
                        
                        employee = new Employee(EmployeeDetail);
                    }
                }
            }

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }

        return employee;
    }

}
