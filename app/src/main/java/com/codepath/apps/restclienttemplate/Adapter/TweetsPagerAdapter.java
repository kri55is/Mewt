package com.codepath.apps.restclienttemplate.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionsTimelineFragment;

/**
 * Created by emilie on 10/2/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context context;

    public TweetsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;

    }

    //return total num of fragments
    @Override
    public int getCount() {
        return 2;
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
