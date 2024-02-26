package com.reconinstruments.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Arrays;
import java.util.List;

/*
 * ArrayAdapter for list of SimpleListItems, which define their own views, uses convertView for all views
 * so must extend and implement getViewTypeCount and getItemViewType in order to use more than one layout
 */
public class SimpleArrayAdapter<T extends SimpleListItem> extends ArrayAdapter<T> {

    ReconListView listView;

    Context context = null;
    List<T> contents = null;

    public SimpleArrayAdapter(Context context, T... contents) {
        this(context, Arrays.asList(contents));
    }
    public SimpleArrayAdapter(Context context, List<T> contents) {
        super(context, 0, contents);
        this.context = context;
        this.contents = contents;
    }

    public void setListView(ReconListView listView) {
        this.listView = listView;
    }

    @Override
    public T getItem(int position) {
        return contents.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        T item = getItem(position);

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(item.getLayoutId(), null);
        }
        item.updateView(convertView);
        // Needed in order to create space above the first item while not modifying the list view bounds
        // Ideally we would have this padding defined in a layout, style or at least in the listview itself..
        // using a list view headerview requires more modifications to any adapter for the list view,
        // simplest solution by far is to insert the space inside the top list view item, so that the listView child views
        // match the adapter items 1 to 1
        // The convertView layout needs to be able to insert top padding without changing the inner layout
        if(position==0) {
            convertView.setPadding(0, listView.listHeaderMargin, 0, 0);
        } else {
            convertView.setPadding(0, 0, 0, 0);
        }
        return convertView;
    }
}