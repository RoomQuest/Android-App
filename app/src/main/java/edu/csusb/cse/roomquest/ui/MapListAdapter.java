package edu.csusb.cse.roomquest.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.csusb.cse.roomquest.mapping.Map;

/**
 * Created by Michael on 3/16/2015.
 */
public class MapListAdapter extends BaseAdapter {
    private final Context context;
    Map[] maps;
    public MapListAdapter (Context context) {
        this.context = context;
    }

    public void setMaps (Map[] maps) {
        this.maps = maps;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (maps != null) {
            return maps.length;
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {return false;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private class Tag {
        TextView tv;//TODO
    }
}
