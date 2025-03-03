package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        
        // test database connectivity; get DAO

        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        PunchDAO punchDAO = daoFactory.getPunchDAO();
        
        // find badge

        Badge b = badgeDAO.find("C4F37EFF");

        /******* TESTING PUNCH CLASS STUFF */

        Punch p = punchDAO.find(147);
        Punch p1 = punchDAO.find(3433);
        LocalDate ts = LocalDate.of(2018, Month.SEPTEMBER, 17);
        Badge b1 = badgeDAO.find("67637925");
        ArrayList<Punch> p2 = punchDAO.list(b1, ts);

        
        // output should be "Test Badge: #C4F37EFF (Welch, Travis C)"
        
        System.err.println("Test Badge: " + b.toString());
        System.err.println("Test Punch: " + p.toString());
        System.err.println("Test Punch: " + p1.printOriginal());

        for (Punch a : p2) {
            System.err.println(a.printOriginal());
        }

        /////****** END TESTING PUNCH CLASS STUFF */
        /// 
        /// 
        /// ***** EMPLOYEE TEST
        /// 
        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        
        Employee e = employeeDAO.find(14);
        System.err.println("Test Employee: " + e.toString());
    }

}

// Triston Ballard
// Marc Compton
// Jakolbe Brewster
// Maverick Burchfield
// Ralph Wheeler
