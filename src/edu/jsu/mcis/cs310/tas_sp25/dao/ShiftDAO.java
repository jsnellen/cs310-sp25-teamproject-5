/**
 * Data Access Object (DAO) class for accessing Shift records in the database.
 * Provides methods for retrieving shifts by ID and by employee badge.
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

public class ShiftDAO {

    /** SQL query to find a shift by shift ID. */
    //private static final String FIND_BY_ID = "SELECT * FROM shift WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM dailyschedule JOIN shift ON shift.dailyscheduleid = dailyschedule.id WHERE shift.id = ?";

    /** SQL query to find a shift by employee badge ID. */
    private static final String FIND_BY_BADGE = "SELECT shiftid FROM employee WHERE badgeid =?";

    /** DAOFactory used to obtain database connections. */
    private final DAOFactory daoFactory;

    /**
     * Constructs a ShiftDAO using the provided DAOFactory.
     *
     * @param daoFactory the DAOFactory to obtain database connections
     */
    public ShiftDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Retrieves a Shift from the database using the given shift ID.
     *
     * @param id the ID of the shift to retrieve
     * @return a Shift object, or null if no match is found
     * @throws DAOException if a database access error occurs
     */
    public Shift find(int id) {
        
        Shift shift = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(FIND_BY_ID);
                ps.setInt(1, id);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        
                        // Store shift data into a HashMap for easy access
                        HashMap<String, String> ShiftDetail = new HashMap<>();
                        ShiftDetail.put("id", rs.getString("id"));
                        ShiftDetail.put("description", rs.getString("description"));
                        ShiftDetail.put("shiftStart", rs.getString("shiftstart"));
                        ShiftDetail.put("shiftStop", rs.getString("shiftstop"));
                        ShiftDetail.put("roundInterval", rs.getString("roundinterval"));
                        ShiftDetail.put("gracePeriod", rs.getString("graceperiod"));
                        ShiftDetail.put("dockPenalty", rs.getString("dockpenalty"));
                        ShiftDetail.put("lunchStart", rs.getString("lunchstart"));
                        ShiftDetail.put("lunchStop", rs.getString("lunchstop"));
                        ShiftDetail.put("lunchThreshold", rs.getString("lunchthreshold"));
                        
                        // Create a Shift object using the extracted data
                        shift = new Shift(ShiftDetail);
                    }

                }

            }

        } catch (SQLException e) {

            // Handle SQL exceptions by throwing a DAOException
            throw new DAOException(e.getMessage());

        } finally {

            // Ensure ResultSet and PreparedStatement are closed properly
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }

        }

        return shift;
    }

    /**
     * Retrieves the shift assigned to an employee based on their badge ID.
     *
     * @param badge the Badge object representing the employee's badge
     * @return a Shift object, or null if no shift is assigned to the badge
     * @throws DAOException if a database access error occurs
     */

    public Shift find(Badge badge) {
        
        // Variable to store the found shift
        Shift shift = null; 
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            
            // Establish database connection using DAOFactory
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                   
                // Prepare the SQL query to find a shift by employee badge
                ps = conn.prepareStatement(FIND_BY_BADGE);
                ps.setString(1, badge.getId());

                // Execute the query and check if there are results
                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    if (rs.next()) {
                        
                        // Retrieve the shift ID associated with the badge
                        int shiftId = rs.getInt("shiftid");
                        shift = find(shiftId);
                    }

                }

            }

        } catch (SQLException e) {
            
            // Handle SQL exceptions by throwing a DAOException
            throw new DAOException(e.getMessage());

        } finally {

            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }

        }

        return shift;
    }

    public Shift find(Badge badge, LocalDate ts) {
        
        // Variable to store the found shift
        Shift shift = null; 
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            
            // Establish database connection using DAOFactory
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                   
                // Prepare the SQL query to find a shift by employee badge
                ps = conn.prepareStatement(FIND_BY_BADGE);
                ps.setString(1, badge.getId());

                // Execute the query and check if there are results
                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    if (rs.next()) {
                        
                        // Retrieve the shift ID associated with the badge
                        int shiftId = rs.getInt("shiftid");
                        shift = find(shiftId);
                    }

                }

            }

        } catch (SQLException e) {
            
            // Handle SQL exceptions by throwing a DAOException
            throw new DAOException(e.getMessage());

        } finally {

            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }

        }

        return shift;
    }


}
