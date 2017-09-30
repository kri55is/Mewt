package com.codepath.apps.restclienttemplate.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class CreateTweetActivity extends AppCompatActivity {

    private Button mBtnCancel;
    private Button mBtnSend;
    private ImageView mIvUserProfile;
    private TextView mTvScreenName;
    private EditText mEtTweetText;
    private TextView mTvCharLeft;

    private User mUser;

    private int mRadius = 30; // corner radius, higher value = more rounded
    private int mMargin = 5; // crop margin, set to 0 for corners with no crop

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tweet);

        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mBtnSend = (Button) findViewById(R.id.btnSend);
        mIvUserProfile = (ImageView) findViewById(R.id.ivUserPicture);
        mTvScreenName = (TextView) findViewById(R.id.tvScreenName);
        mEtTweetText = (EditText) findViewById(R.id.etMessage);
        mTvCharLeft = (TextView) findViewById(R.id.tvCharLeft);

        Intent intent = getIntent();

        mUser = (User) Parcels.unwrap(intent.getParcelableExtra("myUser"));
        mTvScreenName.setText("@" + mUser.mScreenName);

        Glide.with(this)
                .load(mUser.mProfileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(this, mRadius, mMargin))
                .into(mIvUserProfile);

        mEtTweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int charLeft = Tweet.TWEET_MAX_SIZE - s.length();
                refreshCount(charLeft);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void refreshCount(int charLeft){
        String numCharLeft = "Char left: ";
        numCharLeft +=Integer.toString(charLeft);
        mTvCharLeft.setText(numCharLeft);
    }

    public void onClickCancelButton(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onClickSendButton(View view) {

        String text = mEtTweetText.getText().toString();

        if(text.length() <= 140) {
            Intent intent = new Intent();

            intent.putExtra("tweet", text);

            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            Toast.makeText(this, "Tweets are limited to 140 characters", Toast.LENGTH_SHORT).show();
        }

    }
}
