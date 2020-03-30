package com.nicoft.bewise;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HubConnectedActivity extends AppCompatActivity {

    DatabaseLoginHelper myDb2;

    DatabaseHelper myDb;
    String str="";
    private Socket socket;
    ProgressDialog progressDialog;
    String URLDefHome;

    private ImageView imageView;
    private Uri file;

    //@InjectView(R.id.picNewPlace) ImageView takePictureButton;

    @InjectView(R.id.buttonAdd1Home) Button _but;
    @InjectView(R.id.editTextNombreHome) EditText _nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubconnected);
        //Button but= (Button) findViewById(R.id.buttonAdd1Home);
        //EditText nom = (EditText) findViewById(R.id.editTextNombreHome);

        ButterKnife.inject(this);

        /*takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });*/

        myDb = new DatabaseHelper(this);

        myDb2 = new DatabaseLoginHelper(this);

        String[] TITLES = new String[]{"House","Office"};

        progressDialog= new ProgressDialog(HubConnectedActivity.this,
                R.style.AppTheme_Dark_Dialog);

        //final Spinner spin =(Spinner) findViewById(R.id.spinnerHomeTypes);
        //spin.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TITLES));

        com.rey.material.widget.Spinner tipoPlace = (com.rey.material.widget.Spinner)findViewById(R.id.spinnerHomeTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_spn, TITLES);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        tipoPlace.setAdapter(adapter);

        _but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                butonclick();
            }
        });
    }

    private void butonclick() {
        if (!validate()) {
            return;
        }
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.VerifyingHubNameDialog));
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        //EditText nombre = (EditText) findViewById(R.id.editTextNombreHome);

        //Toast.makeText(getBaseContext(), "http://" + _nom.getText() + ".en.bewise.com.co", Toast.LENGTH_SHORT).show();

        String status = "";
        AsyncTask<String, Integer, String> asy = new AsyncURL3(this,1).execute("http://" + _nom.getText() + ".en.bewise.com.co");

        /*try {
            while(status.equals("") ) {
                AsyncTask<String, Integer, String> asy = new AsyncURL().execute("http://"+nombre.getText()+".en.bewise.com.co");
                status = asy.get();
                asy.cancel(true);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Toast.makeText(getBaseContext(), status, Toast.LENGTH_LONG).show();

        Toast.makeText(getBaseContext(), getIntent().getExtras().getString("URLBef"), Toast.LENGTH_SHORT).show();
        */
    }
    public void onAsyncResultAquired(String result, int nTaskNum)
    {
        if(nTaskNum==1)
        {
            if (result.contains("unavailable at the moment")) {
                //Toast.makeText(getBaseContext(), "Available!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                progressDialog.setMessage(getResources().getString(R.string.AuthenticatingOnHubDialog));
                progressDialog.show();
                try {
                    socket = IO.socket(getIntent().getExtras().getString("URLBef"));
                    socket.connect();
                    socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                    socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                    JSONObject obj = new JSONObject("{username:\"bewise\",password:\"bewise\"}");
                    socket.emit("authentication", obj);
                    socket.on("authenticated", onAuthenticated);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Toast.makeText(getBaseContext(), "not available :(", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                progressDialog.setMessage(getResources().getString(R.string.NameNotAvailableDialog));
                progressDialog.show();
                //TODO set text color red...
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // onLoginFailed();
                                progressDialog.dismiss();
                            }
                        }, 2000);
            }
        /*
        boolean rta=myDb.insertData(DatabaseHelper.TABLE_NAME_1, nom.getText().toString(), spin.getSelectedItem().toString());
        if(rta){
            Toast.makeText(getApplicationContext(),"Agregado",Toast.LENGTH_SHORT).show();
            btnCheck();
        }
        */
        }
        else if(nTaskNum==2)
        {
            if(result.equals("") || result.contains("unavailable at the moment"))
            {
                AsyncTask<String, Integer, String> asy = new AsyncURL3(this,2).execute(URLDefHome);

            }
            else
            {
                //EditText nombre = (EditText) findViewById(R.id.editTextNombreHome);
                //Toast.makeText(getBaseContext(),"Tunel creado",Toast.LENGTH_LONG).show();
                com.rey.material.widget.Spinner spin = (com.rey.material.widget.Spinner)findViewById(R.id.spinnerHomeTypes);
                boolean rta=myDb.insertData(DatabaseHelper.TABLE_NAME_1, _nom.getText().toString(), spin.getSelectedItem().toString(),URLDefHome);
                if(rta){
                    //Toast.makeText(getApplicationContext(),"Agregado",Toast.LENGTH_SHORT).show();
                    btnCheck();
                    progressDialog.dismiss();
                }

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
                                }
                            }, 2000);

                }
            });
        }
    };

    private Emitter.Listener onProcess = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Cursor res = myDb2.getAllData();
                    res.moveToNext();

                    //Toast.makeText(getBaseContext(), "onProcess" + args[0].toString(), Toast.LENGTH_SHORT).show();
                    if(args[0].toString().equals("authenticatedOK")) {
                        //EditText nombre = (EditText) findViewById(R.id.editTextNombreHome);

                        String json = "{nombre:\""+ _nom.getText() +"\",login:\"bewise\",login2:\""+ res.getString(2) +"\",pass:\"bewise\",pass2:\""+res.getString(3) +"\",rid:\""+ res.getString(5) +"\",name:\""+ res.getString(1) +"\"}";
                        JSONObject obj;
                        try {
                            obj = new JSONObject(json);
                            socket.emit("CAMBIARNOMBRE",obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(args[0].toString().equals("cambiarnombreOK")) {
                        onCamNomOK();
                    }
                }
            });
        }
    };

    private void onCamNomOK()
    {
        progressDialog.dismiss();
        progressDialog.setMessage(getResources().getString(R.string.ConfiguringHubDialog));
        progressDialog.show();
            //EditText nombre = (EditText) findViewById(R.id.editTextNombreHome);
            socket.disconnect();
            socket.close();
            socket =null;

            str="";

            //Toast.makeText(getBaseContext(),"http://"+_nom.getText()+".en.bewise.com.co",Toast.LENGTH_LONG).show();

            String url="http://" + _nom.getText() + ".en.bewise.com.co";
            URLDefHome=url;

            AsyncTask<String, Integer, String> asy = new AsyncURL3(this,2).execute(url);

        String status="";
            /*try {
                while(status.equals("") || status.contains("not found")) {
                    AsyncTask<String, Integer, String> asy = new AsyncURL().execute(url);
                    status = asy.get();
                    asy.cancel(true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }*/
            //Toast.makeText(getBaseContext(), status, Toast.LENGTH_LONG).show();



    }

    private Emitter.Listener onAuthenticated = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socket.emit("authenticated");
                    //Toast.makeText(getBaseContext(), "Authenticated", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    progressDialog.setMessage(getResources().getString(R.string.SendingSettingsDialog));
                    progressDialog.show();
                    socket.on("PROCESS",onProcess);

                }
            });
        }
    };

    public void btnCheck()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        finish();
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

    public boolean validate() {
        boolean valid = true;

        String name = _nom.getText().toString();


        String pattern= "^[a-zA-Z0-9 ]*$";

        if(!name.matches(pattern))
        {
            _nom.setError(getResources().getString(R.string.ErrorNameSpecialChars));
            valid = false;
        }
        else if (name.isEmpty() || name.length() < 5) {
            _nom.setError(getResources().getString(R.string.NameHubError));
            valid = false;
        } else {
            _nom.setError(null);
        }

        return valid;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //takePictureButton.setEnabled(true);
            }
        }
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BeWise");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ImageView im = (ImageView)findViewById(R.id.imageView7);
                im.setImageURI(file);

            }
        }
    }
}
