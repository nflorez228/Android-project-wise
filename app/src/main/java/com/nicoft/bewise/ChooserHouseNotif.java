package com.nicoft.bewise;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blunderer.materialdesignlibrary.views.CardView;
import com.getbase.floatingactionbutton.FloatingActionButton;

public class ChooserHouseNotif extends Activity implements Fragment_ChooserHouse.OnFragmentInteractionListener{

    DatabaseHelper myDb;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooserhouse);

        Toast.makeText(this,getIntent().getExtras().getString("Noti") , Toast.LENGTH_SHORT).show();

        myDb = new DatabaseHelper(this);

        LinearLayout ly = (LinearLayout) findViewById(R.id.LinearLayChooseHouse);
        Fragment_ChooserHouse fgch = new Fragment_ChooserHouse();

        Cursor res = myDb.getAllData(DatabaseHelper.TABLE_NAME_1);

        CardView first=null;
        int counter=0;
        String firstULR="";
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

            if(getIntent().getExtras().getString("Noti")!=null) {
                if (getIntent().getExtras().getString("Noti").split(":")[0].equalsIgnoreCase(Name)) {
                    first = cv;
                    firstULR = URL;
                }
            }
            //ly.addView(cv);
        }

        if(getIntent().getExtras().getString("Noti")!=null)
        {
            Intent intent = new Intent(getApplicationContext(), PpalActivity.class);
            intent.putExtra("URL",firstULR);
            intent.putExtra("NAME",getIntent().getExtras().getString("Noti").split(":")[0]);
            intent.putExtra("EURL",getIntent().getExtras().getString("Noti").split(":")[1]);
            startActivity(intent);
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
                startActivity(intent);//TODO make about us
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }


}
