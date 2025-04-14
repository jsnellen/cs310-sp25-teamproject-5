/**
 * Data Access Object (DAO) for retrieving Badge objects from the database.
 * Handles database operations related to the Badge table.
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.util.ArrayList;

public class ScheduleOverrideDAO {

    private static final String QUERY_FIND = "SELECT * FROM scheduleoverride";

    private final DAOFactory daoFactory;

    /**
     * Constructs the BadgeDAO with a reference to the DAOFactory.
     *
     * @param daoFactory the DAOFactory instance to get database connections from
     */
    ScheduleOverrideDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Finds and returns a Badge object using the specified badge ID.
     *
     * @param id the badge ID to search for in the database
     * @return the Badge object if found, or null if not found
     * @throws DAOException if a database error occurs
     */
    public ArrayList<ScheduleOverride> list() {

        ScheduleOverride override = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList<ScheduleOverride> s1 = new ArrayList<>();

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND);
                //ps.setString(1, id);
                boolean hasresults = ps.execute();

                if (hasresults) {
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String start = rs.getString("start");
                        String end = rs.getString("end");
                        String badgeid = rs.getString("badgeid");
                        int day = rs.getInt("day");
                        int dailyscheduleid = rs.getInt("dailyscheduleid");

                        override = new ScheduleOverride(id, start, end, badgeid, day, dailyscheduleid);
                        s1.add(override);
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

        return s1;
    }
} 