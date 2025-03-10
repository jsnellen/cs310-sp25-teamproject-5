package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

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

                        HashMap<String, String> PunchDetail = new HashMap<>();

                        PunchDetail.put("id", rs.getString("id"));
                        PunchDetail.put("terminalid", rs.getString("terminalid"));
                        PunchDetail.put("badgeid", rs.getString("badgeid"));
                        PunchDetail.put("timestamp", rs.getString("timestamp"));
                        PunchDetail.put("eventtypeid", rs.getString("eventtypeid"));

                        punch = new Punch (PunchDetail);

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

                        HashMap<String, String> PunchDetail = new HashMap<>();

                        PunchDetail.put("id", rs.getString("id"));
                        PunchDetail.put("terminalid", rs.getString("terminalid"));
                        PunchDetail.put("badgeid", rs.getString("badgeid"));
                        PunchDetail.put("timestamp", rs.getString("timestamp"));
                        PunchDetail.put("eventtypeid", rs.getString("eventtypeid"));

                        punch = new Punch (PunchDetail);

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


    public void create(Punch p1) {
        
        HashMap<String, String> PunchDetail = new HashMap<>();

        PunchDetail.put("terminalid", );
        PunchDetail.put("badgeid", );
        PunchDetail.put("timestamp", );
        PunchDetail.put("eventtypeid", );

        punch = new Punch (PunchDetail);



    }

}

