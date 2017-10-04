package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by emilie on 10/4/17.
 */

public class Session {

    private final String TAG = "Session";
    private static Session mSession = null;


    private User myUser;

    private TwitterClient client;
    private MyJsonHttpResponseHandlerUser myJsonHttpResponseHandlerUser;

    public static Session getInstance(){
        if (mSession == null) {
            mSession = new Session();
        }
        return mSession;
    }

    private Session(){

        myJsonHttpResponseHandlerUser = new MyJsonHttpResponseHandlerUser();
        client = TwitterApp.getRestClient();

        getMyUserInfo();
    }

    private void getMyUserInfo() {
        client.getUserInfo(myJsonHttpResponseHandlerUser);
    }

    public User getMyUser() {
        return myUser;
    }

    public class MyJsonHttpResponseHandlerUser extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d(TAG, response.toString());
            try {
                myUser = User.fromJson(response);
                Log.d(TAG, "user name is " + myUser.mName + ", screen name " + myUser.mScreenName );
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            Log.d(TAG, "Received " + response.length() + "mTweets");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
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
            Log.d(TAG, errorResponse.toString());
            throwable.printStackTrace();
        }
    }
}
