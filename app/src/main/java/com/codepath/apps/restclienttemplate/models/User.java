package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by emilie on 9/25/17.
 */

public class User implements Serializable{

    public String mName;
    public long mUid;
    public String mScreenName;
    public String mProfileImageUrl;


    public static User fromJson(JSONObject jsonObject) throws JSONException{
        User user = new User();

        user.mName = jsonObject.getString("name");
        user.mUid = jsonObject.getLong("id");
        user.mScreenName = jsonObject.getString("screen_name");
        user.mProfileImageUrl = jsonObject.getString("profile_image_url");

        return user;

    }
}
