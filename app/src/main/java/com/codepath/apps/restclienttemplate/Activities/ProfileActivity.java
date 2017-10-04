package com.codepath.apps.restclienttemplate.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "ProfileActivityTAG";

    private int mRadius = 30; // corner radius, higher value = more rounded
    private int mMargin = 5; // crop margin, set to 0 for corners with no crop

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


        client = TwitterApp.getRestClient();
        myJsonHttpResponseHandlerUser = new MyJsonHttpResponseHandlerUser();

        if(screen_name == null) {
            client.getUserInfo(myJsonHttpResponseHandlerUser);
        }
        else{
            client.getSpecificUserInfo(screen_name, myJsonHttpResponseHandlerUser);
        }
    }

    public class MyJsonHttpResponseHandlerUser extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d(TAG, response.toString());
            try {
                User user = User.fromJson(response);
                Log.d(TAG, "User name is " + user.mName + ", screen name " + user.mScreenName );

                getSupportActionBar().setTitle(user.mScreenName);
                
                //populate User headline
                populateUserHeadline(user);
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

    private void populateUserHeadline(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.mName);

        tvTagline.setText(user.mTagline);
        tvFollowers.setText(user.mFollowersCount + " Followers");
        tvFollowing.setText(user.mFollowingCount + " Following");

        //load profile image
        Glide.with(this)
                .load(user.mProfileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(this, mRadius, mMargin))
                .into(ivProfileImage);
    }
}
