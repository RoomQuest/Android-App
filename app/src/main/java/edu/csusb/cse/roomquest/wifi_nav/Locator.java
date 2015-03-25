package edu.csusb.cse.roomquest.wifi_nav;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import java.util.Collection;
import java.util.List;

/**
 * Created by Michael on 3/25/15.
 */
public class Locator {

    private Context context;
    WifiManager wm;

    public Locator(Context context) {
        this.context = context;
    }

    public void setup() {
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                resultsReady();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    private void resultsReady() {

    }

    public void scan() {
        wm.startScan();
    }

    public LocationSample findLocation(LocationSample[] samples, HotspotSignal signal) {
        return null;

    }

    public static Collection<Result> sortSamples(LocationSample[] samples, HotspotSignal signal) {
        return null;
    }

    /**
     *
     * @param samples1 Compare to
     * @param samples2 Using
     * @return difference, -1 if no hotspots in common;
     */
    public static float getDifference(HotspotSignal[] samples1, HotspotSignal[] samples2) {
        if (samples1 == null || samples2 == null) {
            return -1;
        }
        int sumsqrs = 0;
        int matches = 0;
        for (HotspotSignal sample1 : samples1) {
            for (HotspotSignal sample2 : samples2) {
                if (sample1.bssid.equals(sample2.bssid)) {
                    matches++;
                    int difference = sample1.dbm - sample2.dbm;
                    sumsqrs += difference * difference;
                    break;
                } else {
                    sumsqrs += sample1.dbm * sample1.dbm;
                }
            }
        }
        if (matches == 0) {
            return -1;
        } else {
            return (float) sumsqrs / matches;
        }
    }

    public class Result {
        public LocationSample LocationSample;
        public float difference;
    }

    public interface OnLocationFoundListner {
        public void locationFound(LocationSample sample);
    }
}
