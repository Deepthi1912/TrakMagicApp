package com.example.usmansh.proofofconcept;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ShowProDetails extends AppCompatActivity {

    Button showNotesBt,showImagesBt,showLocationsBt,ProSettingBt,AddResponse,DestinationBt,showDesbt;
    String ProName,userId;
    String ComingFrom,proStatus;
    boolean responseIsOn = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pro_details);


            showNotesBt     = (Button)findViewById(R.id.showNotesbt);
            showImagesBt    = (Button)findViewById(R.id.showImgBt);
            showLocationsBt = (Button)findViewById(R.id.showLocBt);
            showDesbt       = (Button)findViewById(R.id.showDesbt);
            ProSettingBt    = (Button)findViewById(R.id.ProSettingBt);
            AddResponse     = (Button)findViewById(R.id.ProAddResponse);
            DestinationBt   = (Button)findViewById(R.id.Destinationbt);

        AddResponse.setBackgroundColor(Color.parseColor("#2979ff"));
        DestinationBt.setBackgroundColor(Color.parseColor("#2979ff"));
            //ProSettingBt.setVisibility(View.INVISIBLE);
            proStatus = getIntent().getStringExtra("proActive");

        /*if(ComingFrom != null && ComingFrom.equalsIgnoreCase("MyProject")){
            ProSettingBt.setVisibility(View.VISIBLE);
        }*/

        if(proStatus != null && proStatus.equalsIgnoreCase("OFF")){
            //AddResponse.setVisibility(View.INVISIBLE);

            responseIsOn = false;
            AddResponse.setBackgroundColor(Color.parseColor("#616161"));
            DestinationBt.setBackgroundColor(Color.parseColor("#616161"));
        }

        ComingFrom = getIntent().getStringExtra("comingFrom");
        userId = getIntent().getStringExtra("userId");
        ProName = getIntent().getStringExtra("ProName");



            showNotesBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent showNotes = new Intent(getApplicationContext(),ShowProDataList.class);
                    showNotes.putExtra("userId",userId);
                    showNotes.putExtra("dataType","Text");
                    showNotes.putExtra("ProName",ProName);
                    startActivity(showNotes);
                }
            });




            showImagesBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent showNotes = new Intent(getApplicationContext(),ShowProDataList.class);
                    showNotes.putExtra("dataType","Image");
                    showNotes.putExtra("userId",userId);
                    showNotes.putExtra("ProName",ProName);
                    startActivity(showNotes);
                }
            });


            showLocationsBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent showNotes = new Intent(getApplicationContext(),ShowProDataList.class);
                    showNotes.putExtra("userId",userId);
                    showNotes.putExtra("dataType","Gps");
                    showNotes.putExtra("ProName",ProName);
                    startActivity(showNotes);

                }
            });



            showDesbt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent showNotes = new Intent(getApplicationContext(),ShowProDataList.class);
                    showNotes.putExtra("userId",userId);
                    showNotes.putExtra("dataType","Destination");
                    showNotes.putExtra("ProName",ProName);
                    startActivity(showNotes);


                }
            });


            ProSettingBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent goProSettingAct = new Intent(getApplicationContext(),ProjectSettingAct.class);
                    goProSettingAct.putExtra("ProName",ProName);
                    goProSettingAct.putExtra("comingFrom",getIntent().getStringExtra("comingFrom"));
                    goProSettingAct.putExtra("userId",getIntent().getStringExtra("userId"));
                    goProSettingAct.putExtra("proActive",getIntent().getStringExtra("proActive"));
                    goProSettingAct.putExtra("syear",getIntent().getIntExtra("syear",0));
                    goProSettingAct.putExtra("smonth",getIntent().getIntExtra("smonth",0));
                    goProSettingAct.putExtra("sday",getIntent().getIntExtra("sday",0));
                    goProSettingAct.putExtra("shour",getIntent().getIntExtra("shour",0));
                    goProSettingAct.putExtra("sminute",getIntent().getIntExtra("sminute",0));
                    goProSettingAct.putExtra("eyear",getIntent().getIntExtra("eyear",0));
                    goProSettingAct.putExtra("emonth",getIntent().getIntExtra("emonth",0));
                    goProSettingAct.putExtra("eday",getIntent().getIntExtra("eday",0));
                    goProSettingAct.putExtra("ehour",getIntent().getIntExtra("ehour",0));
                    goProSettingAct.putExtra("eminute",getIntent().getIntExtra("eminute",0));
                    goProSettingAct.putExtra("rem1H",getIntent().getIntExtra("rem1H",0));
                    goProSettingAct.putExtra("rem1M",getIntent().getIntExtra("rem1M",0));
                    goProSettingAct.putExtra("rem2H",getIntent().getIntExtra("rem2H",0));
                    goProSettingAct.putExtra("rem2M",getIntent().getIntExtra("rem2M",0));
                    goProSettingAct.putExtra("rem3H",getIntent().getIntExtra("rem3H",0));
                    goProSettingAct.putExtra("rem3M",getIntent().getIntExtra("rem3M",0));
                    goProSettingAct.putExtra("rem4H",getIntent().getIntExtra("rem4H",0));
                    goProSettingAct.putExtra("rem4M",getIntent().getIntExtra("rem4M",0));
                    startActivity(goProSettingAct);


                }
            });



            AddResponse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       if(responseIsOn) {
                        Intent i = new Intent(getApplicationContext(), AlertDialogAct.class);
                        i.putExtra("comingAddRes", "AddResponse");
                        i.putExtra("userId", userId);
                        i.putExtra("ProName", ProName);
                        i.putExtra("remCode", 6);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
            });




            DestinationBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(responseIsOn) {
                        Intent goLocfromWeb = new Intent(getApplicationContext(), LocFromWeb.class);
                        goLocfromWeb.putExtra("userId", userId);
                        goLocfromWeb.putExtra("ProName", ProName);
                        startActivity(goLocfromWeb);
                    }

                }
            });



    }
}
