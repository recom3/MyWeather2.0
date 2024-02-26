package com.reconinstruments.ui.carousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.reconinstruments.ui.R;
import com.reconinstruments.ui.breadcrumb.BreadcrumbToast;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/*
 * Custom horizontal view pager containing views defined by a list of CarouselItems
 *
 */
public class CarouselViewPager extends CenterAlignViewPager {
    private static final String TAG = CarouselViewPager.class.getSimpleName();

    // Do not show breadcrumbs if fewer fragments than this
    static final int MIN_NUM_FRAGMENTS_TO_SHOW_BREADCRUMBS = 3;

    // Adapter for CarouselItems
    CarouselPagerViewAdapter mPagerAdapter;

    // configurable attributes
    // whether to display a horizontal breadcrumb view when the page is changed
    private boolean showBreadcrumbs;
    // show zoom in/out animation on item selection
    private boolean animateSelection;
    // margin between pages
    private int pageMargin;

    // parent view to attach breadcrumbs to
    private ViewGroup breadcrumbContainer;
    // holds the breadcrumb view
    private BreadcrumbToast breadcrumbToast;

    // time to scroll between pages, in ms
    static final int SCROLL_SPEED = 300;

    // listener for when user presses select on an item
    OnItemSelectedListener onItemSelectedListener;

    /**
     * Simplified onPageChangeListener, called when there is a new active page
     */
    public interface OnPageSelectListener {
        void onPageSelected(CarouselItem item, int position);
    }
    /**
     * Called when the select button pressed on the active page
     */
    public interface OnItemSelectedListener {
        void onItemSelected(CarouselViewPager parent, CarouselItem item, int position);
    }

    public CarouselViewPager(Context context) {
        super(context);
        init();
    }
    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CarouselViewPager,0,0);
        try {
            showBreadcrumbs = a.getBoolean(R.styleable.CarouselViewPager_showBreadcrumbs, true);
            animateSelection = a.getBoolean(R.styleable.CarouselViewPager_animateSelection, false);
            pageMargin = a.getDimensionPixelSize(R.styleable.CarouselViewPager_pageMargin, 0);
        } finally {
            a.recycle();
        }
        init();
    }


    public void init() {
        setPageMargin(pageMargin);
        setClipToPadding(false);
        this.setClipChildren(false);
        this.setOffscreenPageLimit(3);

        // Use reflection to set fixed scroll speed
        Interpolator sInterpolator = new DecelerateInterpolator();
        try {
            Field mScroller = CenterAlignViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), sInterpolator, SCROLL_SPEED);
            mScroller.set(this, scroller);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        addOnPageSelectListener(new OnPageSelectListener() {
            @Override
            public void onPageSelected(CarouselItem item, int position) {
                CarouselViewPager.this.onPageSelected(position);
            }
        });
    }

    public CarouselPagerViewAdapter getCarouselAdapter() {
        return mPagerAdapter;
    }

    public void setBreadcrumbContainer(ViewGroup breadcrumbContainer) {
        this.breadcrumbContainer = breadcrumbContainer;
    }

    /*
     * Add a listener to be called when a new page has become active, simplified form of onPageChangeListener
     * that ignores scroll events
     */
    public void addOnPageSelectListener(final OnPageSelectListener onPageSelectListener) {
        super.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageSelected(int position) {
                onPageSelectListener.onPageSelected(getCarouselAdapter().getCarouselItem(position),position);
            }
        });
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public CarouselItem getCurrentCarouselItem() {
        return getCarouselAdapter().getCarouselItem(getCurrentItem());
    }

    public View getCurrentView() {
        return getCarouselAdapter().getView(getCurrentItem());
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER) {
            CarouselItem currentItem = getCurrentCarouselItem();
            if(animateSelection) {
                View currentView = getCurrentView();
                currentView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.carousel_item_push));
            }
            currentItem.onClick(getContext());
            if(onItemSelectedListener!=null) {
                onItemSelectedListener.onItemSelected(this,currentItem, getCurrentItem());
            }
        }
        return super.onKeyUp(keyCode,event);
    }

    private void onPageSelected(int selection) {
        if(breadcrumbToast!=null)
            breadcrumbToast.updateBreadcrumbs(selection);

        getCarouselAdapter().updateViewForPosition(selection, CarouselItem.POSITION.CENTER);
        if (selection>0)
            getCarouselAdapter().updateViewForPosition(selection - 1, CarouselItem.POSITION.LEFT);
        if (selection<getAdapter().getCount()-1)
            getCarouselAdapter().updateViewForPosition(selection + 1, CarouselItem.POSITION.RIGHT);
    }

    public void setContents(CarouselItem... items){
        setContents(Arrays.asList(items));
    }
    public void setContents(List<? extends CarouselItem> items){
        setAdapter(new CarouselPagerViewAdapter(getContext(),items,this));
    }

    public void setAdapter(CarouselPagerViewAdapter adapter) {
        super.setAdapter(adapter);
        mPagerAdapter = adapter;
        initBreadcrumbs();
        setSelection(0);
    }
    private void initBreadcrumbs() {
        int numItems = mPagerAdapter.getCount();
        if(showBreadcrumbs&&numItems>=MIN_NUM_FRAGMENTS_TO_SHOW_BREADCRUMBS) {
            breadcrumbToast = new BreadcrumbToast(getContext(),breadcrumbContainer,true,numItems);
        }
    }

    /*
     * Scroll the carousel to the specified index
     */
    public void setSelection(int selection) {
        setCurrentItem(selection);
        onPageSelected(selection);
    }

    /**
     * Scrolls the pager at a fixed speed
     */
    public class FixedSpeedScroller extends Scroller {
        private int mDuration;
        public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
            super(context, interpolator);
            this.mDuration = duration;
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}