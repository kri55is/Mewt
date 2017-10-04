package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by emilie on 10/2/17.
 */

public class HomeTimelineFragment extends TweetsListFragments {

    private final String TAG = "HomeTimelineFragmentTAG";

    private TwitterClient client;

//    private User myUser;

    private MyJsonHttpResponseHandler myJsonHttpResponseHandler;
//    private MyJsonHttpResponseHandlerUser myJsonHttpResponseHandlerUser;
    private MyJsonHttpResponseHandlerNewTweet myJsonHttpResponseHandlerNewTweet;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApp.getRestClient();

        myJsonHttpResponseHandler = new MyJsonHttpResponseHandler();
//        myJsonHttpResponseHandlerUser = new MyJsonHttpResponseHandlerUser();
        myJsonHttpResponseHandlerNewTweet = new MyJsonHttpResponseHandlerNewTweet();

//        getMyUSerInfo();
        populateHomeTimeline();

    }

//    private void getMyUSerInfo() {
//        client.getUserInfo(myJsonHttpResponseHandlerUser);
//    }


    private void populateHomeTimeline() {

        client.getHomeTimeline(myJsonHttpResponseHandler);
    }

    private void populateHomeTimelineFromId(long startingId) {

        client.getTimelineFromId(myJsonHttpResponseHandler,startingId);
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
            populateHomeTimelineFromId(id);
        }
    };

    public void postNewTweet(String text){

        client.postNewTweet(myJsonHttpResponseHandlerNewTweet, text);

    }


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


    public class MyJsonHttpResponseHandlerNewTweet extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d(TAG, response.toString());
            insertItem(response, 0);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            Log.d(TAG, response.toString());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Toast.makeText(getContext(), "something went wrong" + statusCode, Toast.LENGTH_SHORT).show();
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

}
