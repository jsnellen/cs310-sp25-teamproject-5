package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.util.HashMap;

public class ShiftDAO {

    private static final String FIND_BY_ID = "SELECT * FROM shift JOIN dailyschedule ON dailyscheduleid WHERE shift.id = ? AND dailyschedule.id = ?";
    private static final String FIND_BY_BADGE = "SELECT shiftid FROM employee WHERE badgeid =?";

    private final DAOFactory daoFactory;

    public ShiftDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Find Shift by ID
    public Shift find(int id) {
        
        Shift shift = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(FIND_BY_ID);
                ps.setInt(1, id);
                ps.setInt(2, id);

                System.err.println(ps.toString());

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        
                        // Store shift data into a HashMap for easy access
                        
                        HashMap<String, String> ShiftDetail = new HashMap<>();
                        ShiftDetail.put("id", rs.getString("dailyschedule.id"));
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
                        shift = new Shift (ShiftDetail);

                        System.err.println(shift.toString());
                        
                    }

                }

            }

        } catch (SQLException e) {

            // Handle SQL exceptions by throwing a DAOException
            throw new DAOException(e.getMessage());

        } finally {

             // Ensure ResultSet and PreparedStatement are closed properly
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

        return shift;

    }
    
     // Method to find a shift by employee badge
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

        return shift;

    }
} 
