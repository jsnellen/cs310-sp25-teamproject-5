package edu.jsu.mcis.cs310.tas_sp25;

public class Punch {

    private final String id, timestamp;

    public Punch(String id, String timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append('#').append(id).append(' ');
        s.append('(').append(timestamp).append(')');

        return s.toString();

    }
}
