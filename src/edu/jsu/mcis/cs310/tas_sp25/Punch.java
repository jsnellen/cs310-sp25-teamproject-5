package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.BadgeDAO;
import edu.jsu.mcis.cs310.tas_sp25.dao.DAOFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Punch {

    private final int id, terminalid, eventtypeid;
    private final String badgeid, timestamp;
    private final LocalDateTime ots;
    private final Badge badge;
    private final EventType eventtype;

    public Punch(HashMap<String, String> PunchDetail) {

        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        this.id = Integer.valueOf((String) PunchDetail.get("id"));
        this.terminalid = Integer.valueOf((String) PunchDetail.get("terminalid"));
        this.badgeid = (String) PunchDetail.get("badgeid");
        this.timestamp = (String) PunchDetail.get("timestamp");
        this.eventtypeid = Integer.valueOf((String) PunchDetail.get("eventtypeid"));
        this.badge = badgeDAO.find(badgeid);
        this.ots = LocalDateTime.now();

        
        switch(eventtypeid) {
            case 0 -> this.eventtype = EventType.CLOCK_OUT;
            case 1 -> this.eventtype = EventType.CLOCK_IN;
            case 2 -> this.eventtype = EventType.TIME_OUT;
            default -> {
                this.eventtype = EventType.CLOCK_OUT;
            }
        }
    }


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


    public int getId() {
        return id;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public String getBadgeId() {
        return badgeid;
    }

    public Badge getBadge() {
        return badge;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getOriginaltimestamp() {
        return ots;
    }

    public int getEventTypeId() {
        return eventtypeid;
    }

    public EventType getPunchtype() {
        return eventtype;
    }

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

}
