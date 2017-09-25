package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by emilie on 9/25/17.
 */

public class Tweet {

    public String mBody;
    public long mUid; //database ID fot the tweet
    public User mUser;
    public String mCreatedAt;

    //Deserialize JSON

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        tweet.mBody = jsonObject.getString("text");
        tweet.mUid = jsonObject.getLong("id");
        tweet.mCreatedAt = jsonObject.getString("created_at");

        tweet.mUser = User.fromJson(jsonObject.getJSONObject("user"));

        return tweet;
    }

}
