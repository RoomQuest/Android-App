package edu.csusb.cse.roomquest.wifi_nav;

import android.net.wifi.ScanResult;

/**
 * Created by Michael on 3/24/2015.
 */
public class HotspotSignal {
    public final String bssid;
    public final int dbm;

    /**
     * Represents signal from a bssid;
     * @param bssid
     * @param dbm
     */
    public HotspotSignal(String bssid, int dbm) {
        this.bssid = bssid;
        this.dbm = dbm;
    }

    public HotspotSignal(ScanResult sr) {
        this(sr.BSSID,sr.level);
    }

    @Override
    public String toString() {
        return bssid + " " + dbm + "dBm";
    }
}
