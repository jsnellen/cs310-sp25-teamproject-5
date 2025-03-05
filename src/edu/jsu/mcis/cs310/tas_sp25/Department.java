package edu.jsu.mcis.cs310.tas_sp25;

import java.util.HashMap;

public class Department {
    private final String description;
    private final int id, terminalid;

    public Department(HashMap<String, String> DepartmentDetail) {

        this.id = Integer.valueOf((String) DepartmentDetail.get("id"));
        this.description = (String) DepartmentDetail.get("description");
        this.terminalid = Integer.valueOf((String) DepartmentDetail.get("terminalid"));
        
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    
    public int getTerminalid(){
        return terminalid;
    }
        @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append('#').append(id).append(' ');
        s.append('(').append(description).append(')');
        s.append(", Terminal ID: ");
        s.append(terminalid);

        return s.toString();

    }
}


