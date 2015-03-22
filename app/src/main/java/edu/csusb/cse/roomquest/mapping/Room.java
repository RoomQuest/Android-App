package edu.csusb.cse.roomquest.mapping;

public class Room {

    public String getName() {
        return name;
    }

    // Variables regarding a room
    private final String name;

    private final String type;

    private final Floor floor;
    private float x, y;

    public Room(String name, String type, Floor floor, int x, int y) {
        this.name = name;
        this.type = type;
        this.floor = floor;
        this.x = x;
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public float getXCoord() {
        return x;
    }

    public float getYCoord() {
        return y;
    }

    public Floor getFloor() {
        return floor;
    }

}
