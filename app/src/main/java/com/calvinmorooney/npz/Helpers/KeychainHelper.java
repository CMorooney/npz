package com.calvinmorooney.npz.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by calvinmorooney on 4/3/15.
 */
public class KeychainHelper {

    public static void storeCredentials (Context c, String username, String password){
        SharedPreferences keychain = c.getSharedPreferences("npz", c.MODE_PRIVATE);
        SharedPreferences.Editor editor = keychain.edit ();

        editor.putString("USER", username);
        editor.putString("PASS", password);
        editor.commit();
    }

    public static String[] getCredentials (Context c){
        SharedPreferences keychain = c.getSharedPreferences ("npz", c.MODE_PRIVATE);
        String u = keychain.getString("USER", "");
        String p = keychain.getString("PASS", "");

        return new String [] { u, p };
    }

    public static void clearCredentials (Context c){
        SharedPreferences keychain = c.getSharedPreferences ("npz", c.MODE_PRIVATE);
        SharedPreferences.Editor editor = keychain.edit();

        editor.clear();
        editor.commit();
    }
}
