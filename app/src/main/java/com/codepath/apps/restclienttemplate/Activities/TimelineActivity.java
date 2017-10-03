package com.codepath.apps.restclienttemplate.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.Adapter.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragments;
import com.codepath.apps.restclienttemplate.models.Tweet;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragments.TweetSelectedListener {

    private final String TAG = "TimelineActivityTAG";

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbSearch);
        setSupportActionBar(toolbar);

        //get view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        //set the adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));

        //setup
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

    }

    private void getMyUSerInfo() {
//        client.getUserInfo(myJsonHttpResponseHandlerUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
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
//            intent.putExtra("myUser", Parcels.wrap(myUser));
            startActivityForResult(intent, REQUEST_CODE);

            return true;
        }
        if (id == R.id.miProfile){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String text = data.getStringExtra("tweet");
            Log.d(TAG, "New tweet :" + text);


//            client.postNewTweet(myJsonHttpResponseHandlerNewTweet, text);

        } else {
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Create tweet cancelled");
            }
        }
    }

    @Override
    public void onTweetSelected(Tweet tweet) {
                Toast.makeText(this, tweet.mBody, Toast.LENGTH_SHORT).show();

    }
}
