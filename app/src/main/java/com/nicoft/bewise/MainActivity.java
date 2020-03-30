package com.nicoft.bewise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    DatabaseLoginHelper myDb;
    DatabaseHelper myDb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        myDb = new DatabaseLoginHelper(this);
        myDb2 = new DatabaseHelper(this);


        Intent intent = new Intent(this, FlexibleSpaceWithImageWithViewPagerTabActivity.class);
        //Intent intent = new Intent(this, TestActivity.class);

        //startActivity(intent);

        //checkLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onResume(){
        super.onResume();
        checkLogin();

    }

    private void checkLogin(){


        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);

            return;
        }
        StringBuffer buffer = new StringBuffer();
        res.moveToNext();
            if(!res.getString(4).equals("YES")) {
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
                //break;
            }
            else
            {
                Cursor res2 = myDb2.getAllData(DatabaseHelper.TABLE_NAME_1);
                if (res2.getCount() == 0) {
                    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("NoPages", 0);
                    editor.commit();
                    //Toast.makeText(this.getApplicationContext(),"Main",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ChooseFirstHub.class);
                    startActivity(intent);

                    return;
                }
                else {
                    //Intent intent = new Intent(this, FlexibleSpaceWithImageWithViewPagerTabActivity.class);
                    Intent intent = new Intent(this, ChooserHouse.class);
                    startActivity(intent);
                    //break;
                }
            }





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
