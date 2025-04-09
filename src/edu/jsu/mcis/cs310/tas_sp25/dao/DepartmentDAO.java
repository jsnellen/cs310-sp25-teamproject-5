/**
 * Data Access Object (DAO) class for accessing Department records in the database.
 * Provides methods for retrieving department data by ID.
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.util.HashMap;

public class DepartmentDAO {

    /** SQL query used to find a department by ID. */
    private static final String QUERY_FIND = "SELECT * FROM department WHERE id = ?";

    /** Factory object used to obtain database connections and other DAOs. */
    private final DAOFactory daoFactory;

    /**
     * Constructs a DepartmentDAO using the provided DAOFactory.
     *
     * @param daoFactory the DAOFactory to obtain database connections
     */
    public DepartmentDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Finds and retrieves a Department record from the database using the department ID.
     *
     * @param id the ID of the department to retrieve
     * @return the Department object, or null if no matching record is found
     * @throws DAOException if a database access error occurs
     */
    public Department find(int id) {

        Department department = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);

                boolean hasResults = ps.execute();

                if (hasResults) {

                    rs = ps.getResultSet();

                    if (rs.next()) {
                        HashMap<String, String> DepartmentDetail = new HashMap<>(); 
                        DepartmentDetail.put("id", rs.getString("id"));
                        DepartmentDetail.put("description", rs.getString("description"));
                        DepartmentDetail.put("terminalid", rs.getString("terminalid"));
                        
                        department = new Department(DepartmentDetail);
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

        return department;
    }
}
