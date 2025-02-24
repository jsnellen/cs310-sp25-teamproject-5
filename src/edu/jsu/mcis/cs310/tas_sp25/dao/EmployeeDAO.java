package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;

public class EmployeeDAO {

    private static final String QUERY_FIND = "SELECT * FROM employee WHERE id = ?";

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

                        String badgeId = rs.getString("badgeid");
                        String firstName = rs.getString("firstname");
                        String middleName = rs.getString("middlename");
                        String lastName = rs.getString("lastName");
                        int employeeTypeId = rs.getInt("employeetypeid");
                        int departmentId = rs.getInt("departmentid");
                        int shiftId = rs.getInt("shiftId");
                        String active = rs.getString("active");
                        String inactive = rs.getString("inactive");
                        employee = new Employee(id, badgeId, firstName, middleName, lastName, employeeTypeId, departmentId, shiftId, active, inactive);

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
