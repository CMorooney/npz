package com.calvinmorooney.npz;

import com.calvinmorooney.npz.Helpers.KeychainHelper;
import com.calvinmorooney.npz.util.SystemUiHider;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;

public class SplashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        String [] storedCreds = KeychainHelper.getCredentials (this);
        if (storedCreds == null)
        {
            goToLoginActivity();
            finish();
        }
        else
        {
            ParseUser.logInInBackground(storedCreds [0], storedCreds [1], new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        goToMainActivity ();
                    } else {
                        goToLoginActivity();
                    }
                    finish();
                }
            });
        }
    }

    void goToLoginActivity ()
    {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity (i);
    }

    void goToMainActivity ()
    {
        Intent i = new Intent (this, MainActivity.class);
        ((AppState) getApplication()).initialize();
        startActivity (i);
    }
}
