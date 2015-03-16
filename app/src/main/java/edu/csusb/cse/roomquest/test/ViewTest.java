package edu.csusb.cse.roomquest.test;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import edu.csusb.cse.roomquest.R;
import edu.csusb.cse.roomquest.mapping.Map;
import edu.csusb.cse.roomquest.parsing.MapMaker;
import edu.csusb.cse.roomquest.ui.MapView;

/**
 * Created by Michael on 3/15/2015.
 */
public class ViewTest extends ActionBarActivity {

    private static final String TAG = ViewTest.class.toString();
    // splash UI elements
    private ProgressBar progress;
    private ImageView logo;

    // UI elements
    DrawerLayout navDrawer;
    MapView mapView;
    ListView mapListView;

    // Data
    Map[] maps;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // add some fancy colors to the app switcher if its Lolipoop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(new ActivityManager.TaskDescription(
                    "Room Quest",
                    BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher),
                    getResources().getColor(R.color.csusb_blue)
            ));
        }
        loadMaps();
    }

    /**
     * Show the splash screen and load the Maps from the file system.
     */
    private void loadMaps() {
        setContentView(R.layout.splash);

        // Find UI
        progress = (ProgressBar) findViewById(R.id.progress);
        logo = (ImageView) findViewById(R.id.logo);
        new Thread(new InitialLoad()).start();
    }

    /**
     * Part of the loadMaps method.
     */
    private class InitialLoad implements Runnable {
        private int i;
        @Override
        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        progress.setVisibility(View.VISIBLE);
                    }
                });
                for (i = 0; i <= 100; i++) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progress.setProgress(i);
                        }
                    });
                    Thread.sleep(50);
                }
                // Load maps
                Log.d(TAG,"loading maps");
                maps = MapMaker.getMaps();
                Log.d(TAG,"Complete, " + ((maps == null) ? "null" : "not null"));
                runOnUiThread(new Runnable() {
                    public void run() {
                        progress.setVisibility(View.GONE);
                    }
                });
                Thread.sleep(2000);

                runOnUiThread(new Runnable() {
                    public void run() {
                        loadMapUi();
                    }
                });
            } catch (InterruptedException ignored) {}
        }
    }

    private void loadMapUi() {
        if (maps == null || maps.length == 0) {
            setContentView(R.layout.unable_to_load);
            return;
        }
        setContentView(R.layout.activity_main);
        // Find views
        navDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mapListView = (ListView) findViewById(R.id.map_list);
        mapView = (MapView) findViewById(R.id.map);
        // set up behavior
        navDrawer.openDrawer(mapListView);
        mapListView.setAdapter(new ArrayAdapter<Map>(this,android.R.layout.simple_list_item_1,maps));
        mapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mapView.loadMap((Map)parent.getItemAtPosition(position),((Map)parent.getItemAtPosition(position)).floors[0].getImageFile().getPath());
                navDrawer.closeDrawer(mapListView);
            }
        });
        mapListView.addHeaderView(getLayoutInflater().inflate(R.layout.map_list_header,mapListView,false),null,false);
    }
}
