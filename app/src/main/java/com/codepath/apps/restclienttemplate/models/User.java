package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by emilie on 9/25/17.
 */

@Parcel
public class User {

    public String mName;
    public long mUid;
    public String mScreenName;
    public String mProfileImageUrl;
    public String mTagline;
    public int mFollowersCount;
    public int mFollowingCount;


    public static User fromJson(JSONObject jsonObject) throws JSONException{
        User user = new User();

        user.mName = jsonObject.getString("name");
        user.mUid = jsonObject.getLong("id");
        user.mScreenName = jsonObject.getString("screen_name");
        user.mProfileImageUrl = jsonObject.getString("profile_image_url");

        user.mTagline = jsonObject.getString("description");
        user.mFollowersCount = jsonObject.getInt("followers_count");
        user.mFollowingCount = jsonObject.getInt("friends_count");

        return user;

    }
}
