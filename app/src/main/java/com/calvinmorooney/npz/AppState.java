package com.calvinmorooney.npz;

import java.util.List;
import java.util.ArrayList;
import android.app.*;
import android.provider.ContactsContract;
import android.net.Uri;
import android.database.Cursor;
import android.util.Log;

import com.calvinmorooney.npz.Helpers.ParseHelper;
import com.calvinmorooney.npz.Receivers.PushReceiver;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class AppState extends Application {

    final String CLIENT_ID = "LyfrTXo1XlbfMhgbxbCSsDrRavB4QxjIJlDCjerH";
    final String APP_ID = "vaAZ53fv0UAg48EPvPHvSoBBiBQFjV9y6HqQvsqF";
    final int VERSION = 1;

    public ArrayList<String> deviceDirectory;
    public List<ParseUser> friendsList;

    @Override
    public void onCreate ()
    {
        super.onCreate();

        loadPhoneList ();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APP_ID, CLIENT_ID);
    }

    public void initialize ()
    {
        subscribeToPushes();
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
        installation.put("appVersion", VERSION);
        installation.saveInBackground();
    }

    private void loadPhoneList ()
    {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor cursor = getContentResolver ().query (uri, projection, null, null, null);

        deviceDirectory = new ArrayList<String> ();

        if (cursor.moveToFirst()) {
            do {
                String number = cursor.getString (cursor.getColumnIndex (projection [0]));

                number = ParseHelper.parsePhoneNumer(number);

                Log.e ("number: ", number);
                deviceDirectory.add(number);

            }  while (cursor.moveToNext());
        }
    }
}
