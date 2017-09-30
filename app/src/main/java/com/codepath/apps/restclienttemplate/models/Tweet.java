package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by emilie on 9/25/17.
 */

public class Tweet {

    final static String TAG = "Tweet";

    public String mBody;
    public long mUid; //database ID fot the tweet
    public User mUser;
    public String mCreatedAt;

    public String mTweetImage="";

    //Deserialize JSON

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        tweet.mBody = jsonObject.getString("text");
        tweet.mUid = jsonObject.getLong("id");
        tweet.mCreatedAt = getRelativeTimeAgo(jsonObject.getString("created_at"));

        tweet.mUser = User.fromJson(jsonObject.getJSONObject("user"));

        try{
            JSONObject objEntity = jsonObject.getJSONObject("entities");
//            Log.d(TAG, objEntity.toString());
            JSONArray objMedia = objEntity.getJSONArray("media");
//            Log.d(TAG, objMedia.toString());
            String media_URL = objMedia.getJSONObject(0).getString("media_url");
            tweet.mTweetImage = media_URL;
        }
        catch(JSONException e){
            Log.d(TAG, "no image in this tweet");
        }

        return tweet;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        //todo get local dynamically
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public static void logTweetsTitle(ArrayList<Tweet> tweets){
        Log.d(TAG, "Printing tweets.");
        for (int i = 0; i < tweets.size();i++) {
            Log.d(TAG, " tweet " + i + " : " + tweets.get(i).mBody);
        }

    }
}
