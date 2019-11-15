package com.example.usmansh.proofofconcept;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayNote extends AppCompatActivity {

    TextView displayTxt;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);


        displayTxt = (TextView)findViewById(R.id.displayTxt);

        String ProName = getIntent().getStringExtra("ProName");
        String listdata = getIntent().getStringExtra("listdata");

        String lat  = getIntent().getStringExtra("lat");
        String lang = getIntent().getStringExtra("lang");

        if(lat != null && lang !=null) {
            displayTxt.setText("Project Name: " + ProName + "\n\n" +"Location Name: "+ listdata+
                                    "\n\nLatitude: "+lat+"\nLongitude: "+lang);
        }else{
            displayTxt.setText("Project Name: " + ProName + "\n\n" + listdata);
        }

    }
}
