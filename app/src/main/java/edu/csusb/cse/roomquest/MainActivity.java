package edu.csusb.cse.roomquest;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MainActivity extends ActionBarActivity {

    Map map = null;
    MapView mapView;
    private SearchView searchView = null;
    private FileInputStream fileInputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mapView = new MapView(this);

        File mapFile = new File(Environment.getExternalStorageDirectory() + "/RoomQuest/map.rqm");
        Log.d("File", mapFile.getPath());
        try {
            fileInputStream = new FileInputStream(mapFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... params) {
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
        ;
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        return true;
    }

    private void showMap() {
        setContentView(mapView);
    }

    private void showSearchResults() {

    }
}
