package edu.csusb.cse.roomquest.mapping;

/**
 * Created by Michael on 3/12/2015.
 */
public class Floor {
    private final String name;

    public Floor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Floor findFloorByName(Floor[] floors, String name) {
        for (Floor floor : floors) {
            if (floor.getName().contains(name)) {
                return floor;
            }
        }
        return null;
    }

}
