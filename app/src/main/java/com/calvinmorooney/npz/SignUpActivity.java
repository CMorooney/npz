package com.calvinmorooney.npz;

import java.util.concurrent.ThreadPoolExecutor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.calvinmorooney.npz.Helpers.KeychainHelper;
import com.parse.*;


public class SignUpActivity extends ActionBarActivity {

    Button btnSignUp;
    EditText email, username, password;
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_sign_up);

        email = (EditText) findViewById(R.id.sign_up_email);
        username = (EditText) findViewById(R.id.sign_up_username);
        password = (EditText) findViewById(R.id.sign_up_password);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
    }

    public void signUp (View v)
    {
        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());

        TelephonyManager mTelephonyMgr;

        mTelephonyMgr = (TelephonyManager) getSystemService (TELEPHONY_SERVICE);

        String number = mTelephonyMgr.getLine1Number();

        number = number.replace (" ", "").replace ("(", "").replace (")", "").replace ("+", "").replace ("-", "");

        if(number.length () > 10)
        {
            number = number.substring (number.length () - 10);
        }


        user.put ("phone", number);


        loadingDialog = new ProgressDialog (this);
        loadingDialog.setTitle("one sec..");
        loadingDialog.show();

        user.signUpInBackground(new SignUpCallback()
        {
            public void done(ParseException e)
            {
                loadingDialog.dismiss();
                if (e == null) {
                    handleSignUpSuccess();
                } else {
                    handleSignUpError (e);
                }
            }
        });
    }

    void handleSignUpSuccess ()
    {
        ParseUser.logInInBackground(username.getText ().toString (), password.getText ().toString (), new LogInCallback()
        {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    goToMainActivity ();
                } else {
                    handleSignUpError (e);

                }
            }
        });
    }

    void handleSignUpError (ParseException e)
    {
        Toast.makeText (this, e.getMessage(), Toast.LENGTH_SHORT).show ();
    }

    void goToMainActivity ()
    {
        KeychainHelper.storeCredentials (this, username.getText().toString(), password.getText().toString());
        ((AppState) getApplication()).initialize();
        startActivity (new Intent(this, MainActivity.class));
    }
}
