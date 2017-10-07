package com.codepath.apps.restclienttemplate.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionsTimelineFragment;

/**
 * Created by emilie on 10/2/17.
 */

public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {

    private static int NUM_ITEMS = 2;
    private String tabTitles[] = new String[] {"Home", "Mentions"};

    public TweetsPagerAdapter(FragmentManager fm){
        super(fm);

    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    //return the fragment to use depending on position
    @Override
    public Fragment getItem(int position) {
        if( position == 0)
            return new HomeTimelineFragment();
        else if (position == 1){
            return new MentionsTimelineFragment();
        }
        else {
            return null;
        }
    }


    //return fragment title
    @Override
    public CharSequence getPageTitle(int position) {
        //generate title based on item position
        return tabTitles[position];
    }

}


