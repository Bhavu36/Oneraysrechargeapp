package com.onerays.bhavna.oneraysrechargeapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class singup extends AppCompatActivity {
    TextView wsi_registerasworker;
    EditText wsi_name, wsi_password, wsi_emailid, wsi_confirmpassword, address, contact;
    Button wsi_save;
    Spinner wsi_workfunction, work_type;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String record, sType;
    ArrayAdapter<String> adapter;
    String[] s_Type = {"Admin", "Client"};
    ArrayAdapter type_adapter;
    ImageView uplodImage;

    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_singup );
        //wsi_workfunction = ( Spinner ) findViewById( R.id.wsi_workfunction );
        wsi_registerasworker = ( TextView ) findViewById( R.id.wsi_registerasworker );
        wsi_name = ( EditText ) findViewById( R.id.wsi_name );
        wsi_password = ( EditText ) findViewById( R.id.wsi_password );
        wsi_emailid = ( EditText ) findViewById( R.id.wsi_emailid );
        wsi_confirmpassword = ( EditText ) findViewById( R.id.wsi_confirmpassword );
        wsi_save = ( Button ) findViewById( R.id.wsi_save );
        //adapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, names );
        //adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        uplodImage = ( ImageView ) findViewById( R.id.uplodeimage );
        address = ( EditText ) findViewById( R.id.wsi_address );
        contact = ( EditText ) findViewById( R.id.wsi_no );
        //wsi_workfunction.setAdapter( adapter );
       // requestStoragePermission();
//        wsi_workfunction.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

      uplodImage.setOnClickListener( new View.OnClickListener() {
          @Override
          public void onClick (View v) {
              showFileChooser();
          }
      } );
        work_type = (Spinner) findViewById(R.id.wsignup_type);
        type_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, s_Type);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        work_type.setAdapter(type_adapter);






        wsi_save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (wsi_name.getText().toString().equalsIgnoreCase( "" )) {
                    wsi_name.setError( "The Field Can't Be Empty" );
                } else if (contact.getText().toString().equalsIgnoreCase( "" )) {
                    contact.setError( "The Field Can't Be Empty" );
                } else if (contact.getText().toString().length() != 10) {
                    contact.setError( "Mobile number must be 10 digit" );
                } else if (address.getText().toString().equalsIgnoreCase( "" )) {
                    address.setError( "The Field Can't Be Empty" );
                } else if (wsi_password.getText().toString().equalsIgnoreCase( "" )) {
                    wsi_password.setError( "The Field Can't Be Empty" );
                } else if (wsi_password.getText().toString().length() != 8) {
                    wsi_password.setError( "Password must be 8 charector" );
                } else if (wsi_confirmpassword.getText().toString().equalsIgnoreCase( "" )) {
                    wsi_confirmpassword.setError( "The Field Can't Be Empty" );

                } else {
                    Intent intent = new Intent( singup.this, MainActivity.class );
                    startActivity( intent );
                    startActivity( new Intent( singup.this, LoginActivity.class ) );
                    if (new Connectioncall( singup.this ).isConnectingToInternet()) {
                        uploadMultipart();
                    } else {
                        new Connectioncall( singup.this ).connectiondetect();
                    }
                }
            }
        } );
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        startActivity( new Intent( singup.this, LoginActivity.class ) );
    }

    private class insData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute () {
            super.onPreExecute();
            pd = new ProgressDialog( singup.this );
            pd.setMessage( "Loading..." );
            pd.setCancelable( false );
            pd.show();
        }

        @Override
        protected String doInBackground (String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            if (sType.equalsIgnoreCase( "Worker" )) {
                hashMap.put( "wsi_name", wsi_name.getText().toString() );
                hashMap.put( "wsi_type", sType );
                //hashMap.put( "wsi_workfunction", record );
                hashMap.put( "wsi_emailid", wsi_emailid.getText().toString() );
                hashMap.put( "wsi_password", wsi_password.getText().toString() );
                return new MakeServiceCall().MakeServiceCall( Constant.URL + "w_signup.php", MakeServiceCall.POST, hashMap );
            } else {
                hashMap.put( "wsi_name", wsi_name.getText().toString() );
                hashMap.put( "wsi_type", sType );
                hashMap.put( "wsi_emailid", wsi_emailid.getText().toString() );
                hashMap.put( "wsi_password", wsi_password.getText().toString() );
                return new MakeServiceCall().MakeServiceCall( Constant.URL + "c_signup.php", MakeServiceCall.POST, hashMap );
            }

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
                    Toast.makeText( singup.this, object.getString( "Message" ), Toast.LENGTH_SHORT ).show();
                    startActivity( new Intent( singup.this, LoginActivity.class ) );
                } else {
                    Toast.makeText( singup.this, object.getString( "Message" ), Toast.LENGTH_SHORT ).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void uploadMultipart () {
        //getting name for the image
        //String name = editText.getText().toString().trim();


        //getting the actual path of the image
        if (filePath != null) {
            String path = getPath( filePath );

            //Uploading code
            try {
                String uploadid = UUID.randomUUID().toString();


                if (sType.equalsIgnoreCase( "Admin" )) {
                    //Creating a multi part request
                    Toast.makeText( this, "Admin Sign up Successfully", Toast.LENGTH_SHORT ).show();
                    new MultipartUploadRequest( singup.this, uploadid, Constant.URL + "w_signup.php" )
                            .addFileToUpload( path, "file" ) //Adding file
                            .addParameter( "action", "addImage" )
                            .addParameter( "wsi_name", wsi_name.getText().toString() )
                            .addParameter( "wsi_type", sType )
                            //.addParameter( "wsi_workfunction", record )
                            .addParameter( "workermyaccountstatus", "" )
                            .addParameter( "workermyaccountcontactnumber", contact.getText().toString() )
                            .addParameter( "workermyaccountaddress", address.getText().toString() )
                            .addParameter( "wsi_emailid", wsi_emailid.getText().toString() )
                            .addParameter( "wsi_password", wsi_password.getText().toString() )
                            .addParameter( "wsi_password", wsi_password.getText().toString() )
                            .setNotificationConfig( new UploadNotificationConfig() )
                            .setMaxRetries( 2 )
                            .startUpload(); //Starting the upload
                } else {
                    Toast.makeText( this, "Client Sign up Successfully", Toast.LENGTH_SHORT ).show();
                    new MultipartUploadRequest( singup.this, uploadid, Constant.URL + "w_signup.php" )
                            .addFileToUpload( path, "file" ) //Adding file
                            .addParameter( "action", "addImage" )
                            .addParameter( "wsi_name", wsi_name.getText().toString() )
                            .addParameter( "wsi_type", sType )
                            .addParameter( "workermyaccountstatus", "" )
                            .addParameter( "workermyaccountcontactnumber", contact.getText().toString() )
                            .addParameter( "workermyaccountaddress", address.getText().toString() )
                            .addParameter( "wsi_emailid", wsi_emailid.getText().toString() )
                            .addParameter( "wsi_password", wsi_password.getText().toString() )
                            .addParameter( "wsi_password", wsi_password.getText().toString() )
                            .setNotificationConfig( new UploadNotificationConfig() )
                            .setMaxRetries( 2 )
                            .startUpload(); //Starting the upload
                }

            } catch (Exception exc) {
                Toast.makeText( singup.this, exc.getMessage(), Toast.LENGTH_SHORT ).show();
                // startActivity(new Intent(w_signup.this, login.class));
            }
        } else {
            Toast.makeText( this, "Select Image", Toast.LENGTH_SHORT ).show();
        }
    }

    private void showFileChooser () {
        Intent intent = new Intent();
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( intent, "Select Picture" ), PICK_IMAGE_REQUEST );

    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap( singup.this.getContentResolver(), filePath );
                uplodImage.setImageBitmap( bitmap );

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath (Uri uri) {
        Cursor cursor = singup.this.getContentResolver().query( uri, null, null, null, null );
        cursor.moveToFirst();
        String document_id = cursor.getString( 0 );
        document_id = document_id.substring( document_id.lastIndexOf( ":" ) + 1 );
        cursor.close();

        cursor = singup.this.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null );
        cursor.moveToFirst();
        String path = cursor.getString( cursor.getColumnIndex( MediaStore.Images.Media.DATA ) );
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission () {
        if (ContextCompat.checkSelfPermission( singup.this, Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale( singup.this, Manifest.permission.READ_EXTERNAL_STORAGE )) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions( singup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE );
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText( singup.this, "Permission granted now you can read the storage", Toast.LENGTH_LONG ).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText( singup.this, "Oops you just denied the permission", Toast.LENGTH_LONG ).show();
            }
        }
    }
}




