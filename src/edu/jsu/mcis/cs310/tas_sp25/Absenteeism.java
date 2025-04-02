/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.DAOFactory;
import edu.jsu.mcis.cs310.tas_sp25.dao.EmployeeDAO;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;

/**
 *
 * @author rwhee
 */
public class Absenteeism {

    private Employee employee;
    private int employeeid;
    private LocalDate startDate;
    private BigDecimal percentage;
    
    public Absenteeism(HashMap<Object, Object> AbsenteeDetail){
        this.employeeid = (int) AbsenteeDetail.get("employeeid");

        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();

        this.employee = employeeDAO.find(employeeid);
        this.startDate = ((LocalDate) AbsenteeDetail.get("startDate"))
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        this.percentage = (BigDecimal) AbsenteeDetail.get("percentage");
    }

    public Absenteeism(Employee e, LocalDate ts, BigDecimal percentage2) {
        this.employee = e;
        this.employeeid = e.getId();
        this.startDate = ts;
        this.percentage = percentage2;
    }

    public int getEmployeeid() {
        return employeeid;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }
    
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        DateTimeFormatter start = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        // Append Absenteeism details to the StringBuilder
        System.err.println("Test Point 8: " + employeeid + " " + startDate);


        build.append("#")
                .append(employee.getBadge().getId()).append(" (")
                .append("Pay Period Starting ")
                .append(startDate.format(start))
                .append("): ")
                .append(percentage.toString())
                .append("%");


        return build.toString();

        }
    
}
