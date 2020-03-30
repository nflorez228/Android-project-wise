package com.nicoft.bewise;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    DatabaseLoginHelper myDb;
    public static final String PREFS_NAME = "IDFirebase";



    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.input_passwordret) EditText _passwordRetText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    //@InjectView(R.id.link_login) TextView _loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        myDb = new DatabaseLoginHelper(this);


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        /*_loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.CreatingAccountProgressDialog));
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String token = settings.getString("token", "");

        boolean rta = myDb.insertData(name,email,password,token);
        if(!rta)
        {
            return;
        }

        // end

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                    }
                }, 3000);

    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String passwordret = _passwordRetText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError(getResources().getString(R.string.NameError));
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getResources().getString(R.string.EmailError));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            _passwordText.setError(getResources().getString(R.string.PasswordError));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (passwordret.isEmpty() || !passwordret.equals(password)) {
            _passwordRetText.setError(getResources().getString(R.string.RetypePassError));
            valid = false;
        } else {
            _passwordRetText.setError(null);
        }

        return valid;
    }
}