package edu.jsu.mcis.cs310.tas_sp25;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Employee {

    private final int id, employeeTypeId, departmentId, shiftId;
    private final String badgeId, firstName, middleName, lastName, active, inactive;

    public Employee(int id, String badgeId, String firstName, String middleName, String lastName, int employeeTypeId, int departmentId, int shiftId, String active, String inactive) {
        this.id = id;
        this.employeeTypeId = employeeTypeId;
        this.departmentId = departmentId;
        this.shiftId = shiftId;
        this.badgeId = badgeId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.active = active;
        this.inactive = inactive;
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

