package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.DAOFactory;
import edu.jsu.mcis.cs310.tas_sp25.dao.ShiftDAO;

public class Main {

    public static void main(String[] args) {
        
        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        ShiftDAO shiftDAO = daoFactory.getShiftDAO();
        Shift tempShift = shiftDAO.find(5);
        System.err.println(tempShift);
    }

}

// Triston Ballard
// Marc Compton
// Jakolbe Brewster
// Maverick Burchfield
// Ralph Wheeler
