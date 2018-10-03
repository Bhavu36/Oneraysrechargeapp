package com.onerays.bhavna.oneraysrechargeapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class c1_myaccount extends AppCompatActivity {
    EditText csi_name, csi_address, csi_contactnumber, csi_emailid, csi_pass;
    Button clientmyaccountupdatebutton;
    ImageView clientprofilephoto_id;
    Spinner wsi_status;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Uri filePath;
    ArrayAdapter status_adapter;
    ArrayAdapter<String> adapter;
    String[] worker_status = {"Engage", "Disengage"};

    String sId, sType, sCSI_Name, sCSI_Address, sCSI_Contact, record;
    SharedPreferences sp;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_c1_myaccount );
        requestStoragePermission();

        sp = getSharedPreferences( Constant.PREF, MODE_PRIVATE );
        sId = sp.getString( Constant.ID, "" );
        sType = sp.getString( Constant.TYPE, "" );
        Toast.makeText( c1_myaccount.this, sType, Toast.LENGTH_SHORT ).show();
        csi_name = ( EditText ) findViewById( R.id.csi_name );
        csi_address = ( EditText ) findViewById( R.id.csi_address );
        csi_contactnumber = ( EditText ) findViewById( R.id.csi_contactnumber );
        clientprofilephoto_id = ( ImageView ) findViewById( R.id.clientprofilephoto_id );
        clientmyaccountupdatebutton = ( Button ) findViewById( R.id.clientmyaccountupdatebutton );
        csi_pass = ( EditText ) findViewById( R.id.csi_password );
//        wsi_status = (Spinner) findViewById(R.id.wsi_status);
        csi_emailid = ( EditText ) findViewById( R.id.csi_emailid );
//        relativeLayout = (RelativeLayout) findViewById(R.id.c1_myacc_relative);

        adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, worker_status );

//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        wsi_status.setAdapter(adapter);

        if (new Connectioncall( c1_myaccount.this ).isConnectingToInternet()) {
            new getcAcccountData().execute();
        } else {
            new Connectioncall( c1_myaccount.this ).connectiondetect();
        }
       /* wsi_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                record = (String) parent.getItemAtPosition(position);
            }*/

     /*       @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        clientprofilephoto_id.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent pickPhotoIntent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                startActivityForResult( pickPhotoIntent, PICK_IMAGE_REQUEST );
            }
        } );

       /* if (sType.equalsIgnoreCase("Worker")) {
          //  relativeLayout.setVisibility(View.VISIBLE);
        } else {
//            relativeLayout.setVisibility(View.GONE);
        }*/

        clientmyaccountupdatebutton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View view) {

                uploadMultipartWorker();

                /*Intent intent = new Intent(c1_myaccount.this, client_homepage.class);
                startActivity(intent);*/
                /*if (new ConnectionCall(c1_myaccount.this).isConnectingToInternet()) {
                    new insData().execute();
                } else {
                    new ConnectionCall(c1_myaccount.this).connectiondetect();
                }*/

            }
        } );
    }


    private class getcAcccountData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute () {
            super.onPreExecute();
            pd = new ProgressDialog( c1_myaccount.this );
            pd.setMessage( "Loading..." );
            pd.show();
        }

        @Override
        protected String doInBackground (String... params) {
            return new MakeServiceCall().MakeServiceCall( Constant.URL + "fetchdata.php?wsi_id=" + sId, MakeServiceCall.POST, new HashMap<String, String>() );
        }

        @Override
        protected void onPostExecute (String s) {
            super.onPostExecute( s );
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                JSONObject object = new JSONObject( s );
                if (object.getString( "Status" ).equalsIgnoreCase( "True" )) {
                    JSONArray array = object.getJSONArray( "response" );
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject( i );
                        csi_name.setText( jsonObject.getString( "wsi_name" ) );
                        csi_address.setText( jsonObject.getString( "workermyaccountaddress" ) );
                        csi_contactnumber.setText( jsonObject.getString( "workermyaccountcontactnumber" ) );
                        csi_emailid.setText( jsonObject.getString( "wsi_emailid" ) );
                        Picasso.with( c1_myaccount.this ).load( (jsonObject.getString( "workerprofilephoto" )) ).into( clientprofilephoto_id );
                    }
                } else {
                    Toast.makeText( c1_myaccount.this, object.getString( "Message" ), Toast.LENGTH_SHORT ).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadMultipartWorker () {
        String path = getImage( filePath );
        sCSI_Address = csi_address.getText().toString();
        sCSI_Contact = csi_contactnumber.getText().toString();
        if (!path.equals( "" )) {
            try {
                Toast.makeText( this, "Update Successfully...", Toast.LENGTH_SHORT ).show();
                new MultipartUploadRequest( this, Constant.URL + "updateProfile.php" )
                        .addParameter( "action", "updateProfileWorker" )
                        .addParameter( "wsi_id", sId )
                        .addFileToUpload( path, "file" ) //Adding file
                        .addParameter( "wsi_name", csi_name.getText().toString() )
                        .addParameter( "wsi_type", sType )
                        .addParameter( "workermyaccountstatus", "" )
                        .addParameter( "workermyaccountcontactnumber", csi_contactnumber.getText().toString() )
                        .addParameter( "workermyaccountaddress", csi_address.getText().toString() )
                        .addParameter( "wsi_emailid", csi_emailid.getText().toString() )
                        .addParameter( "wsi_password", csi_pass.getText().toString() )
                        .setNotificationConfig( new UploadNotificationConfig() )
                        .setMaxRetries( 2 )
                        .startUpload();
                new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run () {
                        startActivity( new Intent( c1_myaccount.this, MainActivity.class ).setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                        finish();
                    }
                }, 3000 );
            } catch (Exception exc) {
                if (csi_address.getText().toString().equalsIgnoreCase( "" )) {
                    csi_address.setError( "Address Required" );
                    return;
                } else if (csi_contactnumber.getText().toString().equalsIgnoreCase( "" )) {
                    csi_contactnumber.setError( "Contact No Required" );
                    return;
                } else {
                    if (new Connectioncall( c1_myaccount.this ).isConnectingToInternet()) {
                        new insData().execute( sCSI_Address, sCSI_Contact );
                    } else {
                        new Connectioncall( c1_myaccount.this ).connectiondetect();
                    }
                }
            }
        }
    }

    private void uploadMultipart () {
        String path = getImage( filePath );
        sCSI_Address = csi_address.getText().toString();
        sCSI_Contact = csi_contactnumber.getText().toString();
        if (!path.equals( "" )) {
            try {
                new MultipartUploadRequest( this, Constant.URL + "updateProfile.php" )
                        .addParameter( "action", "updateProfile" )
                        .addFileToUpload( path, "file" ) //Adding file
                        .addParameter( "workermyaccountaddress", sCSI_Address )
                        .addParameter( "workermyaccountcontactnumber", sCSI_Contact )
                        .addParameter( "wsi_id", sId )//Adding text parameter to the request
                        .setNotificationConfig( new UploadNotificationConfig() )
                        .setMaxRetries( 2 )
                        .startUpload();
                new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run () {
                        startActivity( new Intent( c1_myaccount.this, MainActivity.class ).setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                        finish();
                    }
                }, 3000 );
            } catch (Exception exc) {
                if (csi_address.getText().toString().equalsIgnoreCase( "" )) {
                    csi_address.setError( "Address Required" );
                    return;
                } else if (csi_contactnumber.getText().toString().equalsIgnoreCase( "" )) {
                    csi_contactnumber.setError( "Contact No Required" );
                    return;
                } else {
                    if (new Connectioncall( c1_myaccount.this ).isConnectingToInternet()) {
                        new insData().execute( sCSI_Address, sCSI_Contact );
                    } else {
                        new Connectioncall( c1_myaccount.this ).connectiondetect();
                    }
                }
            }
        }
    }

    private String getImage (Uri uri) {
        if (uri != null) {
            String path = null;
            String[] s_array = {MediaStore.Images.Media.DATA};
            Cursor c = managedQuery( uri, s_array, null, null, null );
            int id = c.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
            if (c.moveToFirst()) {
                do {
                    path = c.getString( id );
                }
                while (c.moveToNext());
                c.close();
                if (path != null) {
                    return path;
                }
            }
        }
        return "";
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.getData() != null) {
                        filePath = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap( getContentResolver(), filePath );
                            clientprofilephoto_id.setImageBitmap( bitmap );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText( this, "Permission granted now you can read the storage", Toast.LENGTH_LONG ).show();
            } else {
                Toast.makeText( this, "Oops you just denied the permission", Toast.LENGTH_LONG ).show();
            }
        }
    }

    private void requestStoragePermission () {
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.READ_EXTERNAL_STORAGE )) {

        }
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE );
    }

    private class insData extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute () {
            super.onPreExecute();
            pd = new ProgressDialog( c1_myaccount.this );
            pd.setMessage( "Loading..." );
            pd.setCancelable( false );
            pd.show();
        }

        @Override
        protected String doInBackground (String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put( "action", "updateProfile_i" );
            hashMap.put( "csi_id", sId );
            hashMap.put( "csi_address", params[0] );
            hashMap.put( "csi_contactnumber", params[1] );
            return new MakeServiceCall().MakeServiceCall( Constant.URL + "updateProfile.php", MakeServiceCall.POST, hashMap );
        }

        @Override
        protected void onPostExecute (String s) {
            super.onPostExecute( s );
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                JSONObject object = new JSONObject( s );
                if (object.getString( "Status" ).equalsIgnoreCase( "True" )) {
                    Toast.makeText( c1_myaccount.this, object.getString( "Message" ), Toast.LENGTH_SHORT ).show();
                    Intent i = new Intent( c1_myaccount.this, MainActivity.class );
                    startActivity( i );
                } else {
                    Toast.makeText( c1_myaccount.this, object.getString( "Message" ), Toast.LENGTH_SHORT ).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

       private class insDataImpl extends AsyncTask<String, String, String> {
            ProgressDialog pd;

            @Override
            protected void onPreExecute () {
                super.onPreExecute();
                pd = new ProgressDialog( c1_myaccount.this );
                pd.setMessage( "Loading..." );
                pd.setCancelable( false );
                pd.show();
            }

            @Override
            protected String doInBackground (String... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                // hashMap.put("clientprofilephoto_id", clientprofilephoto_id.getText().toString());
                hashMap.put( "csi_name", csi_name.getText().toString() );
                hashMap.put( "csi_address", csi_address.getText().toString() );
                hashMap.put( "csi_contactnumber", csi_contactnumber.getText().toString() );
                return new MakeServiceCall().MakeServiceCall( "https://cleaning-faries.000webhostapp.com/c_signup.php", MakeServiceCall.POST, hashMap );
            }

            @Override
            protected void onPostExecute (String s) {
                super.onPostExecute( s );
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                try {
                    JSONObject object = new JSONObject( s );
                    if (object.getString( "Status" ).equalsIgnoreCase( "True" )) {
                        Toast.makeText( c1_myaccount.this, object.getString( "Message" ), Toast.LENGTH_SHORT ).show();
                        Intent i = new Intent( c1_myaccount.this, MainActivity.class );
                        startActivity( i );
                    } else {
                        Toast.makeText( c1_myaccount.this, object.getString( "Message" ), Toast.LENGTH_SHORT ).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }


    }
}



