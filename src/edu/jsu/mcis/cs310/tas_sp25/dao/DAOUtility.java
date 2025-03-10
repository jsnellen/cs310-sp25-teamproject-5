package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp25.Punch;

/**
 * 
 * Utility class for DAOs.  This is a final, non-constructable class containing
 * common DAO logic and other repeated and/or standardized code, refactored into
 * individual static methods.
 * 
 */
public final class DAOUtility {
    
    public static String getPunchListAsJson(ArrayList<Punch> dailypunchlist) {
   
        // Create an ArrayList for the punch data
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        // Iterate throught the list of punches
        for (Punch punch : dailypunchlist) {
            // Create a Hashmap to store the data for the current punch
            HashMap<String, String> punchData = new HashMap<>();
       
            // Add the punch data to the Hashmap
            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", punch.getBadgeid());
            punchData.put("terminalid", String.valueOf(punch.getTerminalid()));
            punchData.put("punchtype", punch.getPunchtype().toString());
            punchData.put("adjustmenttype", punch.getAdjustmenttype().toString());
            punchData.put("originaltimestamp", punch.printOriginal());
            punchData.put("adjustedtimestamp", punch.printAdjusted());
       
            // Append the HashMap to the ArrayList
            jsonData.add(punchData);
        }
    
        // Serialize the ArrayList to a JSON String
        String json = Jsoner.serialize(jsonData);
    
        // Return the JSON String
        return json;
    
    }

}