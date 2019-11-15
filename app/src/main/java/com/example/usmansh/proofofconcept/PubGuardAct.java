package com.example.usmansh.proofofconcept;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PubGuardAct extends AppCompatActivity {

    Button volBt,volCanBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_guard);


            volBt    = (Button)findViewById(R.id.volBt);
            volCanBt = (Button)findViewById(R.id.volCanBt);


            volBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent gotoRegPG = new Intent(getApplicationContext(),RegPublicGuard.class);
                    gotoRegPG.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gotoRegPG);
                    finish();
                   }
                });



            volCanBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    gotoMainAct();
                }
            });

    }



    public  void gotoMainAct() {


        Intent goMainAct = new Intent(getApplicationContext(),HomeActivity.class);
        goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goMainAct);
        finish();
    }



}
