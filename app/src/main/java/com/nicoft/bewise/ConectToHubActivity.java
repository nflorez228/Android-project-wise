package com.nicoft.bewise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class ConectToHubActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        addSlide(AppIntroFragment.newInstance("", getResources().getString(R.string.FirstSlideDescription), R.drawable.h2a, Color.parseColor("#E91E63")));
        addSlide(AppIntroFragment.newInstance("", getResources().getString(R.string.SecondSlideDescription), R.drawable.h3a, Color.parseColor("#673AB7")));
        addSlide(AppIntroFragment.newInstance("", getResources().getString(R.string.ThirdSlideDescription), R.drawable.h4a, Color.parseColor("#2196F3")));
        addSlide(AppIntroFragment.newInstance("", getResources().getString(R.string.FourthSlideDescription), R.drawable.h5a, Color.parseColor("#03A9F4")));


        if(getWifiName(getApplicationContext()).equals(""))
        {
            addSlide(AppIntroFragment.newInstance("", getResources().getString(R.string.FifthSlideDescriptionNoWifi), R.drawable.giphy, Color.parseColor("#009688")));
        }
        else
        {
            addSlide(AppIntroFragment.newInstance("", getResources().getString(R.string.FifthSlideDEscriptionWifi1) + "\n" + getWifiName(getApplicationContext()) + "\n"+getResources().getString(R.string.FifthSlideDescriptionWifi2), R.drawable.giphy, Color.parseColor("#009688")));
        }


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


    }

    public String getWifiName(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return "";
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int highScore = sharedPref.getInt("NoPages", 0);
        //Toast.makeText(getApplicationContext(),highScore+"sa",Toast.LENGTH_LONG).show();
        String wifi = getWifiName(getApplicationContext());
        if(!wifi.equals("") && highScore>0)
        {
            Intent intent = new Intent(this, ConectToHubActivity2.class);
            startActivity(intent);
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
        if(getWifiName(getApplicationContext()).equals(""))
        {
            startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
        }
        else
        {
            Intent intent = new Intent(this, ConectToHubActivity2.class);
            startActivity(intent);
            //Toast.makeText(getApplicationContext(),"no ",Toast.LENGTH_LONG).show();
        }
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.

        if(newFragment!=null) {
            if (newFragment.toString().endsWith("4}")) {
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("NoPages", 1);
                editor.commit();
                //Toast.makeText(getApplicationContext(), ((AppIntroFragment) newFragment).toString(), Toast.LENGTH_LONG).show();
            }
        }

    }
}
