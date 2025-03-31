/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.Absenteeism;
import edu.jsu.mcis.cs310.tas_sp25.Employee;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;



public class AbsenteeismDAO {
    

    private static final String QUERY_FIND = "SELECT * FROM absenteeism WHERE employeeid = ? AND payperiod = ?";
    private static final String QUERY_CREATE = "INSERT INTO absenteeism (employeeid, payperiod, percentage) VALUES (?, ?, ?) " + "ON DUPLICATE KEY UPDATE percentage = ?";

    private final DAOFactory daoFactory;

    public AbsenteeismDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public Absenteeism find(Employee employee, LocalDate payPeriodStartDate) {
        Absenteeism absenteeism = null;

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_FIND)) {

            ps.setInt(1, employee.getId());
            ps.setDate(2, Date.valueOf(payPeriodStartDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HashMap<Object, Object> absenteeDetail = new HashMap<>();
                    absenteeDetail.put("employee", employee);
                    absenteeDetail.put("payPeriodStartDate", payPeriodStartDate);
                    absenteeDetail.put("percentage", rs.getBigDecimal("percentage"));
                    absenteeism = new Absenteeism(absenteeDetail);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

        return absenteeism;
    }

    public void create(Absenteeism absenteeism) {
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_CREATE)) {

            ps.setInt(1, absenteeism.getEmployeeid().getId());
            ps.setDate(2, Date.valueOf(absenteeism.getStartDate()));
            ps.setBigDecimal(3, absenteeism.getPercentage());
            ps.setBigDecimal(4, absenteeism.getPercentage());

            System.err.println(ps);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }
    
    
}
   

