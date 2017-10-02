package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.Adapter.TweetAdapter;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by emilie on 10/2/17.
 */

public class TweetsListFragments extends Fragment {

    //inflation happens insideonCreateView

    private final String TAG = "TweetsListFragmentsTAG";

    private TweetAdapter mTweetAdapter;
    private ArrayList<Tweet> mTweets;
    private RecyclerView mRvTweets;
    private LinearLayoutManager mLayoutManager;
    private EndlessRecyclerViewScrollListener mEndlessScrollListener;

    Handler handler = new Handler();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);

        //find the recycler view
        mRvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);

        //init the arrayList
        mTweets = new ArrayList<>();

        //construct the adapter from this datasource
        mTweetAdapter = new TweetAdapter(mTweets);

        mLayoutManager = new LinearLayoutManager(getContext());
        //Recycler view setup
        mRvTweets.setLayoutManager(mLayoutManager);
        mRvTweets.setAdapter(mTweetAdapter);

        mEndlessScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                handler.postDelayed(makeQueryWithDelay, 3000);
                Toast.makeText(getContext(), "should load more", Toast.LENGTH_SHORT).show();;
            }
        };
        mRvTweets.addOnScrollListener(mEndlessScrollListener);

        mRvTweets.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return v;
    }


    public ArrayList<Tweet> getTweets(){
        return mTweets;
    }

    public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 5;
        // The current offset index of data you have loaded
        private int currentPage = 0;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;
        // Sets the starting page index
        private int startingPageIndex = 0;

        RecyclerView.LayoutManager mLayoutManager;

        public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
        }

        public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public int getLastVisibleItem(int[] lastVisibleItemPositions) {
            int maxSize = 0;
            for (int i = 0; i < lastVisibleItemPositions.length; i++) {
                if (i == 0) {
                    maxSize = lastVisibleItemPositions[i];
                }
                else if (lastVisibleItemPositions[i] > maxSize) {
                    maxSize = lastVisibleItemPositions[i];
                }
            }
            return maxSize;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScrolled(RecyclerView view, int dx, int dy) {
            int lastVisibleItemPosition = 0;
            int totalItemCount = mLayoutManager.getItemCount();

            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
            } else if (mLayoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            } else if (mLayoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }

            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.loading = true;
                }
            }
            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            // threshold should reflect how many total columns there are too
            if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                currentPage++;
                onLoadMore(currentPage, totalItemCount, view);
                loading = true;
            }
        }

        // Call this method whenever performing new searches
        public void resetState() {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = 0;
            this.loading = true;
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

    }


    public void insertItem(JSONObject response, int position){
        try {
            Tweet newTweet = Tweet.fromJSON(response);
            mTweets.add(0,newTweet);
            mTweetAdapter.notifyItemInserted(0);
            mRvTweets.scrollToPosition(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void addItems(JSONArray response){

        for (int i=0; i< response.length();i++){
            try {
                Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                Log.d(TAG,"Adding tweet: " + tweet.mBody);
                mTweets.add(tweet);
                mTweetAdapter.notifyItemInserted(mTweets.size() - 1);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
