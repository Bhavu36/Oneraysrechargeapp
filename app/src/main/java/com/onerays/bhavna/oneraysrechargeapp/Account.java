package com.onerays.bhavna.oneraysrechargeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Account extends AppCompatActivity {

    TextView username,email,mobile,city,dor;
    ImageButton img;

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Account";
    CoordinatorLayout coordinatorLayout;
    long mobileSession;
    SessionManager sm;
    DbHandlersUsers db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_profile);

       coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        username = (TextView) findViewById(R.id.userNameProfile);
        mobile = (TextView) findViewById(R.id.mobileProfile);
        email = (TextView) findViewById(R.id.emailProfile);
        city = (TextView) findViewById(R.id.cityProfile);
        dor = (TextView) findViewById(R.id.dateProfile);
        img = (ImageButton) findViewById(R.id.imgProfile);

        db = new DbHandlersUsers(this);
        sm = new SessionManager(this);
        mobileSession = sm.getMobile();
        String[] values = db.getData(mobileSession);

        //Code for getting Users Data from db and setting in the required field

        if (values.equals("")) {
            Toast.makeText(this, "Doesn't Return Anything", Toast.LENGTH_SHORT).show();
        } else {
            username.setText(values[1]);        //Username
            mobile.setText("Mobile No :     " + mobileSession);
            email.setText("Email ID :     " + values[0]);
            city.setText("City :    " + values[2]);
            dor.setText("Date Of Registration:  " + values[4]);
           // username.setAnimation( AnimationUtils.loadAnimation(this, R.anim.text_animation));
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction( Intent.ACTION_GET_CONTENT);
                startActivityForResult( Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();

                if (null != selectedImageUri) {

                    // Saving to Database...
                    if (saveImageInDB(selectedImageUri)) {
                        showMessage("Image Saved in Database ! Cheers");
                        img.setImageURI(selectedImageUri);
                    }

                    // Reading from Database after 3 seconds just to show the message
                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            if (loadImageFromDB()) {
                                showMessage("Image Loaded from Database ! Cheers");
                            }
                        }
                    }, 3000);
                }

            }
        }
    }

    // Save the
    Boolean saveImageInDB(Uri selectedImageUri) {

        try {
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            byte[] inputData = getBytes(iStream);
            db.insertImage(mobileSession,inputData);
            return true;
        } catch (IOException ioe) {
            Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            return false;
        }

    }

    Boolean loadImageFromDB() {
        try {
            byte[] bytes = db.retreiveImageFromDB(mobileSession);
            // Show Image from DB in ImageView
            img.setImageBitmap(getImage(bytes));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "<loadImageFromDB> Error : " + e.getLocalizedMessage());
            return false;
        }
    }


    // Image Utility methods

    public static byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}

