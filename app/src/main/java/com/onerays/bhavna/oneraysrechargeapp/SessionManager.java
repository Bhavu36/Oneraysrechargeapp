
package com.onerays.bhavna.oneraysrechargeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SessionManager {

    SharedPreferences pref;// Shared Preferences

    Editor editor;    // Editor for Shared preferences

    Context _context;

    int PRIVATE_MODE = 0;// Shared pref mode

    private static final String PREF_NAME = "LoginTest";// Sharedpref file name

    private static final String IS_LOGIN = "IsLoggedIn";    // All Shared Preferences Keys

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    // Create login session


    public void createLoginSession(long number) {
        // Storing login value as TRUE and storing the user Mobile number,
        editor.putBoolean(IS_LOGIN, true);
        editor.putLong("MOBILE",number);
        editor.commit();
    }

    // getMobile No
    public long getMobile(){
        long a = pref.getLong("MOBILE",1);
        return a;
    }


    //Adding card details to the shared preferences, only those added by user will be stored here,

    public void addCardDetails(String number, String expiry, String cvv){
        String a=pref.getString("number","null" );
        String b=pref.getString("expiry",null);
        String c=pref.getString("cvv",null);

        if(a.equals("null")){
            editor.putString("number",number);
            editor.putString("expiry",expiry);
            editor.putString("cvv",cvv);
        }else{
            a=a+","+number;
            b=b+","+expiry;
            c=c+","+cvv;

            editor.putString("number",a);
            editor.putString("expiry",b);
            editor.putString("cvv",c);
        }

        editor.commit();
   }



    public String[][] getCard(){
        String a = pref.getString("number","None");
       String b = pref.getString("expiry","None");
        String c = pref.getString("cvv","None");

        String[]array1=convertStringToArray(a);
       String[]array2=convertStringToArray(b);
       String[]array3=convertStringToArray(c);

       String[][]array={array1,array2,array3};

       return array;
   }

   private static String[] convertStringToArray(String str){
        String[]arr={};
        if(str.equals("None")){

        }else{
           arr = str.split(",");
       }
       return arr;
   }

    //code for checking the login status of the user,
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    //Clearing the details of the logged in user when he logs out,
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);
    }

    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}

