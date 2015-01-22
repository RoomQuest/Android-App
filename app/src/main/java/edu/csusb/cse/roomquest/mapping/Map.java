package edu.csusb.cse.roomquest.mapping;

import java.util.ArrayList;

/**
 * Created by Michael on 1/22/2015.
 * A Map Represents a single viewable map
 */
public class Map {

    ArrayList<Point> points = new ArrayList<Point>();
    ArrayList<Node> nodes = new ArrayList<Node>();
    ArrayList<Path> paths = new ArrayList<Path>();
    ArrayList<Room> rooms = new ArrayList<Room>();

    /**
     * Represents a single point on the map for Rooms, Nodes, etc.
     * The coordinate system starts at the top left at (0,0) and continues proportionally with a
     * maximum value of (1,1) for that of a square map image. A rectangular map might have coordinates
     * (1,0.75) at the bottom right corner.
     */
    public static class Point{
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
        final float x,y;
    }

    /**
     * A Node connects paths together on the map.
     * Nodes can serve as waypoints since they contain coordinates on a map.
     */
    public static class Node {
        //TODO Make constructor
        long id;
        Point point;

        ArrayList<PathConnection> pathConnections;
        public static class PathConnection {
            public final Path path;
            public final Side side;

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
            INTERSECTION,ELEVATOR,STAIRS
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
    }

    public static class Room {
        //TODO Make constructor
        Point point;
        long id;

    }
}
