package com.nicoft.bewise;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class AskForWifiActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    Button but;

    int temp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_wifi);
        myDb = new DatabaseHelper(this);

        //startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
        but= (Button) findViewById(R.id.buttonConnectHubWifi);

        Cursor res = myDb.getAllData(DatabaseHelper.TABLE_NAME_1);
        temp=res.getCount();

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                TextView nom = (TextView) findViewById(R.id.textViewWifiName);

                if (nom.getText() == "") {
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                    Toast.makeText(getBaseContext(), "Please select your wifi network", Toast.LENGTH_LONG).show();

                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(AskForWifiActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Searching for BewiseHub on your Network");
                    progressDialog.show();
                    //Toast.makeText(getBaseContext(), "Search raspi", Toast.LENGTH_LONG).show();
                    new android.os.Handler().postDelayed(
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

                }

            }
        });
        TextView nom = (TextView) findViewById(R.id.textViewWifiName);
        nom.setText(getWifiName(getApplicationContext()));


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
    public void onResume(){
        super.onResume();

        Cursor res2 = myDb.getAllData(DatabaseHelper.TABLE_NAME_1);
        if (res2.getCount() == temp) {
            TextView nom = (TextView) findViewById(R.id.textViewWifiName);
            nom.setText(getWifiName(getApplicationContext()));
        }
        else
        {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_place, menu);
        return true;
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
}
