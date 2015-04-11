package com.calvinmorooney.npz;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.calvinmorooney.npz.Adapters.ParseFriendsAdapter;
import com.calvinmorooney.npz.Helpers.ParseHelper;
import com.calvinmorooney.npz.Services.FloatingNippleService;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

        // Find users near a given location
        ParseQuery userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("objectId", user.getObjectId());

        // Find devices associated with these users
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereMatchesQuery("user", userQuery);

        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setMessage("nipple from " + ParseUser.getCurrentUser().getUsername());
        push.sendInBackground();
    }
}
