package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Session;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by emilie on 10/2/17.
 */

public class UserTimelineFragment extends TweetsListFragments {

    private final String TAG = "UserTimelineFragmentTAG";
    private TwitterClient client;

    private User myUser;

    private MyJsonHttpResponseHandler myJsonHttpResponseHandler;

    public static UserTimelineFragment newInstance(String screenName){
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApp.getRestClient();

        myJsonHttpResponseHandler = new MyJsonHttpResponseHandler();

        //todo remove get my user info: we might want other user info
        String screenName = getArguments().getString("screen_name");
        if(screenName == null) {
            Session session = Session.getInstance();
            myUser = session.getMyUser();
        }
        populateUserTimeline();

    }

    private void populateUserTimeline() {
        String screenName = getArguments().getString("screen_name");

        client.getUserTimeline(screenName, myJsonHttpResponseHandler);
    }

    private void populateUserTimelineFromId(long startingId) {
        String screenName = getArguments().getString("screen_name");

        client.getUserTimelineFromId(screenName, myJsonHttpResponseHandler,startingId);
    }

    // Create the Handler object (on the main thread by default)
    // Define the code block to be executed
    public Runnable makeQueryWithDelay = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            ArrayList<Tweet> tweets = getTweets();
            int numTweets = tweets.size();
            long id = tweets.get(numTweets - 1).mUid - 1;
            Log.d(TAG, "Requested items id starting at id = " + id);
            populateUserTimelineFromId(id);
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

            addItems(response);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            if (statusCode == 429){
                //try again after 5 secondes?
                handler.postDelayed(makeQueryWithDelay, 5000);
            }
            Toast.makeText(getContext(), "something went wrong: " + statusCode + ". errorResponse: ", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getContext(), "something went wrong" + statusCode, Toast.LENGTH_SHORT).show();

            Log.d(TAG, errorResponse.toString());
            throwable.printStackTrace();
        }
    }

    @Override
    public void loadTweets() {
//        Toast.makeText(getContext(),"loadMoreTweets USerTimeline",Toast.LENGTH_SHORT).show();
        handler.postDelayed(makeQueryWithDelay, 3000);
    }
}
