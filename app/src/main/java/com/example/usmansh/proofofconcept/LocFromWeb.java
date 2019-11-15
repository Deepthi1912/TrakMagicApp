package com.example.usmansh.proofofconcept;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LocFromWeb extends AppCompatActivity {


    Button webBt,setLocationBt;
    EditText latitude,longitude;
    WebView mWebView;
    String userId,ProName;
    DatabaseReference responseCount,submitGpsREF;
    LocationLoc Track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_from_web);

        userId = getIntent().getStringExtra("userId");
        ProName = getIntent().getStringExtra("ProName");

        setLocationBt  = (Button)findViewById(R.id.setLocationBt);
        webBt          = (Button)findViewById(R.id.webBt);
        mWebView       = (WebView) findViewById(R.id.webV);
        latitude       = (EditText)findViewById(R.id.latEd);
        longitude      = (EditText)findViewById(R.id.longEd);


        webBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    WebSettings webSettings = mWebView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    mWebView.loadUrl("https://www.latlong.net/convert-address-to-lat-long.html");
                }
                catch (Exception ex){
                    Toast.makeText(LocFromWeb.this, "Website going to close now", Toast.LENGTH_SHORT).show();
                }

            }
        });


        setLocationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(latitude.getText().toString()) || TextUtils.isEmpty(latitude.getText().toString())){
                    Toast.makeText(LocFromWeb.this, "Error: Data is Missing..!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(LocFromWeb.this, "Your Destination has set successfully..!", Toast.LENGTH_SHORT).show();
                    uploadLoaction();
                }
            }
        });



    }





    public  void gotoMainAct() {


        Intent goMainAct = new Intent(getApplicationContext(),HomeActivity.class);
        goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goMainAct);
        finish();
    }
    private void uploadLoaction() {


         submitGpsREF = FirebaseDatabase.getInstance().getReference("ResponseData").child(userId).child(ProName).child("Destination");
         responseCount = FirebaseDatabase.getInstance().getReference("ResponseCount").child(userId).child(ProName);

        String lat =   latitude.getText().toString().trim();
        String lang = longitude.getText().toString().trim();

        Track = new LocationLoc();
        Track.setLat(lat);
        Track.setLang(lang);

        //Checking start
          submitGpsREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue() == null){
                    Track.setTitle("001: "+ Calendar.getInstance().getTime()+"\n     ");
                    submitGpsREF.child("1").setValue(Track);
                    responseCount.child("DestResValue").setValue("1");
                    Toast.makeText(LocFromWeb.this, "Destination Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                    gotoMainAct();

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


        responseCount.child("DestResValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                    //Toast.makeText(AlertDialogAct.this, "GpsResValue GET: " + counter, Toast.LENGTH_SHORT).show();

                    int count = Integer.parseInt(counter);
                    count++;

                    Track.setTitle("00"+count+": "+Calendar.getInstance().getTime()+"\n     ");

                    responseCount.child("DestResValue").setValue(String.valueOf(count));
                    submitGpsREF.child(String.valueOf(count)).setValue(Track).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LocFromWeb.this, "Destination Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                gotoMainAct();
                            }else {
                                Toast.makeText(LocFromWeb.this, "Error while uploading GPS..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(LocFromWeb.this, "Res count Value is null" , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }





}
