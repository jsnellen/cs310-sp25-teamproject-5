package edu.jsu.mcis.cs310.tas_sp25.dao;

import com.github.cliftonlabs.json_simple.*;
import java.sql.*;

public class ReportDAO {

    private final DAOFactory daoFactory;

    // SQL base query with joins
    private static final String QUERY_BASE = 
    "SELECT b.id AS badgeid, " +
    "       CONCAT(e.lastname, ', ', e.firstname, ' ', IFNULL(e.middlename, '')) AS name, " +
    "       d.description AS department, " +
    "       CASE WHEN e.employeetypeid = 1 THEN 'Full-Time Employee' ELSE 'Temporary Employee' END AS type " +
    "FROM badge b " +
    "JOIN employee e ON b.id = e.badgeid " +
    "JOIN department d ON e.departmentid = d.id";


    private static final String QUERY_FILTER = " AND d.id = ?";
    private static final String QUERY_ORDER = " ORDER BY e.lastname, e.firstname, e.middlename";

    public ReportDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public String getBadgeSummary(Integer departmentId) throws SQLException {
        JsonArray badgeArray = new JsonArray();

        // Build final query
        StringBuilder query = new StringBuilder(QUERY_BASE);
        if (departmentId != null) {
            query.append(QUERY_FILTER);
        }
        query.append(QUERY_ORDER);

        try (
            Connection conn = daoFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query.toString())
        ) {
            // Inject department filter
            if (departmentId != null) {
                ps.setInt(1, departmentId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JsonObject badge = new JsonObject();
                    badge.put("badgeid", rs.getString("badgeid"));
                    String name = rs.getString("name").replaceAll("\\s+", " ").trim();
                    badge.put("name", name);
                    badge.put("department", rs.getString("department"));
                    badge.put("type", rs.getString("type"));
                    badgeArray.add(badge);
                }
            }
        }

        return Jsoner.serialize(badgeArray);
    }
}
