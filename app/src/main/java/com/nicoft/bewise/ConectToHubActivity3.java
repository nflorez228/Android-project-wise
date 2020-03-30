package com.nicoft.bewise;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class ConectToHubActivity3 extends AppIntro2 {

    ProgressDialog progressDialog;
    String URLIPext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("NoPages", 0);
        editor.commit();
        int highScore = sharedPref.getInt("NoPages", 0);
        //Toast.makeText(getApplicationContext(),highScore+"sa",Toast.LENGTH_LONG).show();


        setZoomAnimation();

        setImmersiveMode(true);
        setGoBackLock(true);
        setColorTransitionsEnabled(true);

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        /*addSlide(R.layout.contecthub1);
        addSlide(second_fragment);
        addSlide(third_fragment);
        addSlide(fourth_fragment);*/

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        //addSlide(AppIntroFragment.newInstance("", "Now we will look and try to comunicate with your bewisehub.", R.drawable.giphy, Color.parseColor("#4CAF50")));
        addSlide(AppIntroFragment.newInstance("", getResources().getString(R.string.SeventhSideDescription), R.drawable.giphy, Color.parseColor("#FF9800")));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        //showSkipButton(false);
        setProgressButtonEnabled(true);
        this.skipButtonEnabled =false;

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
        progressDialog= new ProgressDialog(ConectToHubActivity3.this,
                R.style.AppTheme_Dark_Dialog);
    }



    @Override
    public void onResume() {
        super.onResume();
        //getWifiName(getApplicationContext());
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int value = sharedPref.getInt("NoPages",0);
        //Toast.makeText(this.getApplicationContext(),value+"as",Toast.LENGTH_LONG).show();
        if(value==60)
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("NoPages", 0);
            editor.commit();
            finish();
        }
    }

        @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(), HubConnectedActivity.class);
        intent.putExtra("URLBef", getIntent().getExtras().getString("URLBef"));
        startActivity(intent);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
        //Toast.makeText(getApplicationContext(),((AppIntroFragment)newFragment).toString(),Toast.LENGTH_LONG).show();

    }
}
