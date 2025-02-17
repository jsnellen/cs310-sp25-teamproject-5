package edu.jsu.mcis.cs310.tas_sp25;

public class Punch {

    private final String id, terminalid, badgeid, timestamp, eventtypeid;

    public Punch(String id, String terminalid, String badgeid, String timestamp, String eventtypeid) {
        this.id = id;
        this.terminalid = terminalid;
        this.badgeid = badgeid;
        this.timestamp = timestamp;
        this.eventtypeid = eventtypeid;
    }

    public String getId() {
        return id;
    }

    public String getTerminalId() {
        return terminalid;
    }

    public String getBadgeId() {
        return badgeid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getEventTypeId() {
        return eventtypeid;
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append('#').append(id).append(' ');
        s.append('(').append(timestamp).append(')');

        return s.toString();

    }
}
