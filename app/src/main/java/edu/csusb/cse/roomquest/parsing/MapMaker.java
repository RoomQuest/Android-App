package edu.csusb.cse.roomquest.parsing;


import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.csusb.cse.roomquest.mapping.Floor;
import edu.csusb.cse.roomquest.mapping.Map;
import edu.csusb.cse.roomquest.mapping.Room;


public class MapMaker {

    /**
     * Create a Map object
     * @param input The input to parse
     * @return a Map object
     */
	public static Map parseMapFile(InputStream input) {
        // Now using InputStream as input instead.
        // InputStreams can be files and web addresses.

        /*Map map = new Map();
        List<Room> mapList = new ArrayList<Room>();
		String line = null;
		String splitBy = ",";

        BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(input));

            //map.mapImageName = br.readLine();
			while((line = br.readLine()) != null) {
				//split row by comma
				String [] row = line.split(splitBy);
				//Create room object and enter csv information to Map object

                        mapList.add(new Room(
                                row[0], // Name
                                row[1], // Type
                                row[2], // Floor
                                Integer.parseInt(row[3]), // X
                                Integer.parseInt(row[4]) // Y
                        ));

			}
            // No need to print anymore
			//print values stored in Map
			//printMapList(mapList);

            map.rooms = (Room[]) mapList.toArray(new Room[0]);
            return map;

		}catch(Exception e) {
			e.printStackTrace();
        }finally {
			if(br != null) {
				try {
					br.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
        return map;
        */
        return null;
    }

    public static Map parseMapFolder(File folder, String name, String fullName) {

        List<Room> roomList = new ArrayList<Room>();
        Floor[] floors;

        // TODO decide on files or folders.

        // Setup BufferedReader to read lines.
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = null;
        try {
            if ((line = br.readLine())!= null) {
                String[] floorNames = line.split (",");
                floors = new Floor[floorNames.length];
                for (int i = 0; i < floorNames.length; i++) {
                    floors[i] = new Floor(floorNames[i]);
                }
            } else {
                return null;
            }
            // Read each line while there is one
            while((line = br.readLine()) != null) {
                // Split it up into elements
                String[] elements = line.split(",");
                // Make sure each element doesn't throw an exception
                try {
                    // Find appropriate floor

                    // Create and add a new Room.
                    roomList.add(new Room(
                            elements[0], // Name
                            elements[1], // Type
                            Floor.findFloorByName(floors, elements[2]), // Floor
                            Integer.parseInt(elements[3]), // X
                            Integer.parseInt(elements[4]) // Y
                    ));
                } catch (Exception e) {
                    // Well, looks like someone screwed up that line in the CSV, I can't read it!
                    Log.e("MapMaker","Unable to parse a line in room.csv");
                }
            }
        // More irritating exception crap
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Make sure to close the Buffered Reader. We're not that stupid.
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Map(
                name,
                fullName,
                roomList.toArray(new Room[roomList.size()]),
                floorList.toArray(new Floor[floorList.size()])
        );
    }



}

