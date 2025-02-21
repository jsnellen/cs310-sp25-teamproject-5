package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PunchDAO {

    private static final String QUERY_FIND = "SELECT * FROM event WHERE id = ?";
    private static final String QUERY_LIST = "SELECT * from event WHERE badgeid = ? AND DATE(timestamp) = ?";

    private final DAOFactory daoFactory;

    PunchDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }


    ///////******* RETURNS PUNCH GIVEN PUNCH ID */
    /// 
    /// 
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

                        int terminalid = rs.getInt("terminalid");
                        String badgeid = rs.getString("badgeid");
                        String timestamp = rs.getString("timestamp");
                        int eventtypeid = rs.getInt("eventtypeid");
                        punch = new Punch(id, terminalid, badgeid, timestamp, eventtypeid);

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

        return punch;

    }

    /////***** RETURNS LIST OF PUNCHES GIVEN BADGE AND DATE */
    /// 
    /// 
    public ArrayList<Punch> list(Badge badgeid, LocalDate timestamp) {

        Punch punch = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        String b = badgeid.getId();
        String ts = timestamp.toString();

        System.err.println(ts);

        ArrayList<Punch> p1 = new ArrayList<>();

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_LIST);
                ps.setString(1, b);
                ps.setString(2, ts);

                System.err.println(b);
                System.err.println(ts);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {

                        int id = rs.getInt("id");
                        int terminalid = rs.getInt("terminalid");
                        String b1 = rs.getString("badgeid");
                        String ts1 = rs.getString("timestamp");
                        int eventtypeid = rs.getInt("eventtypeid");
                        punch = new Punch(id, terminalid, b1, ts1, eventtypeid);

                        System.err.println("AAAAAAA");

                        p1.add(punch);
                        

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

        return p1;

    }

}

