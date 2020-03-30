package com.nicoft.bewise;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_LOGIN = 1;

    @InjectView(R.id.btn_login) Button _loginButton;
    //@InjectView(R.id.link_signup) TextView _signupLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        VideoView videoView = (VideoView) findViewById(R.id.videoSplashView);

        Uri path = Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.tour_video);

        videoView.setVideoURI(path);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        ButterKnife.inject(this);
        
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        /*_signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                //Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                Intent intent = new Intent(getApplicationContext(), ChooseFirstHub.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                //finish();
            }
        });*/
    }

    @Override
    public void onResume()
    {
        super.onResume();
        VideoView videoView = (VideoView) findViewById(R.id.videoSplashView);

        Uri path = Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.tour_video);

        videoView.setVideoURI(path);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        ButterKnife.inject(this);
    }

    public void login() {
        Log.d(TAG, "Login");
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
        //finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP || requestCode == REQUEST_LOGIN ) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                finish();

            }
        }
    }



    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }




}
