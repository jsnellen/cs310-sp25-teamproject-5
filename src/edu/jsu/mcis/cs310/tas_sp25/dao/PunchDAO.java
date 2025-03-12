package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class PunchDAO {

    private static final String QUERY_FIND = "SELECT * FROM event WHERE id = ?";
    private static final String QUERY_LIST = "SELECT * from event WHERE badgeid = ? AND DATE(timestamp) = ?";
    private static final String QUERY_CREATE = "INSERT INTO event (terminalid, badgeid, eventtypeid) VALUES (?, ?, ?)";
    private static final String QUERY_LAST = "SELECT * FROM event ORDER BY id DESC LIMIT 1";
    private static final String QUERY_ = "";

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


    public int create(Punch p1) {

        //Punch punch = null;

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        int punchid = 0;

        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        //BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();

        Employee e1 = employeeDAO.find(p1.getBadge());
        Department d1 = departmentDAO.find(e1.getDepartment());


        if((p1.getTerminalid() == d1.getTerminalid()) || (p1.getTerminalid() == 0)) {

            System.err.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        


            try {

                Connection conn = daoFactory.getConnection();

                if (conn.isValid(0)) {

                    ps = conn.prepareStatement(QUERY_CREATE);
                    ps.setInt(1, p1.getTerminalid());
                    ps.setString(2, p1.getBadgeId());
                    ps.setInt(3, p1.getEventTypeId());

                    //System.err.println(ps.toString());

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

                if (ps1 != null) {
                    try {
                        ps1.close();
                    } catch (SQLException e) {
                        throw new DAOException(e.getMessage());
                    }
                }

            }

                //return punchid;

            

        }

        return punchid;

    }
        

}

