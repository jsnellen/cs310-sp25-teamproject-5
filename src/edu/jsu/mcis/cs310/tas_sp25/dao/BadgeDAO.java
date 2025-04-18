/**
 * Data Access Object (DAO) for retrieving Badge objects from the database.
 * Handles database operations related to the Badge table.
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;

public class BadgeDAO {

    private static final String QUERY_FIND = "SELECT * FROM badge WHERE id = ?";
    private static final String QUERY_INSERT = "INSERT INTO badge (id, description) VALUES (?, ?)";
    private static final String QUERY_UPDATE = "UPDATE badge SET description = ? WHERE id = ?";
    private static final String QUERY_DELETE = "DELETE FROM badge WHERE id = ?";

    private final DAOFactory daoFactory;

    /**
     * Constructs the BadgeDAO with a reference to the DAOFactory.
     *
     * @param daoFactory the DAOFactory instance to get database connections from
     */
    BadgeDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Finds and returns a Badge object using the specified badge ID.
     *
     * @param id the badge ID to search for in the database
     * @return the Badge object if found, or null if not found
     * @throws DAOException if a database error occurs
     */
    public Badge find(String id) {

        Badge badge = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND);
                ps.setString(1, id);
                boolean hasresults = ps.execute();

                if (hasresults) {
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        String description = rs.getString("description");
                        badge = new Badge(id, description);
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

        return badge;
    }
    
    
    public boolean create(Badge badge) {
        
        boolean result = false;
        PreparedStatement ps = null;
        
        try {
            Connection conn = daoFactory.getConnection();
            ps = conn.prepareStatement(QUERY_INSERT); 

            ps.setString(1, badge.getId());
            ps.setString(2, badge.getDescription());
            result = (ps.executeUpdate() == 1);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
         if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public boolean update(Badge badge) {
       PreparedStatement ps = null;
        
        try {
            Connection conn = daoFactory.getConnection();
            ps = conn.prepareStatement(QUERY_UPDATE); 

            ps.setString(1, badge.getId());
            ps.setString(2, badge.getDescription());
            return (ps.executeUpdate() == 1);
            

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        finally {
         if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }
        //return (ps.executeUpdate() == 1);
    }

    public boolean delete(String id) {
        PreparedStatement ps = null;
        try {
            Connection conn = daoFactory.getConnection();
            ps = conn.prepareStatement(QUERY_DELETE);

            ps.setString(1, id);
            return (ps.executeUpdate() == 1);

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
         if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }
    }
} 
