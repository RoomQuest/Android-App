package edu.csusb.cse.roomquest.ui;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
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
import edu.csusb.cse.roomquest.mapping.Floor;
import edu.csusb.cse.roomquest.mapping.Map;
import edu.csusb.cse.roomquest.mapping.Room;
import edu.csusb.cse.roomquest.parsing.MapMaker;
import edu.csusb.cse.roomquest.downloader.Spot;
import edu.csusb.cse.roomquest.search.SearchMap;

/**
 * Created by Michael on 3/15/2015.
 */
public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.toString();
    // splash UI elements
    private ProgressBar progress;
    private ImageView logo;

    // UI elements
    private DrawerLayout navDrawer;
    private MapView mapView;
    private ListView mapListView, resultListView;
    ResultListAdapter resultListAdapter;
    private FloorSelectorView floorsView;
    private SearchView searchView;

    // Data
    Map[] maps;
    private Map map;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    // State info
    private boolean showMenu = false;
    private boolean showingResults = false;
    private boolean highlightingRoom = false;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) // relax Android studio, I check its lolipop before I use a lolipop api
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // add some fancy colors to the app switcher if its Lolipoop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // check if lolipop
            setTaskDescription(new ActivityManager.TaskDescription(
                    "Room Quest",
                    BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher),
                    getResources().getColor(R.color.csusb_blue)
            ));
        }
        // Load those maps
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
                Spot.loadCert(getResources().openRawResource(R.raw.csusb));
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
            // ITS A TRAP!
            setContentView(R.layout.unable_to_load);
            findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMaps();
                }
            });
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
        resultListView = (ListView) findViewById(R.id.result_list);
        // set up the ListView in the NavigationDrawer
        mapListView.setAdapter(new ArrayAdapter<>(getSupportActionBar().getThemedContext(),android.R.layout.simple_list_item_1,maps));
        mapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayMap((Map) parent.getItemAtPosition(position));
                highlightRoom(null);
                hideSearch();
                navDrawer.closeDrawer(mapListView);
            }
        });
        mapListView.addHeaderView(getLayoutInflater().inflate(R.layout.map_list_header,mapListView,false),null,false);
        // set up resultListView
        resultListAdapter = new ResultListAdapter(this);
        resultListView.setAdapter(resultListAdapter);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null) {
                    try {
                        SearchMap.Result r = (SearchMap.Result) parent.getItemAtPosition(position);
                        highlightRoom(r.getRoom());
                    } catch (ClassCastException e) {
                    }
                }
                hideSearch();
            }
        });
        // set up drawer
        navDrawer.requestFocus();
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
                    hideResults();
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
            public void onIndexChange(int index) {
                mapView.loadMap(map,map.floors[index]);
                Log.i(TAG, "Loading " + map.floors[index]);
            }
        });
    }

    private void displayMap(Map map) {
        this.map = map;
        mapView.loadMap(map,map.floors[0]);
        if (map.floors.length <= 1)
            floorsView.setFloors(null);
        else
            floorsView.setFloors(map.floors,0);
        searchView.setIconified(true);
    }

    private void displayFloor(Floor floor) {
        mapView.loadFloor(floor);
        floorsView.selectFloor(floor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        // setup search view
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideResults();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                resultListView.performItemClick(resultListView,0,0);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals("") || s != null)
                search(s);
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    search(String.valueOf(searchView.getQuery()));
                    showResults();
                }
            }
        });
        return showMenu;
    }

    @Override
    public void onBackPressed() {
        if (navDrawer != null && navDrawer.isDrawerOpen(mapListView))
            navDrawer.closeDrawer(mapListView);
        else if (showingResults)
            hideResults();
        else if (highlightingRoom)
            highlightRoom(null);
        else
            super.onBackPressed();
    }

    private void search(String query) {
        if (map == null)
            return;
        else {
            SearchMap.Result[] results = SearchMap.search(map, query);
            resultListAdapter.setResults(results);
        }
    }

    private void showResults() {
        showingResults = true;
        resultListView.setVisibility(View.VISIBLE);
    }

    private void hideResults() {
        showingResults = false;
        resultListView.setVisibility(View.GONE);
    }

    private void hideSearch() {
        searchView.setQuery("", false); // clear first
        searchView.setIconified(true); // close it, WTF ANDROID API???
        // I had to read the android support library source at 3:00am to figure that one out!
        // Aperently setIconified clears instead of iconify if not clear.
        // https://github.com/android/platform_frameworks_support/blob/master/v7/appcompat/src/android/support/v7/widget/SearchView.java
    }

    private void highlightRoom(Room room) {
        mapView.highlightRoom(room);
        if (room != null) {
            displayFloor(room.getFloor());
            highlightingRoom = true;
        } else {
            highlightingRoom = false;
        }
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
