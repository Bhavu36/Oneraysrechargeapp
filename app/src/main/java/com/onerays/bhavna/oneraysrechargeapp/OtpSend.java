package com.onerays.bhavna.oneraysrechargeapp;

import android.os.StrictMode;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;
import android.os.Build;


import cz.msebera.android.httpclient.HttpResponse;


public class OtpSend {

    int rno;

    public char[] RandomOtp() {
        int len = 4;
        String numbers = "0123456789";

        // Using random method
        Random rndm_method = new Random();

        char[] otp = new char[len];

        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return otp;
    }

    public boolean sendSMS(long mobileno) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        rno = Integer.parseInt(new String(RandomOtp()));

        System.out.println(rno);
        System.out.println(mobileno);

        //Your authentication key
        String authkey = "9d8c2380f8XX";
        //Multiple mobiles numbers separated by comma
        String mobiles = new Long(mobileno).toString();
        //Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "INFOSM";
        //Your message to send, Add URL encoding here.
        String message = "Verify Your Mobile No For RechargeApp By this OTP " + rno;
        //define route
        String route = "4";

        URLConnection myURLConnection = null;
        URL myURL = null;
        BufferedReader reader = null;

        //encoding message
        String encoded_message = URLEncoder.encode(message);

        //Send SMS API
        //String mainUrl = "https://agonistical-threat.000webhostapp.com/onerays/otp.php";

        String mainUrl = "http://sms.hemaom.com/submitsms.jsp?user=OMVINAYA&key=9d8c2380f8XX&senderid=INFOSM&accusage=1";

        //Prepare parameter string
        StringBuilder sbPostData = new StringBuilder(mainUrl);
        sbPostData.append("&mobiles=" + mobiles);
        sbPostData.append("&message=" + encoded_message);

        mainUrl = sbPostData.toString();

        try {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //reading response
            String response;
            while ((response = reader.readLine()) != null)
                //print response
                Log.d("RESPONSE", " " + response);

            //finally close connection
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;

    }
}

