package com.codepath.apps.restclienttemplate.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.Adapter.TweetAdapter;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private final String TAG = "TimelineActivityTAG";
    private final int REQUEST_CODE = 20;

    private TwitterClient client;

    private TweetAdapter mTweetAdapter;
    private ArrayList<Tweet> mTweets;
    private RecyclerView mRvTweets;
    private LinearLayoutManager mLayoutManager;
    private EndlessRecyclerViewScrollListener mEndlessScrollListener;

    private User myUser;

    private MyJsonHttpResponseHandler myJsonHttpResponseHandler;
    private MyJsonHttpResponseHandlerUser myJsonHttpResponseHandlerUser;
    private MyJsonHttpResponseHandlerNewTweet myJsonHttpResponseHandlerNewTweet;

    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbSearch);
        setSupportActionBar(toolbar);

        client = TwitterApp.getRestClient();

        //find the recycler view
        mRvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        //init the arrayList
        mTweets = new ArrayList<>();

        //construct the adapter from this datasource
        mTweetAdapter = new TweetAdapter(mTweets);

        mLayoutManager = new LinearLayoutManager(this);
        //Recycler view setup
        mRvTweets.setLayoutManager(mLayoutManager);
        mRvTweets.setAdapter(mTweetAdapter);

        mEndlessScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                handler.postDelayed(makeQueryWithDelay, 3000);
            }
        };
        mRvTweets.addOnScrollListener(mEndlessScrollListener);

        myJsonHttpResponseHandler = new MyJsonHttpResponseHandler();
        myJsonHttpResponseHandlerUser = new MyJsonHttpResponseHandlerUser();
        myJsonHttpResponseHandlerNewTweet = new MyJsonHttpResponseHandlerNewTweet();

        getMyUSerInfo();
        populateTimeline();
    }

    private void getMyUSerInfo() {
        client.getMyUserInfo(myJsonHttpResponseHandlerUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create) {
            Log.d(TAG, "action create tweet clicked");

            Intent intent= new Intent(this, CreateTweetActivity.class);
            intent.putExtra("myUser", Parcels.wrap(myUser));
            startActivityForResult(intent, REQUEST_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String text = data.getStringExtra("tweet");
            Log.d(TAG, "New tweet :" + text);

            client.postNewTweet(myJsonHttpResponseHandlerNewTweet, text);

        } else {
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Create tweet cancelled");
            }
        }
    }
    private void populateTimeline() {

        client.getHomeTimeline(myJsonHttpResponseHandler);
    }

    private void populateTimelineFromId(long startingId) {

        client.getTimelineFromId(myJsonHttpResponseHandler,startingId);
    }

    // Create the Handler object (on the main thread by default)
    // Define the code block to be executed
    private Runnable makeQueryWithDelay = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            int numTweets = mTweets.size();
            long id = mTweets.get(numTweets - 1).mUid + 1;
            Log.d(TAG, "Requested items id starting at id = " + id);
            populateTimelineFromId(id);
        }
    };

    public class MyJsonHttpResponseHandler extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d(TAG, response.toString());
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TAG, "Received " + response.length() + "mTweets");
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

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            if (statusCode == 429){
                //try again after 5 secondes?
                handler.postDelayed(makeQueryWithDelay, 5000);
            }
            Toast.makeText(getBaseContext(), "something went wrong" + statusCode, Toast.LENGTH_SHORT).show();

            Log.d(TAG, errorResponse.toString());
            throwable.printStackTrace();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d(TAG, responseString.toString());
            throwable.printStackTrace();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            if (statusCode == 429){
                //try again after 5 secondes?
                handler.postDelayed(makeQueryWithDelay, 5000);
            }
            Toast.makeText(getBaseContext(), "something went wrong" + statusCode, Toast.LENGTH_SHORT).show();

            Log.d(TAG, errorResponse.toString());
            throwable.printStackTrace();
        }
    }

    public class MyJsonHttpResponseHandlerUser extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d(TAG, response.toString());
            try {
                myUser = User.fromJson(response);
                Log.d(TAG, "my user name is " + myUser.mName + ", screen name " + myUser.mScreenName );
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            Log.d(TAG, "Received " + response.length() + "mTweets");
            for (int i=0; i< response.length();i++){
                try {
                    Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                    mTweets.add(tweet);
                    mTweetAdapter.notifyItemInserted(mTweets.size() - 1);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            if (statusCode == 429){
                //try again after 5 secondes?
                handler.postDelayed(makeQueryWithDelay, 5000);
            }
            Toast.makeText(getBaseContext(), "something went wrong" + statusCode, Toast.LENGTH_SHORT).show();

            Log.d(TAG, errorResponse.toString());
            throwable.printStackTrace();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d(TAG, responseString.toString());
            throwable.printStackTrace();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            if (statusCode == 429){
                //try again after 5 secondes?
                handler.postDelayed(makeQueryWithDelay, 5000);
            }
            Toast.makeText(getBaseContext(), "something went wrong" + statusCode, Toast.LENGTH_SHORT).show();

            Log.d(TAG, errorResponse.toString());
            throwable.printStackTrace();
        }
    }

    public class MyJsonHttpResponseHandlerNewTweet extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d(TAG, response.toString());
            try {
                Tweet newTweet = Tweet.fromJSON(response);
                mTweets.add(0,newTweet);
                mTweetAdapter.notifyItemInserted(0);
                mRvTweets.scrollToPosition(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            Log.d(TAG, response.toString());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Toast.makeText(getBaseContext(), "something went wrong" + statusCode, Toast.LENGTH_SHORT).show();
            Log.d(TAG, errorResponse.toString());
            throwable.printStackTrace();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d(TAG, responseString);
            throwable.printStackTrace();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            Log.d(TAG, errorResponse.toString());
            throwable.printStackTrace();
        }
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
}
