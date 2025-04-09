package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.BadgeDAO;
import edu.jsu.mcis.cs310.tas_sp25.dao.DAOFactory;
import edu.jsu.mcis.cs310.tas_sp25.dao.ShiftDAO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * The Employee class represents an employee in the time and attendance system.
 * It contains employee details such as ID, name, type, department, shift, and badge.
 */
public class Employee {

    /** The unique ID of the employee. */
    private final int id;

    /** The employee type ID (e.g., Full-Time, Temporary/Part-Time). */
    private final int employeeTypeId;

    /** The department ID to which the employee belongs. */
    private final int departmentId;

    /** The shift ID assigned to the employee. */
    private final int shiftId;

    /** The badge ID associated with the employee. */
    private final String badgeId;

    /** The first name of the employee. */
    private final String firstName;

    /** The middle name of the employee. */
    private final String middleName;

    /** The last name of the employee. */
    private final String lastName;

    /** The activation status of the employee. */
    private final String active;

    /** The deactivation status of the employee. */
    private final String inactive;

    /** The shift assigned to the employee. */
    private final Shift shift;

    /** The badge associated with the employee. */
    private final Badge badge;

    /**
     * Constructs an Employee object using the details provided in a HashMap.
     *
     * @param EmployeeDetail a HashMap containing employee details such as id, name, type, department, and shift.
     */
    public Employee(HashMap<String, String> EmployeeDetail) {

        this.id = Integer.valueOf((String) EmployeeDetail.get("id"));
        this.employeeTypeId = Integer.valueOf((String) EmployeeDetail.get("employeeTypeId"));
        this.departmentId = Integer.valueOf((String) EmployeeDetail.get("departmentId"));
        this.shiftId = Integer.valueOf((String) EmployeeDetail.get("shiftId"));
        this.badgeId = (String) EmployeeDetail.get("badgeId");
        this.firstName = (String) EmployeeDetail.get("firstName");
        this.middleName = (String) EmployeeDetail.get("middleName");
        this.lastName = (String) EmployeeDetail.get("lastName");
        this.active = (String) EmployeeDetail.get("active");
        this.inactive = (String) EmployeeDetail.get("inactive");

        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        ShiftDAO shiftDAO = daoFactory.getShiftDAO();

        this.badge = badgeDAO.find(this.badgeId);
        this.shift = shiftDAO.find(this.shiftId);

    }

    /**
     * Returns the unique ID of the employee.
     *
     * @return the ID of the employee.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the department ID of the employee.
     *
     * @return the department ID of the employee.
     */
    public int getDepartment() {
        return departmentId;
    }

    /**
     * Returns the Badge object associated with the employee.
     *
     * @return the Badge of the employee.
     */
    public Badge getBadge() {
        return this.badge;
    }

    /**
     * Returns the Shift object assigned to the employee.
     *
     * @return the Shift of the employee.
     */
    public Shift getShift() {
        return this.shift;
    }

    /**
     * Returns a string representation of the Employee object in the format:
     * "ID #<id>: <lastName>, <firstName> <middleName> (#<badgeId>), Type: <employeeType>, Department: <department>, Active: <activeDate>".
     *
     * @return a string representation of the employee.
     */
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append("ID #").append(id).append(": ");
        s.append(lastName).append(", ").append(firstName);
        s.append(' ').append(middleName).append(" (#");
        s.append(badgeId).append("), ").append("Type: ");

        // Append employee type based on employeeTypeId
        switch(employeeTypeId) {
            case 1:
                s.append("Full-Time, ");
                break;
            case 0:
                s.append("Temporary / Part-Time, ");
                break;
            default:
                break;
        }

        s.append("Department: ");

        // Append department name based on departmentId
        switch(departmentId) {
            case 1:
                s.append("Assembly, ");
                break;
            case 2:
                s.append("Cleaning, ");
                break;
            case 3:
                s.append("Warehouse, ");
                break;
            case 4:
                s.append("Grinding, ");
                break;
            case 5:
                s.append("Hafting, ");
                break;
            case 6:
                s.append("Office, ");
                break;
            case 7:
                s.append("Press, ");
                break;
            case 8:
                s.append("Shipping, ");
                break;
            case 9:
                s.append("Tool and Die, ");
                break;
            case 10:
                s.append("Maintenance, ");
                break;
            default:
                break;
        }

        s.append("Active: ");

        // Format the active date
        String dateTimeString = active;
        DateTimeFormatter start = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // Format LocalDateTime using the defined formatter
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, start);
        String formattedDateTime = dateTime.format(formatter).toUpperCase();
        s.append(formattedDateTime);

        return s.toString();
    }
}

