package com.calvinmorooney.npz;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.calvinmorooney.npz.Adapters.ParseFriendsAdapter;
import com.calvinmorooney.npz.Helpers.ParseHelper;
import com.calvinmorooney.npz.Services.FloatingNippleService;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SendCallback;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    TextView mainText;
    AlertDialog friendsDialog;

    @Override
    protected void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);

        setContentView(R.layout.activity_main);

        mainText = (TextView) findViewById (R.id.main_text_view);
        mainText.setText("welcome, " + ParseUser.getCurrentUser().getUsername() + "!");

        ParseHelper p = new ParseHelper(this);
        p.queryAvailableFriends();
    }

    public void openContacts (View v)
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setTitle("Select a friend:-");
        final ParseFriendsAdapter friendsAdapter = new ParseFriendsAdapter(MainActivity.this);

        builderSingle.setAdapter(friendsAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recipientSelected (((AppState) getApplication()).friendsList.get(which));
                    }
                });

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        friendsDialog = builderSingle.create();
        friendsDialog.show();
    }

    void recipientSelected(ParseUser user)
    {
        friendsDialog.dismiss();
        mainText.setText(user.getUsername());

        ParseObject p = new ParseObject("Nip");
        p.put ("nipImageId", 1);
        p.put ("victim", user);
        p.put ("sender", ParseUser.getCurrentUser());
        p.saveInBackground();

        ParseQuery pQuery = ParseInstallation.getQuery();
        pQuery.whereEqualTo("username", user.getUsername());

        ParsePush parsePush = new ParsePush();
        parsePush.setQuery(pQuery);
        parsePush.setMessage("npz");
        parsePush.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    pushSuccess();
                }else{
                    pushError(e.getMessage());
                }
            }
        });
    }

    void pushSuccess()
    {
        Log.e("PUSH RESULT: ","success");
        Toast.makeText(this, "push sent", Toast.LENGTH_SHORT);
    }

    void pushError(String err)
    {
        Log.e("PUSH RESULT: ", err);
        Toast.makeText(this, "push fail: " + err, Toast.LENGTH_SHORT);
    }
}
