package com.codepath.apps.restclienttemplate.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "ProfileActivityTAG";

    TwitterClient client;
    MyJsonHttpResponseHandlerUser myJsonHttpResponseHandlerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screen_name = getIntent().getStringExtra("screen_name");
        //create user fragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screen_name);

        //display the usertimeline fragment inside container dynamically
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //make changes
        ft.replace(R.id.flContainer, userTimelineFragment);

        //commit
        ft.commit();


        myJsonHttpResponseHandlerUser = new MyJsonHttpResponseHandlerUser();
        client = TwitterApp.getRestClient();


    }

    public class MyJsonHttpResponseHandlerUser extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d(TAG, response.toString());
            try {
                User myUser = User.fromJson(response);
                Log.d(TAG, "my user name is " + myUser.mName + ", screen name " + myUser.mScreenName );

                getSupportActionBar().setTitle(myUser.mScreenName);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
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
