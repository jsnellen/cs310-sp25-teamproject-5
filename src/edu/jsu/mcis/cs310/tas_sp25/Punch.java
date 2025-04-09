/**
 * Represents a Punch record in the time and attendance system.
 * A Punch records an employee's clock-in, clock-out, or time-out event.
 */
package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.BadgeDAO;
import edu.jsu.mcis.cs310.tas_sp25.dao.DAOFactory;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Punch {

    private final int id, terminalid, eventtypeid;
    private final String badgeid, timestamp;
    private final LocalDateTime ots;
    private final Badge badge;
    private final EventType eventtype;
    private PunchAdjustmentType adjustmenttype;
    private LocalDateTime adjustedtimestamp;
    private Shift shift;

    /**
     * Constructs a Punch object using details from a database HashMap.
     *
     * @param PunchDetail a HashMap containing punch attributes retrieved from the database
     */
    public Punch(HashMap<String, String> PunchDetail) {
        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        this.id = Integer.valueOf(PunchDetail.get("id"));
        this.terminalid = Integer.valueOf(PunchDetail.get("terminalid"));
        this.badgeid = PunchDetail.get("badgeid");
        this.timestamp = PunchDetail.get("timestamp");
        this.eventtypeid = Integer.valueOf(PunchDetail.get("eventtypeid"));
        this.badge = badgeDAO.find(badgeid);
        DateTimeFormatter start = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.ots = LocalDateTime.parse(PunchDetail.get("timestamp"), start);

        switch(eventtypeid) {
            case 0 -> this.eventtype = EventType.CLOCK_OUT;
            case 1 -> this.eventtype = EventType.CLOCK_IN;
            case 2 -> this.eventtype = EventType.TIME_OUT;
            default -> this.eventtype = EventType.CLOCK_OUT;
        }
    }

    /**
     * Constructs a new Punch for live punch data.
     *
     * @param i terminal ID
     * @param badge employee badge
     * @param clockIn type of punch event
     */
    public Punch(int i, Badge badge, EventType clockIn) {
        this.id = 0;
        this.terminalid = i;
        this.badge = badge;
        this.eventtypeid = clockIn.ordinal();
        this.badgeid = badge.getId();
        this.timestamp = null;
        this.ots = LocalDateTime.now();
        this.eventtype = clockIn;
    }

    /** @return punch ID */
    public int getId() {
        return id;
    }

    /** @return terminal ID */
    public int getTerminalid() {
        return terminalid;
    }

    /** @return badge ID as String */
    public String getBadgeId() {
        return badgeid;
    }

    /** @return Badge object */
    public Badge getBadge() {
        return badge;
    }

    /** @return raw timestamp string */
    public String getTimestamp() {
        return timestamp;
    }

    /** @return original LocalDateTime timestamp */
    public LocalDateTime getOriginaltimestamp() {
        System.err.println(ots);
        return ots;
    }

    /** @return event type ID */
    public int getEventTypeId() {
        return eventtypeid;
    }

    /** @return EventType of the punch */
    public EventType getPunchtype() {
        return eventtype;
    }

    /** @return adjustment type for punch */
    public PunchAdjustmentType getAdjustmentType(){
        return adjustmenttype;
    }

    /** @return adjusted timestamp if any */
    public LocalDateTime getAdjustedTimestamp(){
        return adjustedtimestamp;
    }

    /**
     * Adjusts the punch timestamp based on shift rules (start, stop, lunch, rounding).
     *
     * @param shift the shift to apply adjustment rules from
     */
    public void adjust(Shift shift) {
         adjustmenttype = null;
        adjustedtimestamp = null;
        boolean isWeekend = false;
        
        DayOfWeek day = getOriginaltimestamp().getDayOfWeek();
        
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            isWeekend = true;
        }

        int interval = shift.getRoundInterval();
        int grace = shift.getGracePeriod();
        int dock = shift.getDockPenalty();

        LocalTime shiftStart = shift.getShiftStart();
        LocalTime shiftStop = shift.getShiftStop();
        LocalTime lunchStart = shift.getLunchStart();
        LocalTime lunchStop = shift.getLunchStop();

        LocalDateTime shiftStartDateTime = getOriginaltimestamp().with(shiftStart);
        LocalDateTime shiftStopDateTime = getOriginaltimestamp().with(shiftStop);
        LocalDateTime lunchStartDateTime = getOriginaltimestamp().with(lunchStart);
        LocalDateTime lunchStopDateTime = getOriginaltimestamp().with(lunchStop);

        LocalDateTime shiftStartInterval = shiftStartDateTime.minusMinutes(interval);
        LocalDateTime shiftStartGrace = shiftStartDateTime.plusMinutes(grace);
        LocalDateTime shiftStartDock = shiftStartDateTime.plusMinutes(dock);

        LocalDateTime shiftStopInterval = shiftStopDateTime.plusMinutes(interval);
        LocalDateTime shiftStopGrace = shiftStopDateTime.minusMinutes(grace);
        LocalDateTime shiftStopDock = shiftStopDateTime.minusMinutes(dock);

        if (eventtype == EventType.CLOCK_IN) {
            if (getOriginaltimestamp().isAfter(shiftStartInterval.minusSeconds(1)) && getOriginaltimestamp().isBefore(shiftStartDateTime)) {
                adjustedtimestamp = shiftStartDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_START;
                
            } else if (getOriginaltimestamp().isAfter(shiftStartDateTime) && getOriginaltimestamp().isBefore(shiftStartGrace)) {
                adjustedtimestamp = shiftStartDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_START;
                
            } else if (getOriginaltimestamp().isAfter(shiftStartGrace) && getOriginaltimestamp().isBefore(shiftStartDock.plusSeconds(1))) {
                adjustedtimestamp = shiftStartDock;
                adjustmenttype = PunchAdjustmentType.SHIFT_DOCK;
                
            } else if (!isWeekend && getOriginaltimestamp().isAfter(lunchStartDateTime) && getOriginaltimestamp().isBefore(lunchStopDateTime)) {
                adjustedtimestamp = lunchStopDateTime;
                adjustmenttype = PunchAdjustmentType.LUNCH_STOP;
                
            }
        } else if (eventtype == EventType.CLOCK_OUT || eventtype == EventType.TIME_OUT) {
            if (getOriginaltimestamp().isAfter(shiftStopDateTime) && getOriginaltimestamp().isBefore(shiftStopInterval.plusSeconds(1))) {
                adjustedtimestamp = shiftStopDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_STOP;
                
            } else if (getOriginaltimestamp().isBefore(shiftStopDateTime) && getOriginaltimestamp().isAfter(shiftStopGrace)) {
                adjustedtimestamp = shiftStopDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_STOP;
                
            } else if (getOriginaltimestamp().isBefore(shiftStopGrace) && getOriginaltimestamp().isAfter(shiftStopDock.minusSeconds(1))) {
                adjustedtimestamp = shiftStopDock;
                adjustmenttype = PunchAdjustmentType.SHIFT_DOCK;
                
            } else if (!isWeekend && getOriginaltimestamp().isAfter(lunchStartDateTime) && getOriginaltimestamp().isBefore(lunchStopDateTime)) {
                adjustedtimestamp = lunchStartDateTime;
                adjustmenttype = PunchAdjustmentType.LUNCH_START;
            }
        }

        if (adjustmenttype == null) {
            int adjustMinute;
            int minutes = getOriginaltimestamp().getMinute();

            if((minutes % interval) < (interval / 2)){
                adjustMinute = (Math.round(minutes / interval) * interval);
            }else{
                adjustMinute = (Math.round(minutes / interval) * interval) + interval;
            }

            if ((adjustMinute / 60) == 1) {
                adjustmenttype = PunchAdjustmentType.INTERVAL_ROUND;
                adjustedtimestamp = getOriginaltimestamp().withHour(getOriginaltimestamp().getHour() + 1).withMinute(0).withSecond(0).withNano(0);
            } else
            {
                adjustmenttype = PunchAdjustmentType.INTERVAL_ROUND;
                adjustedtimestamp = getOriginaltimestamp().withMinute(adjustMinute).withSecond(0).withNano(0);
            }
            if((getOriginaltimestamp().getMinute() == adjustedtimestamp.getMinute() ) && (getOriginaltimestamp().getHour() == adjustedtimestamp.getHour())){
                adjustedtimestamp = getOriginaltimestamp().withSecond(0).withNano(0);
                adjustmenttype = PunchAdjustmentType.NONE;
            }
        }
    }

    /**
     * Returns a string representation of the punch timestamp for display.
     *
     * @return formatted string of punch timestamp
     */
    @Override
    public String toString() {

         StringBuilder s = new StringBuilder();

        String dateTimeString = timestamp;
        // Define the desired format
        DateTimeFormatter start = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MM/dd/YYYY HH:mm:ss");

        // Format LocalDateTime using the defined formatter
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, start);
        String formattedDateTime = dateTime.format(formatter);

        s.append(formattedDateTime);

        return s.toString().toUpperCase();
    }

    /**
     * Returns a formatted string of the original punch for display.
     *
     * @return formatted original punch string
     */
    public String printOriginal() {
         StringBuilder s = new StringBuilder();
        String timestamp = this.getTimestamp();
        String badgeid = this.getBadgeId();
        int eventtypeid = this.getEventTypeId();

        // Define the desired format
        DateTimeFormatter start = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EE MM/dd/yyyy HH:mm:ss");

        // Format LocalDateTime using the defined formatter
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, start);
        String formattedDateTime = dateTime.format(formatter).toUpperCase();
        
        //This code needed to be commented out. We don't have to built this manually, the info we neeeded is already provided. (Ralph) 
        
        s.append('#').append(badgeid).append(' ');

        if (eventtypeid == 1) {
            s.append("CLOCK IN: ");
        } else if (eventtypeid == 0) {
            s.append("CLOCK OUT: ");
        } else {
            s.append("TIME OUT: ");
        }
        
        s.append(formattedDateTime);

        return s.toString();
    }

    /**
     * Returns a formatted string of the adjusted punch.
     *
     * @return formatted adjusted punch string
     */
    public String printAdjusted() {
        StringBuilder build = new StringBuilder();

        // Define the date and time format
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

        // Get the day of the week from the original timestamp
        DayOfWeek dayOfTheWeek = getOriginaltimestamp().getDayOfWeek();

        // Build the adjusted punch string
    
        build.append("#")
             .append(badge.getId()).append(" ")
             .append(eventtype).append(": ") 
             .append(dayOfTheWeek.name().substring(0, 3))
             .append(" ")
             .append(adjustedtimestamp.format(format)) 
             .append(" (").append(adjustmenttype).append(")");
    
        return build.toString();
    }

    /**
     * Returns a string with just day and time from the adjusted timestamp.
     *
     * @return formatted day and adjusted time string
     */
    public String adjustedToString() {
        StringBuilder build = new StringBuilder();
    
        // Define the date and time format
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    
        // Get the day of the week from the original timestamp
        DayOfWeek dayOfTheWeek = getOriginaltimestamp().getDayOfWeek();
    
        // Build the adjusted punch string
        
          build
             .append(dayOfTheWeek.name().substring(0, 3))
             .append(" ")
             .append(adjustedtimestamp.format(format));
             
        return build.toString();
    }

    /**
     * Gets the number of minutes worked by comparing the punch and shift stop time.
     *
     * @param shift the shift configuration
     * @return number of minutes worked
     */

} 
