package edu.jsu.mcis.cs310.tas_sp25;

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
        s.append(' ').append(middleName).append(" (");
        s.append(badgeId).append(") ");

        return s.toString();

        /*  EXPECTED VALUE
            "ID #14: Donaldson, Kathleen C (#229324A4), Type: Full-Time, Department: Press, Active: 02/02/2017"
        */
    }

}

