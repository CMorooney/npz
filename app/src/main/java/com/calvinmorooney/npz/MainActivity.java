package com.calvinmorooney.npz;

import android.app.Application;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate (Bundle bundle)
    {
        super.onCreate(bundle);

        setContentView(R.layout.activity_main);

        TextView name = (TextView) findViewById (R.id.main_text_view);
        name.setText ("welcome, " + ((AppState) getApplication ()).user.getUsername () + "!");

//        new Thread(new Runnable() {
//            public void run() {
//
//            }
//        }).start();

//        var p = new ParseService (this);
//
//        var x = new List<ParseUser>();
//
//        ThreadPool.QueueUserWorkItem (async o => x = await p.GetFriendsByPhone ());
    }
}
