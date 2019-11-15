package com.example.usmansh.proofofconcept;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class RegCodeVeriAct extends AppCompatActivity {


    Button Rv_Nextbt,Rv_Resend_code_bt;
    EditText Rv_code_ed;
    Button verifyBt;
    DatabaseReference RegUserData;
    FirebaseAuth mAuth;
    String Phno,UserName,Password,numb,msgCode,SendmsgCode,inpUserName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_code_veri);

            UserName = getIntent().getStringExtra("UserName");
            inpUserName = getIntent().getStringExtra("inpUserName");
            Password = getIntent().getStringExtra("Password");
            Phno     = getIntent().getStringExtra("PhNo");


        sendCodeOnMobile();


        Rv_Nextbt = (Button)findViewById(R.id.Rv_Nextbt);
        Rv_Resend_code_bt = (Button)findViewById(R.id.Rv_Resend_code_bt);
        Rv_code_ed = (EditText)findViewById(R.id.Rv_code_ed);

        mAuth = FirebaseAuth.getInstance();
        RegUserData = FirebaseDatabase.getInstance().getReference("RegUsers");




        Rv_Nextbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(msgCode.equals(Rv_code_ed.getText().toString()))
                {
                    createAccount();
                }else
                {
                    Toast.makeText(RegCodeVeriAct.this, "Error: Wrong Code. Try again..!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        Rv_Resend_code_bt.setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(RegCodeVeriAct.this, "Code sent successfully..!", Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {

            Toast.makeText(RegCodeVeriAct.this, "Code Not Sent..!", Toast.LENGTH_SHORT).show();
        }


    }


    private void createAccount() {

        UserName = UserName.toLowerCase() +"@poc.com";

        Toast.makeText(this, "UserName: "+UserName, Toast.LENGTH_SHORT).show();


         mAuth.createUserWithEmailAndPassword(UserName,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    UploadingUserData();

                }else{
                    Toast.makeText(RegCodeVeriAct.this, "Authentication Error..!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    private void UploadingUserData() {


        User user = new User();
        user.setPhno(Phno);
        user.setUsername(UserName);
        user.setPassword(Password);

        RegUserData.child(inpUserName.toLowerCase()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(RegCodeVeriAct.this, "You have Registered Successfully..!", Toast.LENGTH_SHORT).show();
                    Intent goRegMsgAct = new Intent(getApplicationContext(),RegisteredMsgAct.class);
                    goRegMsgAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goRegMsgAct);
                    finish();

                }else{

                    Toast.makeText(RegCodeVeriAct.this, "Registration Error..!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    @Override
    public void onBackPressed() {
        Intent goLoginAct = new Intent(getApplicationContext(),RegistrationAct.class);
        goLoginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goLoginAct);
        finish();
    }
}
