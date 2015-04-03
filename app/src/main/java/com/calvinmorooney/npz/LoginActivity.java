package com.calvinmorooney.npz;

import android.app.Application;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.calvinmorooney.npz.Helpers.KeychainHelper;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends ActionBarActivity {

    Button btnLogIn, btnSignUp;
    EditText username, password;

    final String APP_ID = "vaAZ53fv0UAg48EPvPHvSoBBiBQFjV9y6HqQvsqF";
    final String CLIENT_ID = "LyfrTXo1XlbfMhgbxbCSsDrRavB4QxjIJlDCjerH";

    @Override
    protected void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);

        setContentView(R.layout.activity_login);

        username = (EditText) findViewById (R.id.login_username);
        password = (EditText) findViewById (R.id.login_password);
        btnLogIn = (Button) findViewById (R.id.login_button);
        btnSignUp = (Button) findViewById (R.id.login_sign_up_button);
    }

    public void signUp (View v)
    {
        Intent i = new Intent (v.getContext(), SignUpActivity.class);
        startActivity (i);
    }

    public void validateForm (View v)
    {
        //TODO: validate form
        logIn ();
    }

    private void logIn ()
    {
        ParseUser user = new ParseUser ();

        ParseUser.logInInBackground(username.getText ().toString (), password.getText ().toString (), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    ((AppState) getApplication()).user = user;
                    goToMainActivity ();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });
    }

    void goToMainActivity ()
    {
        KeychainHelper.storeCredentials (this, username.getText().toString(), password.getText().toString());
        startActivity (new Intent (this, MainActivity.class));
    }
}
