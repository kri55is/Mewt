package com.codepath.apps.restclienttemplate.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by emilie on 9/25/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    final int DEFAULT = 0;
    final int IMAGE = 1;

    private List<Tweet> mTweets;
    private User mUser;

    private Context mContext;

    private int mRadius = 30; // corner radius, higher value = more rounded
    private int mMargin = 5; // crop margin, set to 0 for corners with no crop

    //pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;
    }

    @Override
    public int getItemViewType(int position) {
        Tweet tweet = mTweets.get(position);
        String imageURL = tweet.mTweetImage;
        if(!imageURL.isEmpty()) {
            return IMAGE;
        }
        return DEFAULT;
    }


    //for each row, inflate the layout and cache references into viewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case IMAGE:
                View tweetView0 = inflater.inflate(R.layout.item_tweet_with_image, parent, false);
                viewHolder = new ViewHolderWithImage(tweetView0);
                break;
            default:
                View tweetViewDef = inflater.inflate(R.layout.item_tweet, parent, false);
                viewHolder = new ViewHolder(tweetViewDef);
                break;

        }
        return viewHolder;
    }

    //bind the values based on the position of the element
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        //get data according to position
        Tweet tweet = mTweets.get(position);


        switch (viewHolder.getItemViewType()) {
            case IMAGE:
                ViewHolderWithImage vhImage = (ViewHolderWithImage) viewHolder;
                configureViewHolderWithImage( vhImage, tweet);
                break;
            default:
                ViewHolder vh = (ViewHolder) viewHolder;
                configureDefaultViewHolder(vh, tweet);
                break;
        }
    }

    public void configureDefaultViewHolder(ViewHolder holder, Tweet tweet){
        //populate the views according to this data
        holder.tvUsername.setText(tweet.mUser.mName);
        holder.tvScreenName.setText("@" + tweet.mUser.mScreenName);
        holder.tvBody.setText(tweet.mBody);
        holder.tvDate.setText(tweet.mCreatedAt);

        Glide.with(mContext)
                .load(tweet.mUser.mProfileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(mContext, mRadius, mMargin))
                .into(holder.ivProfileImage);
    }

    public void configureViewHolderWithImage(ViewHolderWithImage holder, Tweet tweet){
        holder.tvUsername.setText(tweet.mUser.mName);
        holder.tvScreenName.setText("@" + tweet.mUser.mScreenName);
        holder.tvBody.setText(tweet.mBody);
        holder.tvDate.setText(tweet.mCreatedAt);

        Glide.with(mContext)
                .load("https://pbs.twimg.com/media/DKyhyM_VoAAxcb3.jpg")
//                .load(tweet.mTweetImage)
                .into(holder.ivTweetImage);


        Glide.with(mContext)
                .load(tweet.mUser.mProfileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(mContext, mRadius, mMargin))
                .into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }



    //create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvScreenName;
        public TextView tvBody;
        public TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookup
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvScreenName= (TextView) itemView.findViewById(R.id.tvScreenname);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

        }
    }

    public class ViewHolderWithImage extends RecyclerView.ViewHolder
    {

        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvScreenName;
        public TextView tvBody;
        public TextView tvDate;
        public ImageView ivTweetImage;

        public ViewHolderWithImage(View itemView) {
            super(itemView);

            //perform findViewById lookup
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvScreenName= (TextView) itemView.findViewById(R.id.tvScreenname);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            ivTweetImage = (ImageView) itemView.findViewById(R.id.ivTweetImage);

        }
    }
}
