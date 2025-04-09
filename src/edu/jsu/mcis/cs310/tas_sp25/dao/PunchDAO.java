/**
 * Data Access Object (DAO) class for accessing Punch (event) records in the database.
 * Provides methods for retrieving punches by ID, badge, and timestamp, as well as inserting new punches.
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class PunchDAO {

    /** SQL query to find a punch by punch ID. */
    private static final String QUERY_FIND = "SELECT * FROM event WHERE id = ?";

    /** SQL query to list punches by badge ID and timestamp (date). */
    private static final String QUERY_LIST = "SELECT * FROM event WHERE badgeid = ? AND DATE(timestamp) = ?";

    /** SQL query to insert a new punch event into the database. */
    private static final String QUERY_CREATE = "INSERT INTO event (terminalid, badgeid, eventtypeid) VALUES (?, ?, ?)";

    /** SQL query to get the last inserted punch event. */
    private static final String QUERY_LAST = "SELECT * FROM event ORDER BY id DESC LIMIT 1";

    /** DAOFactory used to obtain database connections and other DAOs. */
    private final DAOFactory daoFactory;

    /**
     * Constructs a PunchDAO using the provided DAOFactory.
     *
     * @param daoFactory the DAOFactory to obtain database connections
     */
    PunchDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Retrieves a Punch from the database using the given punch ID.
     *
     * @param id the ID of the punch to retrieve
     * @return a Punch object, or null if no match is found
     * @throws DAOException if a database access error occurs
     */
    public Punch find(int id) {

        Punch punch = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND);
                ps.setString(1, String.valueOf(id));

                boolean hasresults = ps.execute();

                if (hasresults) {
                    rs = ps.getResultSet();

                    while (rs.next()) {
                        HashMap<String, String> PunchDetail = new HashMap<>();

                        PunchDetail.put("id", rs.getString("id"));
                        PunchDetail.put("terminalid", rs.getString("terminalid"));
                        PunchDetail.put("badgeid", rs.getString("badgeid"));
                        PunchDetail.put("timestamp", rs.getString("timestamp"));
                        PunchDetail.put("eventtypeid", rs.getString("eventtypeid"));

                        punch = new Punch(PunchDetail);
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

        return punch;
    }

    /**
     * Retrieves a list of punches for a given badge and date.
     *
     * @param badgeid the Badge object containing the badge ID
     * @param timestamp the date of the punches to retrieve
     * @return a list of Punch objects
     * @throws DAOException if a database access error occurs
     */
    public ArrayList<Punch> list(Badge badgeid, LocalDate timestamp) {

        Punch punch = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String b = badgeid.getId();
        String ts = timestamp.toString();

        ArrayList<Punch> p1 = new ArrayList<>();

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_LIST);
                ps.setString(1, b);
                ps.setString(2, ts);

                boolean hasresults = ps.execute();

                if (hasresults) {
                    rs = ps.getResultSet();

                    while (rs.next()) {
                        HashMap<String, String> PunchDetail = new HashMap<>();

                        PunchDetail.put("id", rs.getString("id"));
                        PunchDetail.put("terminalid", rs.getString("terminalid"));
                        PunchDetail.put("badgeid", rs.getString("badgeid"));
                        PunchDetail.put("timestamp", rs.getString("timestamp"));
                        PunchDetail.put("eventtypeid", rs.getString("eventtypeid"));

                        punch = new Punch(PunchDetail);

                        p1.add(punch);
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

        return p1;
    }

    /**
     * Retrieves a list of punches for a given badge and date range.
     *
     * @param badgeid the Badge object containing the badge ID
     * @param timestamp the start date of the range
     * @param timestamp1 the end date of the range
     * @return a list of Punch objects for the specified date range
     * @throws DAOException if a database access error occurs
     */
    public ArrayList<Punch> list(Badge badgeid, LocalDate timestamp, LocalDate timestamp1) {

        ArrayList<Punch> p1 = new ArrayList<>();

        for (LocalDate date = timestamp; !date.isAfter(timestamp1); date = date.plusDays(1)) {
            p1.addAll(this.list(badgeid, date));
        }

        return p1;
    }

    /**
     * Inserts a new punch into the database.
     *
     * @param p1 the Punch object to insert into the database
     * @return the ID of the newly created punch
     * @throws DAOException if a database access error occurs
     */
    public int create(Punch p1) {

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        int punchid = 0;

        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();

        Employee e1 = employeeDAO.find(p1.getBadge());
        Department d1 = departmentDAO.find(e1.getDepartment());

        if ((p1.getTerminalid() == d1.getTerminalid()) || (p1.getTerminalid() == 0)) {

            try {
                Connection conn = daoFactory.getConnection();

                if (conn.isValid(0)) {
                    ps = conn.prepareStatement(QUERY_CREATE);
                    ps.setInt(1, p1.getTerminalid());
                    ps.setString(2, p1.getBadgeId());
                    ps.setInt(3, p1.getEventTypeId());

                    ps.executeUpdate();

                    ps1 = conn.prepareStatement(QUERY_LAST);

                    boolean hasresults = ps1.execute();

                    if (hasresults) {
                        rs = ps1.getResultSet();

                        while (rs.next()) {
                            punchid = rs.getInt("id");
                        }
                    }
                }

            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (ps != null) ps.close();
                    if (ps1 != null) ps1.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }

        return punchid;
    }
}
