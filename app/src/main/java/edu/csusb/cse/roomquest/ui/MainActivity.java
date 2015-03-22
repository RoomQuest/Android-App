package edu.csusb.cse.roomquest.ui;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import edu.csusb.cse.roomquest.mapping.Map;
import edu.csusb.cse.roomquest.parsing.MapMaker;
import edu.csusb.cse.roomquest.R;
import edu.csusb.cse.roomquest.mapping.Room;
import edu.csusb.cse.roomquest.mapping.SearchMap;

import static android.os.Environment.getExternalStorageDirectory;


public class MainActivity extends ActionBarActivity {

    // Data
    Map map = null;

    // UI
    // Maps
    MapView mapView;
    // Search
    ListView resultsList;
    RoomListAdapter resultsListAdapter;

    // Action Bar
    private SearchView searchView = null;

    // Mode
    private enum Mode {
        DisplayMap, DisplayResults, Loading
    }
    Mode mode = Mode.Loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG","onCreate");
        // Set up UI elements.
        View splash = getLayoutInflater().inflate(R.layout.splash, null);
        mapView = new MapView(this);
        mapView.setLocation(352,256);
        resultsList = new ListView(this);
        resultsListAdapter = new RoomListAdapter(this);
        resultsList.setAdapter(resultsListAdapter);
        resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                highlightRoom(resultsListAdapter.getItem(position));
            }
        });

        setContentView(splash);

        // Update and read files in the background.
        new UpdateMapAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                highlightRoom(null);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("RoomQuestSearch",s == null ? null : s.isEmpty() ? "empty" : s);
                search(s);
                return false;
            }
        });
        return true;
    }

    private void search(String query) {
        Room[] rooms = SearchMap.SearchForRooms(map, query);
        if (rooms != null  && query != null && !query.isEmpty()) {
            resultsListAdapter.setRooms(rooms);
            showSearchResults();
        } else {
            showMap();
        }
    }

    private void showMap() {
        if (mode != Mode.DisplayMap) {
            setContentView(mapView);
            mode = Mode.DisplayMap;
        }
    }

    private void showSearchResults() {
        if (mode != Mode.DisplayResults) {
            setContentView(resultsList);
            mode = Mode.DisplayResults;
        }
    }

    private void highlightRoom(Room room) {
        mapView.highlightRoom(room);
        showMap();
    }

    private class UpdateMapAsyncTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            FileInputStream fileInputStream = null;
            File mapFolder = new File(Environment.getExternalStorageDirectory(),"RoomQuest" + File.separator + "JB");
            map = MapMaker.parseMapFolder(mapFolder,"JB","Jack Brown Hall");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mapView.loadMap(map, map.floors[0]/*getExternalStorageDirectory() + "/RoomQuest/JB/1.png"*/);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            MainActivity.this.showMap();
        }
    }
}
