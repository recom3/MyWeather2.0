package com.reconinstruments.ui.list;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.reconinstruments.ui.R;

import java.util.Arrays;
import java.util.List;

/**
 * ListActivity for a list of SimpleListItems, which can define their own view and onClick behaviour
 */
public abstract class SimpleListActivity extends FragmentActivity {

    SimpleArrayAdapter<SimpleListItem> mListAdapter;
    protected ReconListView mListView;


    public ReconListView getListView() {
        return mListView;
    }

    public void setContents(SimpleListItem... contents){
        setContents(Arrays.asList(contents));
    }
    public void setContents(List<SimpleListItem> contents) {
        setAdapter(new SimpleArrayAdapter<SimpleListItem>(this,contents));
    }

    public void updateContents(List<SimpleListItem> contents) {
        attachContents(contents);
        mListAdapter.contents = contents;
        mListAdapter.notifyDataSetChanged();
    }

    public void setAdapter(SimpleArrayAdapter<SimpleListItem> adapter) {
        if(mListView==null) {
            mListView = (ReconListView) findViewById(android.R.id.list);
        }
        attachContents(adapter.contents);
        mListAdapter = adapter;
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListAdapter.getItem(position).onClick(SimpleListActivity.this);
            }
        });
        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mListAdapter.getItem(position).onSelected(SimpleListActivity.this);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void attachContents(List<SimpleListItem> contents) {
        for(int i=0;i<contents.size();i++) {
            contents.get(i).attachToList(getListView(),i);
        }
    }

    public SimpleArrayAdapter<SimpleListItem> getAdapter() {
        return mListAdapter;
    }

    /**
     * Helper function to refresh view for a specific list item
     * @param item item to update view for
     */
    public void updateItem(SimpleListItem item) {
        getAdapter().getView(item.position, item.getView(), getListView());
    }
}