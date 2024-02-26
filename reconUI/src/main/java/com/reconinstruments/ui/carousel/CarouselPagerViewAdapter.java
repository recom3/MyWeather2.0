package com.reconinstruments.ui.carousel;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.reconinstruments.ui.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * ViewPager adapter class for CarouselItems
 * loads views as needed but will hold them all in memory
 * because it holds onto all the child views it shouldn't be used to scroll through a huge number of items
 * however, it is bad user experience for a user to scroll through a large number of carousel items regardless
 *
 * doesn't use a fragment adapter for this reason, that it allows a simpler API where custom items don't need to
 * serialize their state, and for the size of carousels we will use in almost all cases, the fragment lifecycle is not
 * beneficial
 */
public class CarouselPagerViewAdapter extends PagerAdapter {

    LayoutInflater inflater;

    Context context;
    List<? extends CarouselItem> items;
    View[] views;
    CarouselViewPager pager;

    public CarouselPagerViewAdapter(Context context,List<? extends CarouselItem> items,CarouselViewPager pager) {
        this.context = context;
        this.items = items;
        this.pager = pager;
        inflater = LayoutInflater.from(context);
        views = new View[items.size()];
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = getView(position);
        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((View)object);
    }

    public View getView(int position) {
        View view = views[position];
        if(view==null) {
            CarouselItem carouselItem = items.get(position);

            view = inflater.inflate(carouselItem.getLayoutId(), null);
            carouselItem.updateView(view);

            view.setTag(position);
            views[position] = view;
        }
        return view;
    }

    public CarouselItem getCarouselItem(int position) {
        return items.get(position);
    }

    public void updateViewForPosition(int position, CarouselItem.POSITION rel_position) {

        View view = getView(position);
        CarouselItem carouselItem = items.get(position);

        if(rel_position==CarouselItem.POSITION.CENTER)
            view.setSelected(true);
        else
            view.setSelected(false);

        carouselItem.updateViewForPosition(view, rel_position);
    }


    @Override
    public float getPageWidth(int position) {
        int pagerWidth = pager.getMeasuredWidth();

        View view = getView(position);
        int viewWidth = view.getMeasuredWidth();
        // if view hasn't been measured yet
        if(viewWidth==0) {
            int unspecSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(unspecSpec,unspecSpec);
            viewWidth = view.getMeasuredWidth();
        }
        // account for rounding errors that might clip text
        viewWidth += 1;

        float ratio = ((float)viewWidth/(float)pagerWidth);
        return ratio;
    }
}
