package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.util.HashMap;

public class EmployeeDAO {

    private static final String QUERY_FIND = "SELECT * FROM employee WHERE id = ?";
    private static final String QUERY_FIND_BADGE = "SELECT * FROM employee WHERE badgeid = ?";

    private final DAOFactory daoFactory;

    EmployeeDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }

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
                        
                        // Create a Shift object using the extracted data
                        employee = new Employee (EmployeeDetail);

                    }

                }

            }

        } catch (SQLException e) {

            throw new DAOException(e.getMessage());

        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }

        }

        return employee;

    }

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
                        
                        // Create a Shift object using the extracted data
                        employee = new Employee (EmployeeDetail);
                        
                    }

                }

            }

        } catch (SQLException e) {

            throw new DAOException(e.getMessage());

        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }

        }

        return employee;

    }

}
