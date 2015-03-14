package edu.csusb.cse.roomquest.mapping;

public class Room {

    public String getName() {
        return name;
    }

    // Variables regarding a room
    private final String name, type;
    private final Floor floor;
    float x, y;

    public Room(String name, String type, Floor floor, int x, int y) {
        this.name = name;
        this.type = type;
        this.floor = floor;
        this.x = x;
        this.y = y;
    }
}
