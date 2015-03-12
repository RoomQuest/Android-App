package edu.csusb.cse.roomquest.mapping_old;

import java.util.ArrayList;

/**
 * Created by Michael on 1/22/2015.
 * A Map provides directions from one node or room to another room.
 */
public class Map {



    // Simply the data associated with the map
    ArrayList<Node> nodes;// = new ArrayList<Node>();
    ArrayList<Path> paths;// = new ArrayList<Path>();
    ArrayList<Door> doors;// = new ArrayList<Door>();
    ArrayList<Room> rooms;// = new ArrayList<Room>();

    /**
     * Represents a single point on the map for Rooms, Nodes, etc.
     * The coordinate system starts at the top left at (0,0) and continues proportionally with a
     * maximum value of (1,1) for that of a square map image. A rectangular map might have the
     * coordinates (1,0.75) at the bottom right corner.
     */
    public static class Point{
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
        final float x,y;
        //TODO Image that is referenced with this point.
    }

    /**
     * A Node connects paths together on the map.
     * Nodes can serve as waypoints since they contain coordinates on a map.
     * Nodes also provide information on how to go from one path to another.
     */
    public static class Node {
        //TODO Make constructor
        long id;
        Point point;

        ArrayList<PathConnection> pathConnections;
        public static class PathConnection {
            public final Path path;
            public final Side side;
            public boolean AditionalInstructions;
            public String Instructions;

            /**
             * The side the path is connected to.
             * (used for calculating which side rooms are on etc.)
             */
            public static enum Side {
                START,END
            }
            public PathConnection(Path path, Side side) {
                this.path = path;
                this.side = side;
            }
        }
        public static enum Type {
            TEE,CROSS,ELEVATOR,STAIRS,CUSTOM_DIRECTIONS
        }
    }

    /**
     * A Path represents a connection between two nodes
     * Paths have a value length proportional to time
     */
    public static class Path {
        //TODO Make constructor
        long id;
        Node start,end;

        float length;
        public static class RoomPosition {
            public static enum Position {
                RIGHT,LEFT,START,END
            }
        }
    }

    /**
     * A Door represents a connection between a path and a room
     */
    public static class Door {

    }

    /**
     * A Room represents a room, yeah its that simple.
     * It also has a point that can be highlighted on the map.
     */
    public static class Room {
        //TODO Make constructor
        Point point;
        long id;
    }
}
