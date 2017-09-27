package com.codepath.apps.restclienttemplate.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.User;

public class CreateTweetActivity extends AppCompatActivity {

    private Button btnCancel;
    private Button btnSend;
    private ImageView ivUserProfile;
    private TextView tvScreenName;
    private EditText etTweetText;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tweet);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSend = (Button) findViewById(R.id.btnSend);
        ivUserProfile = (ImageView) findViewById(R.id.ivUserPicture);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        etTweetText = (EditText) findViewById(R.id.etMessage);

        Intent intent = getIntent();

        user = (User) intent.getSerializableExtra("myUser");
        tvScreenName.setText("@" + user.mScreenName);

        Glide.with(this).load(user.mProfileImageUrl).into(ivUserProfile);

    }

    public void onClickCancelButton(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onClickSendButton(View view) {

        String text = etTweetText.getText().toString();
        Intent intent = new Intent();

        intent.putExtra("tweet", text);

        setResult(RESULT_OK, intent);
        finish();
    }
}
