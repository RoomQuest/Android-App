package edu.csusb.cse.roomquest.wifi_nav;

import java.util.Arrays;

import edu.csusb.cse.roomquest.mapping.Floor;

/**
 * Created by Michael on 3/24/2015.
 * Represents all the HotspotSignals sampled at an x y coordinate and floor.
 */
public class LocationSample {
    public final float x,y;
    public final HotspotSignal[] hotspotSignals;
    public final Floor floor;

    public LocationSample(float x, float y, Floor floor, HotspotSignal[] hotspotSignals) {
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.hotspotSignals = hotspotSignals;
    }

    @Override
    public String toString() {
        return Arrays.toString(hotspotSignals) + " (" + x + "," + y + ")";
    }
}
