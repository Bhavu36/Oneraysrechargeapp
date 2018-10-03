package com.onerays.bhavna.oneraysrechargeapp;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Recharge extends AsyncTask <String, Void, String>{




        HttpURLConnection urlConnection;
        String urlString;


        @Override
        protected String doInBackground(String... strings) {
            urlString = "https://agonistical-threat.000webhostapp.com/onerays/recharge.php";
            try {
                URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");

                urlConnection.setDoOutput(true);
                Recharge recharge;
                AsyncTask<String, Void, String> asyncTask;


                String postingData = URLEncoder.encode("pin", "UTF-8") + "=" + URLEncoder.encode("123456789012345", "UTF-8") +
                        "&" + URLEncoder.encode("mobile-no", "UTF-8") + "=" + URLEncoder.encode("9845621456", "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
                bufferedWriter.write(postingData);
                bufferedWriter.flush();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                String result = null;
                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }


