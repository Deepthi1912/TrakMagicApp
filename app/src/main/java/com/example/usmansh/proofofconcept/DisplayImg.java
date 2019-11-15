package com.example.usmansh.proofofconcept;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DisplayImg extends AppCompatActivity {

    ImageView displayImg;
    TextView imgProNamelabel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_img);


        imgProNamelabel = (TextView)findViewById(R.id.imgProNamelabel);
        displayImg      = (ImageView)findViewById(R.id.displayImg);


        String ProName  = getIntent().getStringExtra("ProName");
        String imgUrl   = getIntent().getStringExtra("listdata");

        //Toast.makeText(this, "LINK: "+imgUrl, Toast.LENGTH_SHORT).show();
        imgProNamelabel.setText("Project Name: "+ProName);
        Log.d("URLLLL: ",imgUrl);
        Picasso.with(getApplicationContext()).load(imgUrl).into(displayImg);


    }
}
