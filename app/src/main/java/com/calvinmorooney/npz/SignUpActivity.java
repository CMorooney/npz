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
import com.calvinmorooney.npz.Helpers.ParseHelper;
import com.parse.*;


public class SignUpActivity extends ActionBarActivity {

    Button btnSignUp;
    EditText email, username, password;

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
        ParseHelper p = new ParseHelper(this);
        p.signUpAndLogIn(username.getText().toString(), password.getText().toString(), email.getText().toString());
    }
}
