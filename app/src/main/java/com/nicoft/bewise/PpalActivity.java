package com.nicoft.bewise;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class PpalActivity extends ActionBarActivity {


    DatabaseLoginHelper myDb2;
    private Socket socket;
    WebView myWebView;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(PpalActivity.this,
                R.style.AppTheme_Dark_Dialog);

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(getResources().getString(R.string.AuthenticatingDialog));
        progressDialog.setCancelable(false);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        progressDialog.show();
                    }
                }, 250);

        myDb2 = new DatabaseLoginHelper(this);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_main2);
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        myWebView = (WebView) this.findViewById(R.id.webView);
        myWebView.clearCache(true);
        myWebView.setWebChromeClient(new WebChromeClient(){


            final ProgressDialog progressDialog = new ProgressDialog(PpalActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            public void onProgressChanged(WebView view, int progress)
            {
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setProgressNumberFormat(null);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setIndeterminate(false);
                    progressDialog.setProgress(progress);
                    progressDialog.setMessage(getResources().getString(R.string.LoadingDialog));
                    progressDialog.show();

                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle(getResources().getString(R.string.LoadingDialog));
                setProgress(progress * 100); //Make the bar disappear after URL is loaded
                //Toast.makeText(getApplicationContext(),progress,Toast.LENGTH_SHORT).show();
                Log.d("PRogress", "Refreshed token: " + progress);


                // Return the app name after finish loading
                if(progress == 100) {
                    setTitle(R.string.app_name);
                    progressDialog.dismiss();
                }
            }
        });
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //myWebView.loadUrl(getIntent().getExtras().getString("URL"));
        //myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);




        if(getWifiName(getApplicationContext()).equals("")) {
            Cursor res = myDb2.getAllData();
            res.moveToNext();

            try {
                socket = IO.socket(getIntent().getExtras().getString("URL"));
                socket.connect();
                socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                JSONObject obj = new JSONObject("{username:\"" + res.getString(2) + "\",password:\"" + res.getString(3) + "\"}");
                socket.emit("authentication", obj);
                socket.on("authenticated", onAuthenticated);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            progressDialog.dismiss();
            //myWebView.loadUrl("http://"+getIntent().getExtras().getString("NAME"));
            //myWebView.loadUrl("http://bewisehub");
            checkInternalIP();
        }

    }

    public void checkInternalIP()
    {
        String ip = getWifiIP(this);
        ip = ip.substring(0,ip.lastIndexOf(".")+1);
        AsyncTask<String, Integer, String> asy = new AsyncURL5(this,1).execute("http://" + ip + "200/");
    }

    public void onAsyncResultAquired(String result, int nTaskNum)
    {
        //Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        if(nTaskNum<=20) {
            String ip = getWifiIP(this);
            ip = ip.substring(0,ip.lastIndexOf(".")+1);
            if (!result.contains("socket.io") && !result.contains("ionicons.min")) {

                if (nTaskNum < 10) {
                    AsyncTask<String, Integer, String> asy = new AsyncURL5(this, nTaskNum + 1).execute("http://" + ip + "20" + nTaskNum + "/");
                } else {
                    AsyncTask<String, Integer, String> asy = new AsyncURL5(this, nTaskNum + 1).execute("http://" + ip + "2" + nTaskNum + "/");
                }
            }
            else
            {

                String url = "";
                if (nTaskNum <= 10) {

                    //myWebView.loadUrl("http://" + ip + "20" + (nTaskNum - 1) + "/");
                    url="http://" + ip + "20" + (nTaskNum - 1) + "/";
                } else
                {
                    //myWebView.loadUrl("http://" + ip + "2" + (nTaskNum - 1) + "/");
                    url="http://" + ip + "2" + (nTaskNum - 1) + "/";
                }

                Cursor res = myDb2.getAllData();
                res.moveToNext();
                try {
                    socket = IO.socket(url);
                    socket.connect();
                    socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                    socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                    JSONObject obj = new JSONObject("{username:\"" + res.getString(2) + "\",password:\"" + res.getString(3) + "\"}");
                    socket.emit("authentication", obj);
                    socket.on("authenticated", onAuthenticated);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        else
        {
            Cursor res = myDb2.getAllData();
            res.moveToNext();
            try {
                socket = IO.socket(getIntent().getExtras().getString("URL"));
                socket.connect();
                socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                JSONObject obj = new JSONObject("{username:\"" + res.getString(2) + "\",password:\"" + res.getString(3) + "\"}");
                socket.emit("authentication", obj);
                socket.on("authenticated", onAuthenticated);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Error conectando a socket.io", Toast.LENGTH_SHORT).show();
                    socket.disconnect();
                    progressDialog.dismiss();
                    progressDialog.setMessage(getResources().getString(R.string.FailedSocket));
                    progressDialog.show();
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // onLoginFailed();
                                    progressDialog.dismiss();
                                    finish();
                                }
                            }, 2000);
                }
            });
        }
    };

    private Emitter.Listener onAuthenticated = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socket.emit("authenticated");
                    Toast.makeText(getBaseContext(), "Authenticated", Toast.LENGTH_SHORT).show();
                    socket.on("SETCUN",onSetCun);
                    //sendImage();
                }
            });
        }
    };

    private Emitter.Listener onSetCun = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String cun= (String) args[0];
                    Toast.makeText(getApplicationContext(),cun,Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                    socket.disconnect();
                    socket.close();

                    //myWebView.loadUrl(getIntent().getExtras().getString("URL") + "/mobile.html?cun=" + cun);
                    if(getIntent().getExtras().getString("EURL")!=null) {
                        myWebView.loadUrl(getIntent().getExtras().getString("URL") + getIntent().getExtras().getString("EURL") + "&cun=" + cun);
                        Toast.makeText(getApplicationContext(),getIntent().getExtras().getString("URL") + getIntent().getExtras().getString("EURL") + "&cun=" + cun,Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        myWebView.loadUrl(getIntent().getExtras().getString("URL") + "?cun=" + cun);
                    }

                }
            });
        }
    };

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onResume(){
        super.onResume();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (myWebView.canGoBack()) {
                        myWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
        myWebView.stopLoading();
    }


    public void sendImage(String path)
    {
        JSONObject sendData = new JSONObject();
        try{
            sendData.put("image", encodeImage(path));
            socket.emit("SETIMG",sendData);
        }catch(JSONException e){
        }
    }

    private String encodeImage(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }

}
