package org.smartregister.path.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by keyman on 24-Mar-17.
 */
public class PathRegisterActivityPagerAdapter extends FragmentPagerAdapter {
    private static final String ARG_PAGE = "page";
    private final Fragment mBaseFragment;
    private final FragmentManager fragmentManager;
    private final Fragment[] fragments;

    public PathRegisterActivityPagerAdapter(FragmentManager fragmentManager, Fragment baseFragment, Fragment[] fragments) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.mBaseFragment = baseFragment;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = mBaseFragment;
                break;
            default:
                fragment = fragments[position - 1];
                break;
        }

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        if (fragment != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments == null ? 1 : fragments.length + 1;
    }
}
