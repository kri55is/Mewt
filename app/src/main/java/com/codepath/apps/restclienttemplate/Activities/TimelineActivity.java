package com.codepath.apps.restclienttemplate.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragments;

public class TimelineActivity extends AppCompatActivity {

    private final String TAG = "TimelineActivityTAG";

    private final int REQUEST_CODE = 20;

    private TweetsListFragments fragmentTweetsList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbSearch);
        setSupportActionBar(toolbar);


        fragmentTweetsList = (TweetsListFragments) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);



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
//            intent.putExtra("myUser", Parcels.wrap(myUser));
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


//            client.postNewTweet(myJsonHttpResponseHandlerNewTweet, text);

        } else {
            if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Create tweet cancelled");
            }
        }
    }


}
