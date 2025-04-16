package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp25.EventType;
import edu.jsu.mcis.cs310.tas_sp25.Punch;
import edu.jsu.mcis.cs310.tas_sp25.ScheduleOverride;
import edu.jsu.mcis.cs310.tas_sp25.Shift;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.TemporalAdjusters;

/**
 * Utility class for DAO-related functions. This class provides static methods
 * for handling common data access operations such as formatting punch lists,
 * calculating worked minutes, and determining absenteeism rates.
 */
public final class DAOUtility {
    
    /**
     * Converts a list of Punch objects into a JSON formatted string.
     * 
     * @param dailypunchlist The list of Punch objects.
     * @return A JSON formatted string representing the punch data.
     */
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        
        for (Punch punch : dailypunchlist) {
            HashMap<String, String> punchData = new HashMap<>();
            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", punch.getBadgeId());
            punchData.put("terminalid", String.valueOf(punch.getTerminalid()));
            punchData.put("punchtype", punch.getPunchtype().toString());
            punchData.put("adjustmenttype", punch.getAdjustmentType().toString());
            punchData.put("originaltimestamp", punch.toString());
            punchData.put("adjustedtimestamp", punch.adjustedToString());
            jsonData.add(punchData);
        }
        
        return Jsoner.serialize(jsonData);
    }

    /**
     * Calculates the total minutes worked from a list of punches, taking into account
     * lunch deductions and shift timings.
     * 
     * @param dailypunchlist The list of Punch objects representing the daily punches.
     * @param shift The associated Shift object.
     * @return The total worked minutes as an integer.
     */
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {
        LocalDateTime shiftStart = null;
        LocalDateTime shiftStop = null;
        boolean lunchDeductible = false;
        long totalWorkedMinutes = 0;
        
        for (Punch punch : dailypunchlist) {
            EventType eventType = punch.getPunchtype();
            LocalDateTime punchTimestamp = punch.getAdjustedTimestamp();
            
            switch (eventType) {
                case CLOCK_IN:
                    shiftStart = punchTimestamp;
                    shiftStop = null; 
                    lunchDeductible = false; 
                    break;
                case CLOCK_OUT:
                    shiftStop = punchTimestamp;
                    break;
                case TIME_OUT:
                    shiftStop = null;
                    break;
                default:
                    break;
            }
            
            if (shiftStart != null && shiftStop != null) {
                long shiftDurationMinutes = Duration.between(shiftStart, shiftStop).toMinutes();
                
                if (shiftDurationMinutes - shift.getLunchDuration().toMinutes() > shift.getLunchThreshold()) {
                    lunchDeductible = true;
                }
                
                if (eventType == EventType.CLOCK_OUT && lunchDeductible) {
                    long lunchDurationMinutes = shift.getLunchDuration().toMinutes();
                    totalWorkedMinutes += Math.max(shiftDurationMinutes - lunchDurationMinutes, 0);
                } else {
                    totalWorkedMinutes += shiftDurationMinutes;
                }
                
                shiftStart = null;
                shiftStop = null;
                lunchDeductible = false;
            }
        }
        
        return (int) totalWorkedMinutes;
    }
    
    /**
     * Calculates absenteeism percentage based on worked and scheduled minutes.
     * 
     * @param punchList The list of punches for the pay period.
     * @param shift The associated Shift object.
     * @return The absenteeism percentage as a BigDecimal.
     */
    public static BigDecimal calculateAbsenteeism(ArrayList<Punch> punchList, Shift shift) {
        if (punchList == null || punchList.isEmpty() || shift == null) {
            return BigDecimal.ZERO;
        }
        
        long totalWorkedMinutes = calculateTotalMinutes(punchList, shift);
        long totalScheduledMinutes = getScheduledMinutes(shift, punchList);
        
        if (totalScheduledMinutes <= 0) {
            return BigDecimal.ZERO;
        }
        
        return new BigDecimal(totalScheduledMinutes - totalWorkedMinutes)
                .divide(new BigDecimal(totalScheduledMinutes), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
    }
    
    /**
     * Calculates the total scheduled minutes for a pay period.
     * 
     * @param shift The Shift object containing work schedule details.
     * @param punchList The list of punches to determine the date range.
     * @return The total scheduled minutes for the pay period.
     */

     /*
    private static long getScheduledMinutes(Shift shift, ArrayList<Punch> punchList) {
        LocalDate startDate = punchList.get(0).getAdjustedTimestamp().toLocalDate()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = startDate.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        
        long workingDays = 0;
        long shiftDurationMinutes = shift.getShiftDuration().toMinutes();
        long lunchDurationMinutes = shift.getLunchDuration().toMinutes();
        long scheduledMinutes = shiftDurationMinutes - lunchDurationMinutes;
        
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (shift.isWorkDay(date.getDayOfWeek())) {
                workingDays++;
            }
        }
        
        return workingDays * scheduledMinutes;
    } */

    private static long getScheduledMinutes(Shift shift, ArrayList<Punch> punchList) {

        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        ScheduleOverrideDAO sDAO = daoFactory.getScheduleOverrideDAO();
        ShiftDAO shiftDAO = daoFactory.getShiftDAO();
        Shift tempShift1 = shiftDAO.find(5);
        System.err.println(tempShift1);
        Shift tempShift2 = shiftDAO.find(6);
        String tempBadgeId = punchList.get(1).getBadgeId();
        String tempBadgeId2 = null;

        long overrideDuration = 0L;

        ArrayList<ScheduleOverride> slist = sDAO.list();

        System.err.println(slist);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        LocalDate startDate = punchList.get(0).getAdjustedTimestamp().toLocalDate()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = startDate.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));

        
        for (ScheduleOverride s : slist) {
            LocalDate tempDateStart = LocalDate.parse(s.getStart(), format);
            //LocalDate tempDateEnd = LocalDate.parse(s.getEnd(), format);
            tempBadgeId2 = s.getBadgeId();
            if (startDate.isEqual(tempDateStart)) {
                if (s.getDailyScheduleId() == 5) {
                    if (tempBadgeId2 == null || tempBadgeId == tempBadgeId2) {
                    //overrideDuration = tempShift1.getShiftDuration().toMinutes();
                    overrideDuration = shift.getShiftDuration().toMinutes() - shift.getLunchDuration().toMinutes();
                    System.err.println("SHIFT DURATION TEST: " + overrideDuration);
                    }
                }
                if (s.getDailyScheduleId() == 6) {
                    if (tempBadgeId2 == null || tempBadgeId == tempBadgeId2) {
                        //overrideDuration = tempShift1.getShiftDuration().toMinutes();
                        overrideDuration = 60L;
                        System.err.println("SHIFT DURATION TEST: " + overrideDuration);
                        }
                }
            }
        }
        
        long workingDays = 0;
        long shiftDurationMinutes = shift.getShiftDuration().toMinutes();
        long lunchDurationMinutes = shift.getLunchDuration().toMinutes();
        long scheduledMinutes = shiftDurationMinutes - lunchDurationMinutes;
        
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (shift.isWorkDay(date.getDayOfWeek())) {
                /* 
                for (ScheduleOverride s : slist) {
                    LocalDate tempDateStart = LocalDate.parse(s.getStart(), format);
                    LocalDate tempDateEnd = LocalDate.parse(s.getEnd(), format);
                    System.err.println("SCHEDULE TEST: " + tempDateStart + " " + tempDateEnd + " " + date);

                    if (date.isAfter(tempDateStart) && date.isBefore(tempDateEnd)) {
                        if (date.getDayOfWeek().ordinal() == s.getDay()) {
                            System.err.println("AAAAAAAAAAAAHHHHHHHH");
                        }
                    }
                    
                }*/


                workingDays++;
            }
        }
        
        return (workingDays * scheduledMinutes) - overrideDuration;
    }
    
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");
    private static final DateTimeFormatter ADJUSTED_FORMATTER = 
        DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");
    
    
    /** 
     * @param punchlist
     * @param shift
     * @return String
     */
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift shift) {
        // Create list to hold punch data
        ArrayList<Map<String, String>> jsonPunchList = new ArrayList<>();
        
        // Process each punch and add to jsonPunchList
        for (Punch punch : punchlist) {
            Map<String, String> punchData = new LinkedHashMap<>();
            
            // Format timestamps according to expected format
            String originalTimestamp = punch.getOriginaltimestamp().format(TIMESTAMP_FORMATTER);
            String adjustedTimestamp = punch.getAdjustedTimestamp().format(ADJUSTED_FORMATTER);
            
            // Put fields in exact expected order
            punchData.put("originaltimestamp", originalTimestamp.toUpperCase());
            punchData.put("badgeid", punch.getBadge().getId());
            punchData.put("adjustedtimestamp", adjustedTimestamp.toUpperCase());
            
            // Handle adjustment type formatting
            String adjustmentType = punch.getAdjustmentType().toString();
            if ("NONE".equals(adjustmentType)) {
                adjustmentType = "None";
            }
            punchData.put("adjustmenttype", adjustmentType);
            
            punchData.put("terminalid", String.valueOf(punch.getTerminalid()));
            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("punchtype", punch.getPunchtype().toString());
            
            jsonPunchList.add(punchData);
        }
        
        // Calculate totals
        int totalMinutes = calculateTotalMinutes(punchlist, shift);
        BigDecimal absenteeism = calculateAbsenteeism(punchlist, shift);
        
        // Create final data structure with specific field order
        Map<String, Object> jsonData = new LinkedHashMap<>();
        jsonData.put("absenteeism", String.format("%.2f%%", absenteeism));
        jsonData.put("totalminutes", totalMinutes);
        jsonData.put("punchlist", jsonPunchList);
        
        // Serialize to JSON
        try {
            return Jsoner.serialize(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
    
}
