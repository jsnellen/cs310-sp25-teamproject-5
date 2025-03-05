package edu.jsu.mcis.cs310.tas_sp25;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Employee {

    private final int id, employeeTypeId, departmentId, shiftId;
    private final String badgeId, firstName, middleName, lastName, active, inactive;

    public Employee(HashMap<String, String> EmployeeDetail) {

        this.id = Integer.valueOf((String) EmployeeDetail.get("id"));;
        this.employeeTypeId = Integer.valueOf((String) EmployeeDetail.get("employeeTypeId"));
        this.departmentId = Integer.valueOf((String) EmployeeDetail.get("departmentId"));
        this.shiftId = Integer.valueOf((String) EmployeeDetail.get("shiftId"));
        this.badgeId = (String) EmployeeDetail.get("badgeId");
        this.firstName = (String) EmployeeDetail.get("firstName");
        this.middleName = (String) EmployeeDetail.get("middleName");
        this.lastName = (String) EmployeeDetail.get("lastName");
        this.active = (String) EmployeeDetail.get("active");
        this.inactive = (String) EmployeeDetail.get("inactive");

    }

    public int getId() {
        return id;
    }


    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append("ID #").append(id).append(": ");
        s.append(lastName).append(", ").append(firstName);
        s.append(' ').append(middleName).append(" (#");
        s.append(badgeId).append("), ").append("Type: ");

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

        String dateTimeString = active;
        // Define the desired format
        DateTimeFormatter start = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // Format LocalDateTime using the defined formatter
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, start);
        String formattedDateTime = dateTime.format(formatter).toUpperCase();
        s.append(formattedDateTime);

        return s.toString();

        /*  EXPECTED VALUE
            "ID #14: Donaldson, Kathleen C (#229324A4), Type: Full-Time, Department: Press, Active: 02/02/2017"
        */
    }

}

