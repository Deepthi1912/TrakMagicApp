package com.example.usmansh.proofofconcept;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class CodeVerifyAct extends AppCompatActivity {

    Button Cv_Nextbt,Cv_Rescodebt;
    EditText Cv_codeed;
    TextView Cv_numDis;
    String Phno,numb,msgCode,SendmsgCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verify);

        //Sending Code on given mobile number
        Phno = getIntent().getStringExtra("Phno");
        sendCodeOnMobile();

        Cv_numDis    = (TextView)findViewById(R.id.Cv_DispNumb);
        Cv_Nextbt    = (Button)findViewById(R.id.Cv_Nextbt);
        Cv_Rescodebt = (Button)findViewById(R.id.Cv_ResCodebt);
        Cv_codeed    = (EditText)findViewById(R.id.Cv_codeed);


        Cv_numDis.setText(Cv_numDis.getText().toString()+" "+Phno);

        Cv_Nextbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(msgCode.equals(Cv_codeed.getText().toString()))
                {
                    Intent ChangePass = new Intent(getApplicationContext(),ChangePassAct.class);
                    ChangePass.putExtra("Phno",Phno);
                    ChangePass.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(ChangePass);
                    finish();

                }else
                {
                    Toast.makeText(CodeVerifyAct.this, "Error: Wrong Code. Try again..!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        Cv_Rescodebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendCodeOnMobile();

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
            Toast.makeText(CodeVerifyAct.this, "Code sent successfully..!", Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {

            Toast.makeText(CodeVerifyAct.this, "Code Not Sent..!", Toast.LENGTH_SHORT).show();
        }


    }




}
