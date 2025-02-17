package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.*;

public class Main {

    public static void main(String[] args) {
        
        // test database connectivity; get DAO

        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        PunchDAO punchDAO = daoFactory.getPunchDAO();
        
        // find badge

        Badge b = badgeDAO.find("C4F37EFF");

        Punch p = punchDAO.find(147);

        Punch p1 = punchDAO.find(3433);
        
        // output should be "Test Badge: #C4F37EFF (Welch, Travis C)"
        
        System.err.println("Test Badge: " + b.toString());
        System.err.println("Test Punch: " + p.toString());
        System.err.println("Test Punch: " + p1.toString());

    }

}


// Marc Compton