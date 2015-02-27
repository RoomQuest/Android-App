package edu.csusb.cse.roomquest;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MainActivity extends ActionBarActivity {

    Map map = null;
    MapView mapView;
    private SearchView searchView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up UI elements.
        View splash = getLayoutInflater().inflate(R.layout.splash, (ViewGroup) this.getWindow().getDecorView(), false);
        setContentView(splash);
        mapView = new MapView(this);
        mapView.setLocation(352,256);

        // Update and read files in the background.
        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                FileInputStream fileInputStream = null;
                File mapFile = new File(Environment.getExternalStorageDirectory() + "/RoomQuest/map.rqm");
                try {
                    fileInputStream = new FileInputStream(mapFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    map = MapMaker.parseMapFile(fileInputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mapView.loadMap(map);
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                MainActivity.this.showMap();
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        return true;
    }

    private void showMap() {
        setContentView(mapView);
    }

    private void showSearchResults() {

    }
}
