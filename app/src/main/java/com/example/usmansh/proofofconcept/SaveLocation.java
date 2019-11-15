package com.example.usmansh.proofofconcept;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SaveLocation extends AppCompatActivity {

    DatabaseReference location;
    Button SLbt;
    EditText inputTitle;
    DatabaseReference submitGpsREF,responseCount;
    String ProName,userIdd;
    LocationLoc Track;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_location);


        location = FirebaseDatabase.getInstance().getReference("Location");

        SLbt = (Button)findViewById(R.id.SubLocbt);
        inputTitle =(EditText)findViewById(R.id.inputLocTitle);

        userIdd    = getIntent().getStringExtra("userId");
        ProName     = getIntent().getStringExtra("ProName");
        Toast.makeText(this, "ProName: "+ProName, Toast.LENGTH_SHORT).show();
        submitGpsREF = FirebaseDatabase.getInstance().getReference("ResponseData").child(userIdd).child(ProName).child("Gps");
        responseCount = FirebaseDatabase.getInstance().getReference("ResponseCount").child(userIdd).child(ProName);








        SLbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lat = String.valueOf(getIntent().getDoubleExtra("lat",0));
                String lang = String.valueOf(getIntent().getDoubleExtra("lang",0));

                Track = new LocationLoc();
                Track.setLat(lat);
                Track.setLang(lang);
                Track.setTitle(inputTitle.getText().toString());



                //Checking start


                submitGpsREF.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if(dataSnapshot.getValue() == null){
                            submitGpsREF.child("1").setValue(Track);
                            responseCount.child("GpsResValue").setValue("1");
                            Toast.makeText(SaveLocation.this, "Location Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                            gotoMainAct();


                        }else{

                            checkCounterFirstThenUploadData();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });



    }





    private void checkCounterFirstThenUploadData() {


        responseCount.child("GpsResValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                    Toast.makeText(SaveLocation.this, "GpsResValue GET: " + counter, Toast.LENGTH_SHORT).show();

                    int count = Integer.parseInt(counter);
                    count++;

                    Toast.makeText(SaveLocation.this, "GpsResValue SET: ", Toast.LENGTH_SHORT).show();
                    responseCount.child("GpsResValue").setValue(String.valueOf(count));
                    submitGpsREF.child(String.valueOf(count)).setValue(Track).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SaveLocation.this, "GPS Successfully uploaded..!", Toast.LENGTH_SHORT).show();
                                gotoMainAct();
                            }else {
                                Toast.makeText(SaveLocation.this, "Error while uploading GPS..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(SaveLocation.this, "Res count Value is null" , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public  void gotoMainAct() {


        Intent goMainAct = new Intent(getApplicationContext(),MainActivity.class);
        goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goMainAct);
        finish();
    }



}
