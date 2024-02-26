package com.reconinstruments.ui.list;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.reconinstruments.ui.R;

/**
 * Created by chris on 19/06/15.
 */
public class ReconListView extends ListView {

    // Used to insert a spacing into the contents of the listview (ie. scrolls with the list view)
    // unfortunately needs to be implemented in the array adapter for reasons outlined in SimpleArrayAdapter
    int listHeaderMargin;

    public ReconListView(Context context) {
        super(context);
        init();
    }

    public ReconListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ReconListView,0,0);
        try {
            listHeaderMargin = a.getDimensionPixelSize(R.styleable.ReconListView_listHeaderMargin, 18);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int selectedItemPos = getSelectedItemPosition();
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if(selectedItemPos==(getAdapter().getCount() - 1)) {
                smoothScrollToPosition(0);
                setSelection(0);
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (selectedItemPos == 0) {
                smoothScrollToPosition(getAdapter().getCount() - 1);
                setSelection(getAdapter().getCount() - 1);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setAdapter(SimpleArrayAdapter adapter) {
        adapter.setListView(this);
        super.setAdapter(adapter);
    }
}
