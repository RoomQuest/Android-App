package edu.csusb.cse.roomquest;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MapMaker {
    public Map parseMapFile(String file) {
        try {
            return parseMapFile(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
	public static Map parseMapFile(InputStream input) {
        // Now using InputStream instead.
        // InputStreams can be files and web addresses.
		//String mapFileToRead = "mapfiles/map.csv";
		BufferedReader br = null;
		String line = "";
		String splitBy = ",";

        Map map = new Map();
		List<Room> mapList = new ArrayList<Room>();
		
		try {
			br = new BufferedReader(new InputStreamReader(input));

            map.mapImageName = br.readLine();
			while((line = br.readLine()) != null) {
				//split comma
				String [] maps = line.split(splitBy);
				
				//Create map object to store values
				Room mapObject = new Room();
				
				//Enter csv information to Map object
				mapObject.setName(maps[0]);
				mapObject.setRoomType(maps[1]);
				mapObject.setXCoord(Integer.parseInt(maps[2]));
				mapObject.setYCoord(Integer.parseInt(maps[3]));
				
				//add mapObject to the list
				mapList.add(mapObject);
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
    }
    // Don't need anymore
/*	public void printMapList(List<Room> mapListToPrint) {
		for(int i = 0; i < mapListToPrint.size(); i++) {
			System.out.println("Maps [ Name: " + mapListToPrint.get(i).getName()
			+ " RoomType: " + mapListToPrint.get(i).getRoomType() + " XCoord: "
			+ mapListToPrint.get(i).getXCoord() + " YCoord: " + mapListToPrint.get(i).getYCoord()
			+ " ]");
		}
	}
*/
}

