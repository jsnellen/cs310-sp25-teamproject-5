package edu.jsu.mcis.cs310.tas_sp25.dao;

import com.github.cliftonlabs.json_simple.*;
import java.sql.*;

public class ReportDAO {

    private final DAOFactory daoFactory;

    private static final String QUERY = "SELECT * FROM badge LEFT OUTER JOIN employee on badge.id = employee.badgeid LEFT OUTER JOIN department on employee.departmentid = department.id";

    public ReportDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public String getBadgeSummary(Integer departmentId) throws SQLException {
        JsonArray badgeArray = new JsonArray();

       

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY)) {

            if (departmentId != null) {
                ps.setInt(1, departmentId);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JsonObject obj = new JsonObject();
                obj.put("badgeid", rs.getString("badge.id"));
                obj.put("name", rs.getString("firstname"));
                obj.put("department", rs.getString("department.id"));
                obj.put("type", rs.getString("employee.type"));
                badgeArray.add(obj);
            }
        }

        return badgeArray.toJson();
    }
}
