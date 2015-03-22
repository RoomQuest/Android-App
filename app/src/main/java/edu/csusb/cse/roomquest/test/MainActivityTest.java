package edu.csusb.cse.roomquest.test;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import edu.csusb.cse.roomquest.R;
import edu.csusb.cse.roomquest.mapping.Map;
import edu.csusb.cse.roomquest.parsing.MapMaker;
import edu.csusb.cse.roomquest.downloader.Spot;
import edu.csusb.cse.roomquest.ui.FloorSelectorView;
import edu.csusb.cse.roomquest.ui.MapView;

/**
 * Created by Michael on 3/15/2015.
 */
public class MainActivityTest extends ActionBarActivity {

    private static final String TAG = MainActivityTest.class.toString();
    // splash UI elements
    private ProgressBar progress;
    private ImageView logo;

    // UI elements
    DrawerLayout navDrawer;
    MapView mapView;
    ListView mapListView;
    private FloorSelectorView floorsView;

    // Data
    Map[] maps;
    private Map map;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    // State info
    private boolean showMenu = false;

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
        getSupportActionBar().hide();

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
                /*for (i = 0; i <= 100; i++) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progress.setProgress(i);
                        }
                    });
                    Thread.sleep(50);
                }*/
                // Load maps
                Log.d(TAG,"downloading maps");
                Spot.fetch();
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

    /**
     * Show the main UI
     */
    private void loadMapUi() {
        if (maps == null || maps.length == 0) {
            setContentView(R.layout.unable_to_load);
            return;
        }
        setContentView(R.layout.activity_main);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Find views
        navDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mapListView = (ListView) findViewById(R.id.map_list);
        mapView = (MapView) findViewById(R.id.map);
        floorsView = (FloorSelectorView) findViewById(R.id.floor_view);
        // set up behavior
        mapListView.setAdapter(new ArrayAdapter<Map>(this,android.R.layout.simple_list_item_1,maps));
        mapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map = (Map)parent.getItemAtPosition(position);
                mapView.loadMap((Map) parent.getItemAtPosition(position), map.floors[0]);
                floorsView.setFloors(map.floors,0);
                navDrawer.closeDrawer(mapListView);
            }
        });
        mapListView.addHeaderView(getLayoutInflater().inflate(R.layout.map_list_header,mapListView,false),null,false);

        // set up drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, navDrawer, R.string.app_name, R.string.hello_world) {
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (map != null)
                    getSupportActionBar().setTitle(map.fullName);
                else
                    getSupportActionBar().setTitle("Select Building");
                syncState();
            }
            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(getTitle());
                syncState();
            }
            @Override
            public void onDrawerStateChanged(int state) {
                super.onDrawerStateChanged(state);
                if (state == DrawerLayout.STATE_DRAGGING) {
                    showMenu(false);
                } else if (state == DrawerLayout.STATE_IDLE) {
                    if (!navDrawer.isDrawerOpen(mapListView))
                        showMenu(true);
                }
            }
        };
        navDrawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        navDrawer.openDrawer(mapListView);
        actionBarDrawerToggle.syncState();

        // set up floor list selector
        floorsView.setOnIndexChangeListener(new FloorSelectorView.OnIndexChangeListener() {
            @Override
            public void onIndexChange(int index, int oldIndex) {
                mapView.loadMap(map,map.floors[index]);
                Log.d(TAG,"index changed");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return showMenu;
    }

    private void showMenu(boolean show) {
        showMenu = show;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return showMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        if (actionBarDrawerToggle != null) {
            actionBarDrawerToggle.onConfigurationChanged(config);
        }
    }

}
