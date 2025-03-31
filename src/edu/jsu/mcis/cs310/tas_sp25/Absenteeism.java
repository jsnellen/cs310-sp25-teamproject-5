/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 *
 * @author rwhee
 */
public class Absenteeism {
    private Employee employeeid;
    private LocalDate startDate;
    private BigDecimal percentage;
    
    public Absenteeism(HashMap<Object, Object> AbsenteeDetail){
        this.employeeid = (Employee) AbsenteeDetail.get("employeeid");
        this.startDate = (LocalDate) AbsenteeDetail.get("startDate");
        this.percentage = (BigDecimal) AbsenteeDetail.get("percentage");
    }

    public Absenteeism(Employee e, LocalDate ts, BigDecimal percentage2) {
        this.employeeid = e;
        this.startDate = ts;
        this.percentage = percentage2;
    }

    public Employee getEmployeeid() {
        return employeeid;
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
        build.append("#")
                .append(employeeid.getId()).append(" ")
                .append("Pay Period Starting ")
                .append(startDate.format(start))
                .append(": ")
                .append(percentage.toString())
                .append("%");


        return build.toString();

        }
    
}
