package edu.csusb.cse.roomquest.mapping;

import java.util.Objects;

public class Room {

    public String getName() {
        return name;
    }

    // Variables regarding a room
    private final String name;

    private final String type;

    private final Floor floor;
    private float x, y;

    public Room(String name, String type, Floor floor, float x, float y) {
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

    @Override
    public boolean equals(Object other) {
        try {
            return getName().equalsIgnoreCase(((Room) other).getName());
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return getName();
    }

}
