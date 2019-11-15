package com.example.usmansh.proofofconcept;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegPublicGuard extends AppCompatActivity {


    Button regPGBt,cancelPGBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_public_guard);


        regPGBt = (Button)findViewById(R.id.RegPGBt);
        cancelPGBt = (Button)findViewById(R.id.cancelPGReg);



        regPGBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegPublicGuard.this, "Your request for Public Guardian has been submit..!", Toast.LENGTH_SHORT).show();
                gotoMainAct();
            }
        });



        cancelPGBt.setOnClickListener(new View.OnClickListener() {
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
