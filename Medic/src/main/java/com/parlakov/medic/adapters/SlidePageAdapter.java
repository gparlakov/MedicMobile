package com.parlakov.medic.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by georgi on 13-11-17.
 */
public class SlidePageAdapter extends FragmentPagerAdapter {

    public SlidePageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Fragment getItem(int i) {
        return null;
    }
}
