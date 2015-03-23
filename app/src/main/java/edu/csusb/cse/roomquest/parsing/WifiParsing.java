package edu.csusb.cse.roomquest.parsing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.csusb.cse.roomquest.mapping.Hotspot;

public class WifiParsing {
    public static Hotspot parseWifiFile(InputStream input) {
        BufferedReader br = null;
        String line = "";
        String splitBy = ",";

        Hotspot wifi = new Hotspot();
        List<Hotspot> hotspotList = new ArrayList<Hotspot>();

        try {
            br = new BufferedReader(new InputStreamReader(input));
            while ((line = br.readLine()) != null) {

                String[] hotspots = line.split(splitBy);


                Hotspot hotspotObject = new Hotspot();


                hotspotObject.setXLocation(Integer.parseInt(hotspots[0]));
                hotspotObject.setYLocation(Integer.parseInt(hotspots[1]));
                hotspotObject.setBSSID(hotspots[2]);
                hotspotObject.setPower(hotspots[3]);
                hotspotList.add(hotspotObject);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return wifi;
    }

    public Hotspot parseWifiFile(String file) {
        try {
            return parseWifiFile(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
