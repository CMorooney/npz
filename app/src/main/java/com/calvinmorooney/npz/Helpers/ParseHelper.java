package com.calvinmorooney.npz.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.calvinmorooney.npz.AppState;
import com.calvinmorooney.npz.MainActivity;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

/**
 * Created by calvinmorooney on 4/4/15.
 */
public class ParseHelper {

    Context context;

    public ParseHelper (Context c)
    {
        context = c;
    }

    public void queryAvailableFriends ()
    {
        ParseQuery<ParseUser> p = ParseUser.getQuery().whereContainedIn ("phone", ((AppState) context.getApplicationContext ()).deviceDirectory);

        p.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    ((AppState) context.getApplicationContext()).friendsList = objects;
                } else {

                }
            }
        });
    }

    public void logIn(final String username, final String password)
    {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                goToMainActivity(username, password);
            }
        });
    }

    public void signUpAndLogIn (final String username, final String password, String email)
    {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        TelephonyManager mTelephonyMgr;

        mTelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);

        String number = mTelephonyMgr.getLine1Number();

        number = ParseHelper.parsePhoneNumber(number);

        user.put ("phone", number);

        user.signUpInBackground(new SignUpCallback()
        {
            public void done(ParseException e)
            {
                if (e == null) {
                    logIn(username, password);
                } else {

            }
            }
        });
    }

    private void subscribeToPushes ()
    {
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }

    private void updateInstallation()
    {
        // Associate the device with a user
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("username", ParseUser.getCurrentUser().getUsername());
        installation.put("user", ParseUser.getCurrentUser());
        installation.put("appVersion", AppState.VERSION);
        installation.saveInBackground();
    }

    public void goToMainActivity(String username, String password)
    {
        KeychainHelper.storeCredentials (context, username, password);

        subscribeToPushes();

        updateInstallation();

        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static String parsePhoneNumber (String number)
    {
        number = number.replace (" ", "").replace ("(", "").replace (")", "").replace ("+", "").replace ("-", "");

        if(number.length () > 10)
        {
            number = number.substring (number.length () - 10);
        }

        return number;
    }
}
