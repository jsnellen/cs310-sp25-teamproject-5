package edu.jsu.mcis.cs310.tas_sp25;

import java.util.HashMap;

/**
 * The Department class represents a department in the time and attendance system.
 * It contains details such as the department's ID, description, and terminal ID.
 */
public class Department {

    /** The description of the department. */
    private final String description;

    /** The unique identifier of the department. */
    private final int id;

    /** The terminal ID associated with the department. */
    private final int terminalid;

    /**
     * Constructs a Department object using the details provided in a HashMap.
     *
     * @param DepartmentDetail a HashMap containing department details like id, description, and terminalid.
     */
    public Department(HashMap<String, String> DepartmentDetail) {

        this.id = Integer.valueOf((String) DepartmentDetail.get("id"));
        this.description = (String) DepartmentDetail.get("description");
        this.terminalid = Integer.valueOf((String) DepartmentDetail.get("terminalid"));
        
    }

    /**
     * Returns the unique identifier of the department.
     *
     * @return the ID of the department.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the description of the department.
     *
     * @return the description of the department.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the terminal ID associated with the department.
     *
     * @return the terminal ID of the department.
     */
    public int getTerminalid(){
        return terminalid;
    }

    /**
     * Returns a string representation of the Department object in the format:
     * "#<id> (<description>), Terminal ID: <terminalid>".
     *
     * @return a string representation of the department.
     */
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        // Append department details to the string builder
        s.append('#').append(id).append(' ');
        s.append('(').append(description).append(')');
        s.append(", Terminal ID: ");
        s.append(terminalid);

        return s.toString();

    }
}
