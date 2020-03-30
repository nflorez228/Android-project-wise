package com.nicoft.bewise;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blunderer.materialdesignlibrary.views.CardView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.rey.material.app.Dialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChooserHouse extends Activity implements Fragment_ChooserHouse.OnFragmentInteractionListener  {

    DatabaseHelper myDb;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    CardView first=null;

    String firstULR="";

    DatabaseLoginHelper myDb2;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooserhouse);


        myDb2 = new DatabaseLoginHelper(this);

        myDb = new DatabaseHelper(this);

        LinearLayout ly = (LinearLayout) findViewById(R.id.LinearLayChooseHouse);
        Fragment_ChooserHouse fgch = new Fragment_ChooserHouse();

        //getFragmentManager().beginTransaction().add(R.id.LinearLayChooseHouse,fgch.newInstance("a","b")).commit();


        Cursor res = myDb.getAllData(DatabaseHelper.TABLE_NAME_1);

        int counter=0;
        while(res.moveToNext()) {
            counter = counter+1;

            CardView cv = new CardView(this);
            final String Name=res.getString(1);
            final String URL=res.getString(3);
            final String Type=res.getString(2);
            cv.setImagePosition(1);
            cv.setDescription("Over Internet:\n  "+URL+"\nOver WiFi:\n"+"  internalip"+" or http://bewisehub/");
            cv.setTitle(Name);
            cv.setNormalButtonText("Go");
            cv.setHighlightButtonText("Configure");
            Drawable icon = getResources().getDrawable(R.drawable.a);

            cv.setImageDrawable(icon);
            //cv.setImageResource(R.drawable.ic_home_white_48dp);
            cv.setOnNormalButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PpalActivity.class);
                    intent.putExtra("URL", URL);
                    intent.putExtra("NAME", Name);
                    startActivity(intent);

                }
            });
            cv.setOnHighlightButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PpalActivity.class);
                    intent.putExtra("URL", URL);
                    intent.putExtra("NAME", Name);
                    intent.putExtra("EURL", "?config=1");
                    startActivity(intent);
                }
            });

            getFragmentManager().beginTransaction().add(R.id.LinearLayChooseHouse,fgch.newInstance(Name,"Over Internet:\n  "+URL+"\nOver WiFi:\n"+"  internalip"+" or http://bewisehub/",URL,R.drawable.e,"?config=1")).commit();

            if(counter==1)
            {
                first=cv;
                firstULR=URL;
            }
            //ly.addView(cv);
        }

        if(counter==1)
        {
            /*Intent intent = new Intent(getApplicationContext(), PpalActivity.class);
            intent.putExtra("URL",firstULR);
            intent.putExtra("NAME",first.getTitle());
            startActivity(intent);*/
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {

                            startFirstHouse();
                        }
                    }, 300);
        }

        //ly.addView(cv);

        FloatingActionButton addhouse = (FloatingActionButton) findViewById(R.id.accion_add);
        addhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseFirstHub.class);
                startActivity(intent);
            }
        });

        FloatingActionButton config = (FloatingActionButton) findViewById(R.id.accion_config);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseFirstHub.class);
                startActivity(intent);//TODO make config
            }
        });

        FloatingActionButton store = (FloatingActionButton) findViewById(R.id.accion_comprar);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.bewise.com.co/store";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        FloatingActionButton about = (FloatingActionButton) findViewById(R.id.accion_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseFirstHub.class);
                //startActivity(intent);//TODO make about us
                Cursor res = myDb2.getAllData();
                res.moveToNext();

                try {
                    socket = IO.socket(firstULR);
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
        });

    }

    public void startFirstHouse()
    {
        Intent intent = new Intent(getApplicationContext(), PpalActivity.class);
        intent.putExtra("URL",firstULR);
        intent.putExtra("NAME",first.getTitle());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
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
        File mFile2 = new File("/storage/emulated/0","e.jpg");


        String sdPath = mFile2.getAbsolutePath().toString();

        Log.i("hiya", "Your IMAGE ABSOLUTE PATH:-"+sdPath);

        File temp=new File(sdPath);

        if(!temp.exists()){
            Log.e("file","no image file at location :"+sdPath);
        }

        File imagefile = mFile2;
        //File imagefile = new File(getFilesDir().getPath());
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


    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Error conectando a socket.io", Toast.LENGTH_SHORT).show();
                    socket.disconnect();
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
                    //sendImage();
                    sendImage("android.resource://" + getPackageName() + "/" + R.drawable.h1a);
                }
            });
        }
    };
}
