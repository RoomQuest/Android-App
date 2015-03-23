package edu.csusb.cse.roomquest.search;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.csusb.cse.roomquest.mapping.Map;
import edu.csusb.cse.roomquest.mapping.Room;
import edu.csusb.cse.roomquest.mapping.RoomAliases;

/**
 * Created by Michael on 3/22/2015.
 * Allows searching through a map.
 */
public class SearchMap {

    public static Result[] search(Map map, String query) {
        query = query.toLowerCase();
        String[] subqueries = query.split(" ");
        SortedSet<Result> results = new TreeSet<Result>();
        for (Room room : map.rooms) {
            // test if the query matches as it is
            if (room.getName().toLowerCase().contains(query)) {
                results.add(new Result(
                        room,
                        room.getName(),
                        room.getName().length() - query.length()
                ));
            } else {
                for (String q : subqueries) { // go though each sub query
                    int qLength = q.length();
                    if (room.getName().toLowerCase().contains(q)) {
                        results.add(new Result(
                                room,
                                room.getName(),
                                room.getName().length() - qLength
                        ));
                    }
                    if (room.getType().toLowerCase().contains(q)) {
                        results.add(new Result(
                                room,
                                room.getType(),
                                room.getType().length() - qLength
                        ));
                    }
                }
            }
        }
        for (RoomAliases alias : map.roomsAliases) {
            for (String name : alias.getAliases()) {
                if (name.toLowerCase().contains(query)) {
                    results.add(new SearchMap.Result(
                            alias.getRoom(),
                            name,
                            name.length() - query.length()
                    ));
                } else {
                    for (String q : subqueries) { // go though each sub query
                        if (name.toLowerCase().contains(q)) {
                            results.add(new SearchMap.Result(
                                    alias.getRoom(),
                                    name,
                                    name.length() - q.length()
                            ));
                        }
                    }
                }
            }
        }
        // TODO Alias functionality here:
        return results.toArray(new Result[results.size()]);
    }

    public static class Result implements Comparable<Result> {
        private final Room room;
        private final String name;
        private int difference;

        public Result(Room room, String name, int difference) {
            this.room = room;
            this.name = name;
            this.difference = difference;
        }

        public Room getRoom() {
            return room;
        }

        public String getName() {
            return name;
        }

        public int getDifference() {
            return difference;
        }

        public void setDifference(int difference) {
            this.difference = difference;
        }

        @Override
        public int compareTo (Result other) {
            int t = getDifference(), o = other.getDifference();
            return (t == o) ? 0 : (t > o) ? 1 : -1;
        }

        @Override
        public boolean equals (Object other) {
            try {
                return name == ((Result)other).name;
            } catch (ClassCastException e) {
                return false;
            }
        }
    }
}
