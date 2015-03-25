package edu.csusb.cse.roomquest.wifi_nav;

import edu.csusb.cse.roomquest.mapping.Floor;

/**
 * Created by Michael on 3/24/2015.
 * Represents a HotspotSignal sample at an x y coordinate
 */
public class LocationSample {
    public final float x,y;
    public final HotspotSignal hotspotSignal;
    public final Floor floor;

    public LocationSample(float x, float y, Floor floor, HotspotSignal hotspotSignal) {
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.hotspotSignal = hotspotSignal;
    }

    @Override
    public String toString() {
        return hotspotSignal + " (" + x + "," + y + ")";
    }
}
