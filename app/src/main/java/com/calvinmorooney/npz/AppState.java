package com.calvinmorooney.npz;

import java.util.List;
import java.util.ArrayList;
import android.app.*;
import android.provider.ContactsContract;
import android.net.Uri;
import android.database.Cursor;
import com.parse.Parse;
import com.parse.ParseUser;

public class AppState extends Application {

    final String CLIENT_ID = "LyfrTXo1XlbfMhgbxbCSsDrRavB4QxjIJlDCjerH";
    final String APP_ID = "vaAZ53fv0UAg48EPvPHvSoBBiBQFjV9y6HqQvsqF";

    public ParseUser user;
    public ArrayList<String> deviceDirectory;

    @Override
    public void onCreate ()
    {
        super.onCreate();

        loadPhoneList ();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APP_ID, CLIENT_ID);
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

                number = number.replace (" ", "").replace ("(", "").replace (")", "").replace ("+", "").replace ("-", "");

                if(number.length () > 10)
                {
                    number = number.substring (number.length () - 10);
                }

                deviceDirectory.add (number);

            }  while (cursor.moveToNext());
        }
    }
}
