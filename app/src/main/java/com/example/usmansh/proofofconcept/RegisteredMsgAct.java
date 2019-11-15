package com.example.usmansh.proofofconcept;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegisteredMsgAct extends AppCompatActivity {

    Button goLoginAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_msg);


        goLoginAct = (Button)findViewById(R.id.goLoginAct);

        goLoginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent goLoginAct = new Intent(getApplicationContext(),LoginAct.class);
                goLoginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goLoginAct);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Press Login..!", Toast.LENGTH_SHORT).show();
    }
}
