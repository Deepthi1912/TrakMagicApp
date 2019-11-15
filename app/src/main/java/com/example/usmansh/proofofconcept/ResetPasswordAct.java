package com.example.usmansh.proofofconcept;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class ResetPasswordAct extends AppCompatActivity {


    Button Res_NextBt;
    EditText Res_phnoed;
    String Phno, msgCode,SendmsgCode,numb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        Res_phnoed = (EditText)findViewById(R.id.Res_phnoed);
        Res_NextBt = (Button)findViewById(R.id.Res_Nextbt);


        Res_NextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Phno = Res_phnoed.getText().toString().trim();

                if(TextUtils.isEmpty(Phno)){
                    Toast.makeText(ResetPasswordAct.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                }else if(Phno.contains(" ")){
                    Toast.makeText(ResetPasswordAct.this, "White Space is not allowed in Phone no..!", Toast.LENGTH_SHORT).show();
                }else{

                    Intent gonext = new Intent(getApplicationContext(),CodeVerifyAct.class);
                    gonext.putExtra("Phno",Phno);
                    gonext.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gonext);
                    finish();

                }
            }
        });


    }




    private void sendCodeOnMobile() {


        Random r = new Random();
        int low = 1001;
        int high = 9999;
        int randNum = r.nextInt(high - low) + low;


         numb = Phno;
         msgCode = String.valueOf(randNum);
         SendmsgCode = "Your Verification Code for TrackMagic is: "+String.valueOf(randNum);

        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numb, null, SendmsgCode, null, null);
            Toast.makeText(ResetPasswordAct.this, "Code sent successfully..!", Toast.LENGTH_SHORT).show();


        } catch (Exception ex) {

            Toast.makeText(ResetPasswordAct.this, "Code Not Sent..!", Toast.LENGTH_SHORT).show();
        }


    }




}
