package com.nicoft.bewise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class FirstPlaceActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_place);
        Button but= (Button) findViewById(R.id.buttonAdd1Home);
        final EditText nom = (EditText) findViewById(R.id.editTextNombreHome);


        myDb = new DatabaseHelper(this);

        String[] TITLES = new String[]{"House","Office"};


        final Spinner spin =(Spinner) findViewById(R.id.spinnerHomeTypes);
        spin.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TITLES));


        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean rta=myDb.insertData(DatabaseHelper.TABLE_NAME_1, nom.getText().toString(), spin.getSelectedItem().toString());
                if(rta){
                    Toast.makeText(getApplicationContext(),"Agregado",Toast.LENGTH_SHORT).show();
                    btnCheck();
                }
            }
        });
    }

    public void btnCheck()
    {
        finish();
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
