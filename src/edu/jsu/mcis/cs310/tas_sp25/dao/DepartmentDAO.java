package edu.jsu.mcis.cs310.tas_sp25.dao;
import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;

public class DepartmentDAO {

    private static final String QUERY_FIND = "SELECT * FROM department WHERE id = ?";

    private final DAOFactory daoFactory;

    public DepartmentDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

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

                        String description = rs.getString("description");
                        int terminalid = rs.getInt("terminalid");

                        department = new Department(id, description, terminalid);
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