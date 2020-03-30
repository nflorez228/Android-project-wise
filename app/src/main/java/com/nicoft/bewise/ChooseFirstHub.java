package com.nicoft.bewise;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blunderer.materialdesignlibrary.views.CardView;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import butterknife.InjectView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChooseFirstHub extends Activity {

    DatabaseHelper myDb;
    DatabaseLoginHelper myDb2;
    ProgressDialog progressDialog;
    private Socket socket;
    private String nombreHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_first_hub);


        progressDialog= new ProgressDialog(ChooseFirstHub.this,
                R.style.AppTheme_Dark_Dialog);


        myDb = new DatabaseHelper(this);
        myDb2 = new DatabaseLoginHelper(this);


        CardView nuevo= (CardView) findViewById(R.id.NewCardView);
        CardView existente= (CardView) findViewById(R.id.ExistingCardView);

        nuevo.setOnNormalButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConectToHubActivity.class);
                startActivity(intent);

            }
        });
        existente.setOnNormalButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), ConectToHubActivity.class);
                //startActivity(intent);
                btn2();
            }
        });

    }

    public void btn()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ChooseFirstHub.this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.laayout_dialog_custom, null))
                // Add action buttons
                .setPositiveButton(getResources().getString(R.string.SearchButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        EditText nom = (EditText) findViewById(R.id.input_nameofexisthub);
                        //Toast.makeText(ChooseFirstHub.this, nom.getText(), Toast.LENGTH_SHORT).show();
                        searchHub(nom.getText().toString());
                    }
                })
                .setNegativeButton(getResources().getString(R.string.CancelButton), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    public void btn2()
    {
        final Dialog dialog = new Dialog(ChooseFirstHub.this);
        dialog.setContentView(R.layout.laayout_dialog_custom);
        dialog.setTitle(getResources().getString(R.string.SearchDialogTitle));
        dialog.backgroundColor(Color.parseColor("#3FA9F5"));
        dialog.titleColor(Color.WHITE);

        Button button = (Button) dialog.findViewById(R.id.dialog_ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit=(EditText)dialog.findViewById(R.id.input_nameofexisthub);
                String text=edit.getText().toString();
                //Toast.makeText(ChooseFirstHub.this, text, Toast.LENGTH_SHORT).show();
                if (text.isEmpty() || text.length() < 5) {
                    edit.setError(getResources().getString(R.string.NameHubError));
                } else {
                    edit.setError(null);

                    searchHub(text);
                    dialog.dismiss();
                }

            }
        });

        Button buttonCancel = (Button) dialog.findViewById(R.id.dialog_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void dialogFound()
    {
        final Dialog dialog = new Dialog(ChooseFirstHub.this);
        dialog.setContentView(R.layout.laayout_dialog_custom_existente);
        dialog.setTitle(getResources().getString(R.string.LoginDialogTitle));

        dialog.backgroundColor(Color.parseColor("#3FA9F5"));
        dialog.titleColor(Color.WHITE);

        Button button = (Button) dialog.findViewById(R.id.dialog_ok2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit=(EditText)dialog.findViewById(R.id.input_emailofhub);
                String text=edit.getText().toString();
                EditText editpass=(EditText)dialog.findViewById(R.id.input_passwordofhub);
                String textpass=editpass.getText().toString();


                boolean valid = true;
                if (text.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    edit.setError(getResources().getString(R.string.EmailError));
                    valid = false;
                } else {
                    edit.setError(null);
                }

                if (textpass.isEmpty() || textpass.length() < 4 || textpass.length() > 15) {
                    editpass.setError(getResources().getString(R.string.PasswordError));
                    valid = false;
                } else {
                    editpass.setError(null);
                }

                if(valid)
                {
                    loginHub(text,textpass);
                    dialog.dismiss();
                }

                //Toast.makeText(ChooseFirstHub.this, text + textpass, Toast.LENGTH_SHORT).show();


            }
        });

        Button buttonCancel = (Button) dialog.findViewById(R.id.dialog_cancel2);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void searchHub(String nom)
    {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.SearchingHubDialog)+"\""+ nom +"\"");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        nombreHub = nom;

        AsyncTask<String, Integer, String> asy = new AsyncURL4(this,1).execute("http://" + nom + ".en.bewise.com.co");
    }

    public void onAsyncResultAquired(String result, int nTaskNum)
    {
        if(nTaskNum==1) {
            if (!result.contains("unavailable at the moment")) {
                //Toast.makeText(getBaseContext(), "found!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                dialogFound();

            } else {
                nombreHub="";
                //Toast.makeText(getBaseContext(), "not found :(", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                progressDialog.setMessage(getResources().getString(R.string.HubNotFoundDialog));
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
        }

    }

    public void loginHub(String email, String pass)
    {
            //Toast.makeText(getBaseContext(), "http://" + nombreHub + ".en.bewise.com.co", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            progressDialog.setMessage(getResources().getString(R.string.AuthenticatingOnHubDialog));
            progressDialog.show();
            try {
                socket = IO.socket("http://" + nombreHub + ".en.bewise.com.co");
                socket.connect();
                socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
                JSONObject obj = new JSONObject("{username:\""+email+"\",password:\""+pass+"\"}");
                socket.emit("authentication", obj);
                socket.on("authenticated", onAuthenticated);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Error conectando a socket.io", Toast.LENGTH_SHORT).show();
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
                        //progressDialog.dismiss();
                        String json = "{login:\""+ res.getString(2) +"\",pass:\""+res.getString(3) +"\",rid:\""+ res.getString(5) +"\"}";
                        JSONObject obj;
                        try {
                            obj = new JSONObject(json);
                            socket.emit("AGREGARLOGIN",obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(args[0].toString().equals("agregarLoginOK")) {
                        onAddLogOK();
                    }
                }
            });
        }
    };

    private void onAddLogOK()
    {
        socket.disconnect();
        socket.close();
        socket =null;
        boolean rta=myDb.insertData(DatabaseHelper.TABLE_NAME_1, nombreHub, "House", "http://" + nombreHub + ".en.bewise.com.co");
        if(rta){
            //Toast.makeText(getApplicationContext(),"Agregado",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            finish();
        }

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
                    progressDialog.setMessage(getResources().getString(R.string.AddingCredentialsDialog));
                    progressDialog.show();
                    socket.on("PROCESS",onProcess);
                }
            });
        }
    };


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

}
