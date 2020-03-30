package com.nicoft.bewise;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class PrincipalActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topdrawer);

        myDb = new DatabaseHelper(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);



        viewAll(DatabaseHelper.TABLE_NAME_2);

        //myDb.updateData(myDb.TABLE_NAME_2,"1","NICOLAS","tipoN","Piso 1","22");
        /*Button boton = (Button) findViewById(R.id.button);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //boolean rta = myDb.insertData(myDb.TABLE_NAME_1,"nicolas","tipoN");
                //boolean rta = myDb.insertData(myDb.TABLE_NAME_2,"nicolas","tipoN","Piso1","23");
                //boolean rta = myDb.insertData(myDb.TABLE_NAME_3,"nicolas","1","EstadoN","tiempox","2234");
                boolean rta = myDb.insertData(myDb.TABLE_NAME_4,"nicolas","categoriax");

                if (rta)
                {
                    Toast.makeText(getApplicationContext(),"insertado",Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }

    public void viewAll(final String table){
        Button boton = (Button) findViewById(R.id.button);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                /*Cursor res = myDb.getAllData(table);
                if (res.getCount() == 0) {
                    showMessage("Error","Nothing Found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    switch (table) {
                        case DatabaseHelper.TABLE_NAME_1:
                            buffer.append("ID:" + res.getString(0) + "\n");
                            buffer.append("Nombre:" + res.getString(1) + "\n");
                            buffer.append("Tipo:" + res.getString(2) + "\n\n");
                            break;
                        case DatabaseHelper.TABLE_NAME_2:
                            buffer.append("ID:" + res.getString(0) + "\n");
                            buffer.append("Nombre:" + res.getString(1) + "\n");
                            buffer.append("Tipo:" + res.getString(2) + "\n");
                            buffer.append("Piso:" + res.getString(3) + "\n");
                            buffer.append("IDLugares:" + res.getString(4) + "\n\n");
                            break;
                        case DatabaseHelper.TABLE_NAME_3:
                            buffer.append("ID:" + res.getString(0) + "\n");
                            buffer.append("Nombre:" + res.getString(1) + "\n");
                            buffer.append("Tipo:" + res.getString(2) + "\n");
                            buffer.append("Estado:" + res.getString(3) + "\n");
                            buffer.append("Time:" + res.getString(4) + "\n");
                            buffer.append("IDHabitacion:" + res.getString(5) + "\n\n");
                            break;
                        case DatabaseHelper.TABLE_NAME_4:
                            buffer.append("ID:" + res.getString(0) + "\n");
                            buffer.append("Nombre:" + res.getString(1) + "\n");
                            buffer.append("Categoria:" + res.getString(2) + "\n\n");
                            break;
                    }
                }

                showMessage("Data",buffer.toString());
            */
            

            }


        });
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            moveTaskToBack(true);
    }


}

