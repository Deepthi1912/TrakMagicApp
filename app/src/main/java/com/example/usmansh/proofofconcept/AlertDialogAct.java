package com.example.usmansh.proofofconcept;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlertDialogAct extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
        ,LocationListener {


    EditText notesTextEd;
    Button   submitNotesBt,skipNotesbt,crossAlert,tickAlert;
    LocationLoc Track;
    Button openCambt;
    Uri photoURI;
    Uri img;
    Intent intent;

    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private static final int REQUEST_CAPTURE_IMAGE2 = 101;
    private StorageReference mStorageRef;
    DatabaseReference submitImgREF,responseCount,activeUser,myProjectList,submitGpsREF,submitNotesREF;
    private android.app.ProgressDialog ProgressDialog;
    String ProName = "",imageUrl;
    double latitude,longitude;

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RES_REQUEST = 7172;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private ImageView mPhotoCapturedImageView;
    private String mImageFileLocation = "";
    FirebaseAuth mAuth;

    String imageFilePath;
    String user_id,user_idd;
    LocationManager locationManager;
    String proName2="";
    String comingFrom="";
    String checkUser =" ";
    Date currentTime;
    TextView dateTimetv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);

                checkInternet();

        mPhotoCapturedImageView = (ImageView) findViewById(R.id.displayImg);
        currentTime = Calendar.getInstance().getTime();
        mPhotoCapturedImageView.setVisibility(View.INVISIBLE);


        comingFrom = getIntent().getStringExtra("comingAddRes");
        int remCode = getIntent().getIntExtra("remCode",0);
        ProName     = getIntent().getStringExtra("ProName");
        user_idd     = getIntent().getStringExtra("userId");
        activeUser = FirebaseDatabase.getInstance().getReference("ActiveUsers");
        myProjectList = FirebaseDatabase.getInstance().getReference("MyProjects");

        mAuth = FirebaseAuth.getInstance();

        if( comingFrom!=null && comingFrom.equalsIgnoreCase("AddResponse")){


            String email = mAuth.getCurrentUser().getEmail();
            String[] parts = email.split("@");
            String my_id = parts[0];

            if(!my_id.equalsIgnoreCase(user_idd)){

                checkUser = "F";

            }

        }



        Toast.makeText(this, "Pro Name: "+ProName, Toast.LENGTH_SHORT).show();


        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone ring = RingtoneManager.getRingtone(getApplicationContext(), notification);
        ring.play();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder

                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {

                        //sending Location Response
                        //locationMange();
                        ring.stop();
                        dialog.cancel();


                    }
                })
                ;


            if(remCode == 0){
                builder.setTitle("Your Project is Starting Now..!");
                proName2 = getIntent().getStringExtra("ProName");

                //cancelJobScheduler(remCode);

            }else if(remCode == 5){
                builder.setTitle("Your Project is Ending Now..!");

                proName2 = getIntent().getStringExtra("ProName");
                user_id = getIntent().getStringExtra("userId");
                 activeUser.child(user_id).child("isActive").setValue("no");
                myProjectList.child(user_id).child(ProName).child("isactive").setValue("OFF");
                closeAllAlarms();


            }else if(remCode == 6) {
                proName2 = getIntent().getStringExtra("ProName");
                builder.setTitle("Extra Alert");

            }
            else
             {
                if(remCode == 4){
                    proName2 = getIntent().getStringExtra("ProName");
                    builder.setTitle("Alert..! Last Reminder "+remCode);
                }else {
                    proName2 = getIntent().getStringExtra("ProName");
                    builder.setTitle("Alert Reminder " + remCode);
                }
            }

        AlertDialog alert = builder.create();
        alert.show();



        notesTextEd = (EditText)findViewById(R.id.notesTextEd);
        submitNotesBt = (Button)findViewById(R.id.submitNotesbt);
        skipNotesbt   = (Button)findViewById(R.id.skiptNotesbt);
        openCambt     = (Button)findViewById(R.id.openCambt);
        tickAlert     = (Button)findViewById(R.id.tickAlert);
        crossAlert    = (Button)findViewById(R.id.crossAlert);
        dateTimetv    = (TextView)findViewById(R.id.dateTimetv);
        dateTimetv.setText(currentTime.toString());

        //Open Camera
        openCambt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 24) {
                    Toast.makeText(AlertDialogAct.this, "Open Camera..!", Toast.LENGTH_SHORT).show();
                    openCameraIntent();
                }else {
                    Toast.makeText(AlertDialogAct.this, "Action Image..!", Toast.LENGTH_SHORT).show();
                    Intent cameraa = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraa,REQUEST_CAPTURE_IMAGE2);
                }
            }
        });



        tickAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog.setCanceledOnTouchOutside(false);

                uploadNotes();
                uploadImage(img);

            }
        });



        crossAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadNotesAsNull();
                uploadImageAsNull();


            }
        });

        submitNotesBt.setVisibility(View.INVISIBLE);
        skipNotesbt.setVisibility(View.INVISIBLE);

        ProgressDialog = new ProgressDialog(this);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        submitImgREF  = FirebaseDatabase.getInstance().getReference("ResponseData").child(user_idd).child(ProName).child("Image");
        responseCount = FirebaseDatabase.getInstance().getReference("ResponseCount").child(user_idd).child(ProName);




        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSION_REQUEST_CODE);

        }
        else{

            Toast.makeText(this, "Location uploading..", Toast.LENGTH_SHORT).show();
            locationMange();


            if(checkPlayServices()){

                //buildGoogleApiClient();
                //createLocationRequest();
                //displayLocation();
                //locationMange();

            }
        }




        submitNotesBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = mgr.getActiveNetworkInfo();
                if (netInfo != null) {
                    if (netInfo.isConnected()) {
                        // Internet Available
                        submitNotesREF = FirebaseDatabase.getInstance().getReference("ResponseData").child(user_idd).child(ProName).child("Text");
                        responseCount = FirebaseDatabase.getInstance().getReference("ResponseCount").child(user_idd).child(ProName);
                        submitNotesREF.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if(dataSnapshot.getValue() == null){

                                    submitNotesREF.child("1").setValue(checkUser+" 001: "+currentTime+"\n\nNotes:  "+notesTextEd.getText().toString());
                                    responseCount.child("textResValue").setValue("1");
                                    Toast.makeText(AlertDialogAct.this, "Notes Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                    alertBoxShow();
                                    //gotoMainAct();


                                }else{

                                    checkCounterFirstThenUploadDataTEXT();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }else {
                        noInternetAlert();
                    }
                } else {
                    //No internet
                    noInternetAlert();
                }

            }
        });




        skipNotesbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = mgr.getActiveNetworkInfo();
                if (netInfo != null) {
                    if (netInfo.isConnected()) {
                        // Internet Available
                        submitNotesREF = FirebaseDatabase.getInstance().getReference("ResponseData").child(user_idd).child(ProName).child("Text");
                        responseCount = FirebaseDatabase.getInstance().getReference("ResponseCount").child(user_idd).child(ProName);
                        submitNotesREF.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.getValue() == null){

                                    submitNotesREF.child("1").setValue(checkUser+" 001: // ");
                                    responseCount.child("textResValue").setValue("1");
                                    Toast.makeText(AlertDialogAct.this, "Notes Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                    alertBoxShow();
                                    //gotoMainAct();
                                }else{

                                    checkCounterFirstThenUploadDataTEXTSKIP();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }else {
                        noInternetAlert();
                    }
                } else {
                    //No internet
                    noInternetAlert();
                }

              alertBoxShow();

            }
        });

    }




    private void uploadNotesAsNull() {

        submitNotesREF = FirebaseDatabase.getInstance().getReference("ResponseData").child(user_idd).child(ProName).child("Text");
        responseCount = FirebaseDatabase.getInstance().getReference("ResponseCount").child(user_idd).child(ProName);
        submitNotesREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() == null){

                    submitNotesREF.child("1").setValue(checkUser+" 001: // ");
                    responseCount.child("textResValue").setValue("1");
                    Toast.makeText(AlertDialogAct.this, "Notes Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                   // alertBoxShow();
                    //gotoMainAct();
                }else{

                    checkCounterFirstThenUploadDataTEXTSKIP();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void uploadNotes() {

        submitNotesREF = FirebaseDatabase.getInstance().getReference("ResponseData").child(user_idd).child(ProName).child("Text");
        responseCount = FirebaseDatabase.getInstance().getReference("ResponseCount").child(user_idd).child(ProName);
        submitNotesREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue() == null){

                    submitNotesREF.child("1").setValue(checkUser+" 001: "+currentTime+"\n\nNotes:  "+notesTextEd.getText().toString());
                    responseCount.child("textResValue").setValue("1");
                    Toast.makeText(AlertDialogAct.this, "Notes Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                    //alertBoxShow();
                    //gotoMainAct();


                }else{

                    checkCounterFirstThenUploadDataTEXT();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void uploadImageAsNull() {


        submitImgREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue() == null){
                    submitImgREF.child("1").setValue(checkUser+" 001 : //");
                    responseCount.child("imageResValue").setValue("1");
                    Toast.makeText(AlertDialogAct.this, "Url Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                    gotoMainAct();


                }else{

                    checkCounterFirstThenUploadDataSKIP();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gotoMainAct();


    }
    private void uploadImage(Uri img) {


        final StorageReference filepath;
        if (img == null) {
            Toast.makeText(this, "Device Not Giving File Path..!", Toast.LENGTH_SHORT).show();
            //ProgressDialog.dismiss();
        } else {
            filepath = mStorageRef.child("ProImages").child(ProName).child(img.getLastPathSegment());
            filepath.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Toast.makeText(AlertDialogAct.this, " Image Successfull uploaded", Toast.LENGTH_SHORT).show();
                   // ProgressDialog.dismiss();
                    Uri downloadpic = taskSnapshot.getDownloadUrl();
                    // Picasso.with(getApplicationContext()).load(downloadpic).into(imgView);
                    sendImageUrl(downloadpic);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AlertDialogAct.this, " Image Failed to upload", Toast.LENGTH_SHORT).show();
                    //ProgressDialog.dismiss();

                }
            });
        }



    }


    private void checkCounterFirstThenUploadDataTEXTSKIP() {


        responseCount.child("textResValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                    //Toast.makeText(AlertDialogAct.this, "textResValue Get: " + counter, Toast.LENGTH_SHORT).show();

                    int count = Integer.parseInt(counter);
                    count++;
                    // Toast.makeText(AlertDialogAct.this, "textResValue Set: "+count, Toast.LENGTH_SHORT).show();
                    responseCount.child("textResValue").setValue(String.valueOf(count));
                    submitNotesREF.child(String.valueOf(count)).setValue(checkUser+" 00"+count+": //").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AlertDialogAct.this, "Notes Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                //alertBoxShow();
                                //gotoMainAct();
                            }else {
                                Toast.makeText(AlertDialogAct.this, "Error while uploading notes..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(AlertDialogAct.this, "Res count Value is null" , Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void checkInternet() {



        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        if (netInfo != null) {
            if (netInfo.isConnected()) {
                // Internet Available

            }else {
                noInternetAlert();
            }
        } else {
            //No internet
            noInternetAlert();
        }




    }

    private void noInternetAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AlertDialogAct.this);
        builder

                .setCancelable(false)
                .setTitle("No Internet..!")
                .setMessage("You cannot give the response without internet.. Try Again..!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        finish();
                        }
                });

        AlertDialog alert = builder.create();
        alert.show();



    }


    private void locationMange() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 1000, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    LatLng latLng = new LatLng(latitude, longitude);

                    List<Address> address = null;

                    Geocoder geocoder = new Geocoder(getApplicationContext());

                    try {
                        address = geocoder.getFromLocation(latitude, longitude, 1);

                        uploadLoaction();
                         locationManager.removeUpdates(this);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });


            //locationManager.removeUpdates(this);


        }catch (Exception e){
            Toast.makeText(this, "Check Network Connection..!", Toast.LENGTH_SHORT).show();
        }


        locationManager.removeUpdates(this);

    }


    private void uploadLoaction() {


        submitGpsREF = FirebaseDatabase.getInstance().getReference("ResponseData").child(user_idd).child(ProName).child("Gps");
        responseCount = FirebaseDatabase.getInstance().getReference("ResponseCount").child(user_idd).child(ProName);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
         //currentTime = Calendar.getInstance().getTime();

        String lat =   String.valueOf(latitude);
        String lang = String.valueOf(longitude);

        Track = new LocationLoc();
        Track.setLat(lat);
        Track.setLang(lang);
        //Track.setTitle("Date: "+date+"\nTime: "+currentTime+"\n");
        //Title Setting while uploading data

        //Checking start


        submitGpsREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue() == null){
                    Track.setTitle(checkUser+" 001: "+currentTime+"\n     ");
                    submitGpsREF.child("1").setValue(Track);
                    responseCount.child("GpsResValue").setValue("1");
                    Toast.makeText(AlertDialogAct.this, "Location Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                    //gotoMainAct();

                }else{

                    checkCounterFirstThenUploadDataGPS();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void checkCounterFirstThenUploadDataGPS() {


        responseCount.child("GpsResValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                    //Toast.makeText(AlertDialogAct.this, "GpsResValue GET: " + counter, Toast.LENGTH_SHORT).show();

                    int count = Integer.parseInt(counter);
                    count++;

                    Track.setTitle(checkUser+" 00"+count+": "+currentTime+"\n     ");

                    responseCount.child("GpsResValue").setValue(String.valueOf(count));
                    submitGpsREF.child(String.valueOf(count)).setValue(Track).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AlertDialogAct.this, "GPS Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                               // gotoMainAct();
                            }else {
                                Toast.makeText(AlertDialogAct.this, "Error while uploading GPS..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(AlertDialogAct.this, "Res count Value is null" , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void uploadLocationSKIP() {



        submitGpsREF = FirebaseDatabase.getInstance().getReference("ResponseData").child(user_idd).child(ProName).child("Gps");
        responseCount = FirebaseDatabase.getInstance().getReference("ResponseCount").child(user_idd).child(ProName);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //currentTime = Calendar.getInstance().getTime();

        //String lat =   String.valueOf(latitude);
        //String lang = String.valueOf(longitude);

        Track = new LocationLoc();
        Track.setLat(" //");
        Track.setLang(" //");
        //Track.setTitle("Date: "+date+"\nTime: "+currentTime+"\n");
        //Title Setting while uploading data

        //Checking start


        submitGpsREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue() == null){
                    Track.setTitle(checkUser+" 001: "+" // "+"\n     ");
                    submitGpsREF.child("1").setValue(Track);
                    responseCount.child("GpsResValue").setValue("1");
                    Toast.makeText(AlertDialogAct.this, "Location Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                    //gotoMainAct();

                }else{

                    checkCounterFirstThenUploadDataGPSSKIP();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    private void checkCounterFirstThenUploadDataGPSSKIP() {



        Track = new LocationLoc();
        Track.setLat(" //");
        Track.setLang(" //");


        responseCount.child("GpsResValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                    //Toast.makeText(AlertDialogAct.this, "GpsResValue GET: " + counter, Toast.LENGTH_SHORT).show();

                    int count = Integer.parseInt(counter);
                    count++;

                    Track.setTitle(checkUser+" 00"+count+": "+" //"+"\n     ");

                    responseCount.child("GpsResValue").setValue(String.valueOf(count));
                    submitGpsREF.child(String.valueOf(count)).setValue(Track).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AlertDialogAct.this, "GPS Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                // gotoMainAct();
                            }else {
                                Toast.makeText(AlertDialogAct.this, "Error while uploading GPS..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(AlertDialogAct.this, "Res count Value is null" , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void checkCounterFirstThenUploadDataTEXT() {


        responseCount.child("textResValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                    //Toast.makeText(AlertDialogAct.this, "textResValue Get: " + counter, Toast.LENGTH_SHORT).show();

                    int count = Integer.parseInt(counter);
                    count++;
                   // Toast.makeText(AlertDialogAct.this, "textResValue Set: "+count, Toast.LENGTH_SHORT).show();
                    responseCount.child("textResValue").setValue(String.valueOf(count));
                    submitNotesREF.child(String.valueOf(count)).setValue(checkUser+" 00"+count+": "+currentTime+"\n\nNotes:  "+notesTextEd.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AlertDialogAct.this, "Notes Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                //alertBoxShow();
                                //gotoMainAct();
                            }else {
                                Toast.makeText(AlertDialogAct.this, "Error while uploading notes..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(AlertDialogAct.this, "Res count Value is null" , Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void alertBoxShow() {




        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        if (netInfo != null) {
            if (netInfo.isConnected()) {
                // Internet Available


                AlertDialog.Builder builder = new AlertDialog.Builder(AlertDialogAct.this);
                builder

                        .setCancelable(false)
                        .setTitle("Image Response..!")
                        .setMessage("Do you want to give Image response ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {

                                //Intent cameraa = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //startActivityForResult(cameraa,CAMERA_REQUEST_CODE);
                                //takePhoto();

                                if (Build.VERSION.SDK_INT >= 24) {
                                    Toast.makeText(AlertDialogAct.this, "Open Camera..!", Toast.LENGTH_SHORT).show();
                                    openCameraIntent();
                                }else {
                                    Toast.makeText(AlertDialogAct.this, "Action Image..!", Toast.LENGTH_SHORT).show();
                                    Intent cameraa = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraa,REQUEST_CAPTURE_IMAGE2);
                                }


                                }
                        })
                        .setNegativeButton("Skip", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                //sending Image SKIP response


                                submitImgREF.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        if(dataSnapshot.getValue() == null){
                                            submitImgREF.child("1").setValue(checkUser+" 001 : //");
                                            responseCount.child("imageResValue").setValue("1");
                                            Toast.makeText(AlertDialogAct.this, "Url Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                            gotoMainAct();


                                        }else{

                                            checkCounterFirstThenUploadDataSKIP();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                gotoMainAct();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();



            }else {
                noInternetAlert();
            }
        } else {
            //No internet
            noInternetAlert();
        }






    }

    private void checkCounterFirstThenUploadDataSKIP() {




        responseCount.child("imageResValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                    //Toast.makeText(AlertDialogAct.this, "imageResValue GET: " + counter, Toast.LENGTH_SHORT).show();
                    int count = Integer.parseInt(counter);
                    count++;
                    //Toast.makeText(AlertDialogAct.this, "imageResValue GET: "+count, Toast.LENGTH_SHORT).show();
                    responseCount.child("imageResValue").setValue(String.valueOf(count));
                    submitImgREF.child(String.valueOf(count)).setValue(checkUser+" 00"+count+" :"+"//").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AlertDialogAct.this, "Image URL Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                gotoMainAct();
                            }else {
                                Toast.makeText(AlertDialogAct.this, "Error while uploading URL..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(AlertDialogAct.this, "Res count Value is null" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,getApplicationContext().getPackageName()+".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {

                Toast.makeText(this, "Camera Code 1", Toast.LENGTH_SHORT).show();

                img = Uri.fromFile(new File(imageFilePath));

                mPhotoCapturedImageView.setVisibility(View.VISIBLE);
                mPhotoCapturedImageView.setImageURI(img);

                /*final StorageReference filepath;
                if (img == null) {
                    Toast.makeText(this, "Device Not Giving File Path..!", Toast.LENGTH_SHORT).show();
                    ProgressDialog.dismiss();
                } else {
                    filepath = mStorageRef.child("ProImages").child(ProName).child(img.getLastPathSegment());
                    filepath.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Toast.makeText(AlertDialogAct.this, " Image Successfull uploaded", Toast.LENGTH_SHORT).show();
                            //ProgressDialog.dismiss();
                            Uri downloadpic = taskSnapshot.getDownloadUrl();
                            // Picasso.with(getApplicationContext()).load(downloadpic).into(imgView);
                            sendImageUrl(downloadpic);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AlertDialogAct.this, " Image Failed to upload", Toast.LENGTH_SHORT).show();
                            ProgressDialog.dismiss();

                        }
                    });
                }*/
            }

            //Coming From Previous Version

            else if (requestCode == REQUEST_CAPTURE_IMAGE2 && resultCode == RESULT_OK) {

                Toast.makeText(this, "Camera Code 2", Toast.LENGTH_SHORT).show();

                img = data.getData();
                mPhotoCapturedImageView.setVisibility(View.VISIBLE);
                mPhotoCapturedImageView.setImageURI(img);

                /*final StorageReference filepath;
                if (img == null) {
                    Toast.makeText(this, "Device Not Giving File Path..!", Toast.LENGTH_SHORT).show();
                    ProgressDialog.dismiss();
                } else {
                    filepath = mStorageRef.child("ProImages").child(ProName).child(img.getLastPathSegment());
                    filepath.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Toast.makeText(AlertDialogAct.this, " Image Successfull uploaded", Toast.LENGTH_SHORT).show();
                            ProgressDialog.dismiss();
                            Uri downloadpic = taskSnapshot.getDownloadUrl();
                            // Picasso.with(getApplicationContext()).load(downloadpic).into(imgView);
                            sendImageUrl(downloadpic);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AlertDialogAct.this, " Image Failed to upload", Toast.LENGTH_SHORT).show();
                            ProgressDialog.dismiss();

                        }
                    });
                }*/
            }


            }catch (Exception ex){

            Toast.makeText(this, "Mobile OS blocked the uploading Service..!\nDue to multiple process running.. Try Again..!", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendImageUrl(Uri downloadpic) {


        imageUrl = String.valueOf(downloadpic);


        submitImgREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue() == null){
                    submitImgREF.child("1").setValue(checkUser+" 001 :"+imageUrl);
                    responseCount.child("imageResValue").setValue("1");
                    Toast.makeText(AlertDialogAct.this, "Url Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                    //gotoMainAct();
                    finish();

                }else{

                    checkCounterFirstThenUploadData();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void checkCounterFirstThenUploadData() {


        responseCount.child("imageResValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                   // Toast.makeText(AlertDialogAct.this, "imageResValue GET: " + counter, Toast.LENGTH_SHORT).show();
                    int count = Integer.parseInt(counter);
                    count++;
                    //Toast.makeText(AlertDialogAct.this, "imageResValue GET: "+count, Toast.LENGTH_SHORT).show();
                    responseCount.child("imageResValue").setValue(String.valueOf(count));
                    submitImgREF.child(String.valueOf(count)).setValue(checkUser+" 00"+count+" :"+imageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AlertDialogAct.this, "Image URL Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                             //   gotoMainAct();
                                finish();
                            }else {
                                Toast.makeText(AlertDialogAct.this, "Error while uploading URL..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(AlertDialogAct.this, "Res count Value is null" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void closeAllAlarms() {


        Toast.makeText(this, "All Alarm Canceled..!", Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent1 = new Intent(getApplicationContext(), StartProjectAlarm.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
                getApplicationContext(), 0, myIntent1, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager1.cancel(pendingIntent1);



        AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent2 = new Intent(getApplicationContext(), EndProjectAlarm.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                getApplicationContext(), 5, myIntent2, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager2.cancel(pendingIntent2);



        AlarmManager alarmManager3 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent3 = new Intent(getApplicationContext(), MyAlarm1.class);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(
                getApplicationContext(), 1, myIntent3, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager3.cancel(pendingIntent3);



    }

    public  void gotoMainAct() {


        Intent goMainAct = new Intent(getApplicationContext(),HomeActivity.class);
        goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goMainAct);
        finish();
    }

    private void displayLocation() {


        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null) {
            uploadLoaction();
        }else {
            Toast.makeText(this, "GPS not Recorded..!", Toast.LENGTH_SHORT).show();
        }



    }

    private void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000)
                .setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();


    }

    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {

                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RES_REQUEST).show();
            } else {
                Toast.makeText(this, "This Device is not supported.!", Toast.LENGTH_SHORT).show();
                finish();
            }

            return false;
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //displayLocation();
        //startLocationUpdate();
        //Toast.makeText(this, "Client Connected..!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnectionSuspended(int i) {

        //mGoogleApiClient.connect();

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onLocationChanged(Location location) {



    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

}
