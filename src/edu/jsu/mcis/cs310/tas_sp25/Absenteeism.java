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
 * The Absenteeism class represents an employee's absenteeism data within a given pay period.
 * It includes the employee's ID, the start date of the pay period, and the percentage of absenteeism.
 * This class provides functionality to retrieve and format absenteeism data for reporting purposes.
 */
public class Absenteeism {

    /** The Employee associated with this absenteeism record. */
    private Employee employee;

    /** The ID of the employee. */
    private int employeeid;

    /** The start date of the pay period for this absenteeism record. */
    private LocalDate startDate;

    /** The percentage of absenteeism for the employee in the given pay period. */
    private BigDecimal percentage;

    /**
     * Constructs an Absenteeism object using the provided absentee details.
     * The start date is adjusted to the previous Sunday (or the same day if it's already Sunday).
     *
     * @param AbsenteeDetail a HashMap containing the absenteeism details, including employee ID,
     *                       start date, and absenteeism percentage.
     */
    public Absenteeism(HashMap<Object, Object> AbsenteeDetail) {
        this.employeeid = (int) AbsenteeDetail.get("employeeid");

        // Fetch Employee data from the DAO
        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();

        this.employee = employeeDAO.find(employeeid);

        // Adjust the start date to the previous Sunday (or the same day if it's Sunday)
        this.startDate = ((LocalDate) AbsenteeDetail.get("startDate"))
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        // Set the absenteeism percentage
        this.percentage = (BigDecimal) AbsenteeDetail.get("percentage");
    }

    /**
     * Constructs an Absenteeism object directly from an Employee, a pay period start date,
     * and a given absenteeism percentage.
     *
     * @param e the Employee associated with this absenteeism record.
     * @param ts the start date of the pay period for this absenteeism.
     * @param percentage2 the absenteeism percentage for this pay period.
     */

     
    public Absenteeism(Employee e, LocalDate ts, BigDecimal percentage2) {
        this.employee = e;  // can get rid of!
        this.employeeid = e.getId();
        this.startDate = ts;
        this.percentage = percentage2.setScale(2);
    }

    /**
     * Returns the employee ID associated with this absenteeism record.
     *
     * @return the employee ID.
     */
    public int getEmployeeid() {
        return employeeid;
    }

    /**
     * Returns the Employee associated with this absenteeism record.
     *
     * @return the Employee object.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Returns the start date of the pay period for this absenteeism record.
     *
     * @return the start date of the pay period.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the absenteeism percentage for the employee during the specified pay period.
     *
     * @return the absenteeism percentage.
     */
    public BigDecimal getPercentage() {
        return percentage;
    }

    /**
     * Returns a string representation of this absenteeism record.
     * The string includes the employee's badge ID, the start date of the pay period,
     * and the absenteeism percentage.
     *
     * @return a string representing the absenteeism record.
     */
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
