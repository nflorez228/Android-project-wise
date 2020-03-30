package com.nicoft.bewise;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.util.concurrent.ExecutionException;

public class ConectToHubActivity2 extends AppIntro2 {

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
        addSlide(AppIntroFragment.newInstance("", getResources().getString(R.string.SixthSideDescription), R.drawable.giphy, Color.parseColor("#8BC34A")));
        //addSlide(AppIntroFragment.newInstance("", "Your bewise hub is up and running. lets give a name to your place. This name will be the name of your bewisehub to the world.", R.drawable.giphy, Color.parseColor("#4CAF50")));

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
        progressDialog= new ProgressDialog(ConectToHubActivity2.this,
                R.style.AppTheme_Dark_Dialog);
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

    public void onAsyncResultAquired(String result, final int nTaskNum)
    {
        //Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        if(nTaskNum==1 || nTaskNum==3)
        {
            if(result.isEmpty() && nTaskNum==1)
            {
                AsyncTask<String, Integer, String> asy = new AsyncURL2(this,3).execute("https://api.ipify.org/");
            }
            else if(result.isEmpty() && nTaskNum==3)
            {
                AsyncTask<String, Integer, String> asy = new AsyncURL2(this,3).execute("http://ipecho.net/plain");
            }
            else {
                result = result.replaceAll("\\n", "");
                result = "http://" + result;
                result = result + ".en.bewise.com.co";
                URLIPext = result;
                //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
                AsyncTask<String, Integer, String> asy = new AsyncURL2(this, 2).execute(result);
            }
        }
        else if(nTaskNum==2) {
            progressDialog.dismiss();
            if (result.contains("not found")) {
                progressDialog.setMessage(getResources().getString(R.string.HubNotFoundNetworkDialog));
                progressDialog.show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // onLoginFailed();
                                progressDialog.dismiss();
                            }
                        }, 3000);
            }
            else
            {
                progressDialog.setMessage(getResources().getString(R.string.HubFoundNetworkDialog));
                progressDialog.show();
                //Toast.makeText(getBaseContext(), "BewiseHub found now lets configure it", Toast.LENGTH_LONG).show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // onLoginFailed();
                                progressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), ConectToHubActivity3.class);
                                intent.putExtra("URLBef", URLIPext);
                                startActivity(intent);
                            }
                        }, 3000);
            }
        }
        else if(nTaskNum==4) {
            progressDialog.dismiss();

            if(!result.equals("") && result.contains("CONFIGURED"))
            {
                progressDialog.setMessage(getResources().getString(R.string.HubFoundNetworkDialog));
                progressDialog.show();
                //Toast.makeText(getBaseContext(), "BewiseHub found now lets configure it", Toast.LENGTH_LONG).show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // onLoginFailed();
                                progressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), ConectToHubActivity3.class);
                                intent.putExtra("URLBef", "http://bewisehub");
                                startActivity(intent);
                            }
                        }, 3000);
            }
            else {
                progressDialog.setMessage(getResources().getString(R.string.HubNotFoundNetworkDialog));
                progressDialog.show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // onLoginFailed();
                                progressDialog.dismiss();
                            }
                        }, 3000);
            }
        }
        else if(nTaskNum>=200)
        {
            progressDialog.dismiss();

            String temp = getWifiIP(this);
            final String ip = temp.substring(0,temp.lastIndexOf(".")+1);
            if ((!result.contains("socket.io") && !result.contains("ionicons.min")) || (result.contains("socket.io") && result.contains("ionicons.min") && result.contains("CONFIGURED"))) {

                if(nTaskNum<=220) {
                    progressDialog.setMessage("Retrying... "+ (nTaskNum-199));
                    progressDialog.show();

                    AsyncTask<String, Integer, String> asy = new AsyncURL2(this, nTaskNum + 1).execute("http://" + ip + (nTaskNum+1) + "/");
                }
                else
                {
                    progressDialog.setMessage(getResources().getString(R.string.HubNotFoundNetworkDialog));
                    progressDialog.show();
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // onLoginFailed();
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                }
            }
            else
            {
                progressDialog.setMessage(getResources().getString(R.string.HubFoundNetworkDialog));
                progressDialog.show();
                //Toast.makeText(getBaseContext(), "BewiseHub found now lets configure it", Toast.LENGTH_LONG).show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // onLoginFailed();
                                progressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), ConectToHubActivity3.class);
                                intent.putExtra("URLBef", "http://" + ip + nTaskNum +"/");
                                startActivity(intent);
                            }
                        }, 3000);

            }
        }
    }

    public String getWifiIP(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    int ip = wifiInfo.getIpAddress();
                    return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
                }
            }
        }
        return "";
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        if(currentFragment.toString().endsWith("3}"))
        {
            startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
        }
        else
        {

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getResources().getString(R.string.SearchingHubNetworkDialog));
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);

            //AsyncTask<String, Integer, String> asy = new AsyncURL2(this,1).execute("https://wtfismyip.com/text");

            //AsyncTask<String, Integer, String> asy2 = new AsyncURL2(this,4).execute("http://bewisehub");

            String ip = getWifiIP(this);
            ip = ip.substring(0,ip.lastIndexOf(".")+1);
            AsyncTask<String, Integer, String> asy = new AsyncURL2(this,200).execute("http://" + ip + "200/");
            //AsyncTask<String, Integer, String> asy2 = new AsyncURL2(this,5).execute("http://192.168.0.255");



            /*
            final NsdManager mNsdManager = (NsdManager) getApplicationContext().getSystemService(Context.NSD_SERVICE);

            NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener()
            {

                @Override
                public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                    Log.e("nsd", "Discovery failed: Error code:" + errorCode);
                    mNsdManager.stopServiceDiscovery(this);
                }

                @Override
                public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                    Log.e("nsd", "Discovery failed: Error code:" + errorCode);
                    mNsdManager.stopServiceDiscovery(this);
                }

                @Override
                public void onDiscoveryStarted(String serviceType) {
                    Log.d("nsd", "Service discovery started");
                }

                @Override
                public void onDiscoveryStopped(String serviceType) {
                    Log.i("nsd", "Discovery stopped: " + serviceType);
                }

                @Override
                public void onServiceFound(NsdServiceInfo serviceInfo) {
                    Log.d("nsd", "Service discovery success" + serviceInfo);
                }

                @Override
                public void onServiceLost(NsdServiceInfo serviceInfo) {

                }
            };
            final String SERVICE_TYPE = "_http._tcp.";
            mNsdManager.discoverServices(
                    SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
            */

            //progressDialog.dismiss();


            //Toast.makeText(getBaseContext(), "Search raspi", Toast.LENGTH_LONG).show();
            /*new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {


                            String status = "";
                            try {
                                while (status.equals("")) {
                                    AsyncTask<String, Integer, String> asy = new AsyncURL().execute("https://wtfismyip.com/text");
                                    status = asy.get();
                                    asy.cancel(true);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            status = status.replaceAll("\\n", "");
                            status = "http://" + status;
                            status = status + ".en.bewise.com.co";
                            //Toast.makeText(getBaseContext(), status, Toast.LENGTH_LONG).show();


                            boolean raspifound = false;
                            String status2 = "";
                            try {
                                while (status2.equals("")) {
                                    AsyncTask<String, Integer, String> asy = new AsyncURL().execute(status);
                                    status2 = asy.get();
                                    asy.cancel(true);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            if (status2.contains("not found")) {
                                progressDialog.setMessage("BewiseHub not found retrying...");
                                status2 = "";
                                try {
                                    while (status2.equals("")) {
                                        AsyncTask<String, Integer, String> asy = new AsyncURL().execute(status);
                                        status2 = asy.get();
                                        asy.cancel(true);
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                                //Toast.makeText(getBaseContext(), "BewiseHub not found retrying...", Toast.LENGTH_LONG).show();

                            }
                            if(!status2.contains("not found")){

                                progressDialog.setMessage("BewiseHub found.");
                                //Toast.makeText(getBaseContext(), "BewiseHub found now lets configure it", Toast.LENGTH_LONG).show();
                                raspifound = true;
                            }
                            else
                            {
                                progressDialog.setMessage("BewiseHub not found.");


                            }


                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            // onLoginFailed();
                                            progressDialog.dismiss();
                                        }
                                    }, 3000);
                            if (raspifound) {
                                Intent intent = new Intent(getApplicationContext(), HubConnectedActivity.class);
                                intent.putExtra("URLBef", status);
                                startActivity(intent);
                            }
                        }
                    },1000);
                    */


            Intent intent = new Intent(this, ConectToHubActivity2.class);
            //startActivity(intent);

            //Toast.makeText(getApplicationContext(),"no ",Toast.LENGTH_LONG).show();
        }
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
        //Toast.makeText(getApplicationContext(),((AppIntroFragment)newFragment).toString(),Toast.LENGTH_LONG).show();

    }
}
