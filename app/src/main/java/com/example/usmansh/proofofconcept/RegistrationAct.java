package com.example.usmansh.proofofconcept;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RegistrationAct extends AppCompatActivity {


    TextView TermCond, PrivacyPol;
    EditText inputPhno, inputUserName,inputPassword,inputCode;
    Button registerBt,cancelRegBt,verifyBt;
    DatabaseReference RegUserData;
    FirebaseAuth mAuth;
    String Phno,UserName,Password,numb,msgCode,SendmsgCode;
    private int SMS_PERMISSION_CODE = 1;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        grantPermissions();

        TermCond      = (TextView)findViewById(R.id.textView13);
        PrivacyPol    = (TextView)findViewById(R.id.textView12);
        inputPhno     = (EditText)findViewById(R.id.inputPhno);
        inputUserName = (EditText)findViewById(R.id.inputUserName);
        inputPassword = (EditText)findViewById(R.id.inputPassword);
        registerBt    = (Button)findViewById(R.id.RegBt);
        cancelRegBt   = (Button)findViewById(R.id.cancelReg);

        mAuth = FirebaseAuth.getInstance();
        RegUserData = FirebaseDatabase.getInstance().getReference("RegUsers");


        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });



        cancelRegBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         Intent goLoginAct = new Intent(getApplicationContext(),LoginAct.class);
                goLoginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goLoginAct);
                finish();
            }
        });



        TermCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://trakmagic.com/?page_id=56"));
                startActivity(browserIntent);
            }
        });


        PrivacyPol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://trakmagic.com/?page_id=61"));
                startActivity(browserIntent);
            }
        });

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
                    Toast.makeText(RegistrationAct.this, "Authentication Error..!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void registerUser() {

         Phno = inputPhno.getText().toString().trim();
         UserName = inputUserName.getText().toString().trim();
         Password = inputPassword.getText().toString().trim();


        if(TextUtils.isEmpty(UserName)){
            Toast.makeText(this, "Enter User Name", Toast.LENGTH_SHORT).show();
        }
        else if(UserName.contains(" ")){
            Toast.makeText(this, "White Space is not allowed in User Name", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }

        else if(Password.length() < 6){
            Toast.makeText(this, "Password must contain 6 characters at least", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(Phno)){
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }else if(Phno.contains(" ")){
            Toast.makeText(this, "White Space is not allowed in Phone no..!", Toast.LENGTH_SHORT).show();
        }



        else{
            createAccount();

        }


    }

    private void CodeVerification() {

        Intent codeVerify = new Intent(getApplicationContext(),RegCodeVeriAct.class);
        codeVerify.putExtra("UserName",UserName);
        codeVerify.putExtra("inpUserName",inputUserName.getText().toString().trim());
        codeVerify.putExtra("Password",Password);
        codeVerify.putExtra("PhNo",Phno);
        codeVerify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(codeVerify);
        finish();

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

                Log.d("Phone: ",numb);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(numb, null, SendmsgCode, null, null);
                Toast.makeText(RegistrationAct.this, "Code sent successfully..!", Toast.LENGTH_SHORT).show();
                CodeVerification();

            } catch (Exception ex) {

                Toast.makeText(RegistrationAct.this, "Code Not Sent..!", Toast.LENGTH_SHORT).show();
            }


        }

    private void UploadingUserData() {
    
    
        User user = new User();
        user.setPhno(Phno);
        user.setUsername(UserName);
        user.setPassword(Password);
        
        RegUserData.child(inputUserName.getText().toString().toLowerCase()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                
                if(task.isSuccessful()){

                    Toast.makeText(RegistrationAct.this, "You have Registered Successfully..!", Toast.LENGTH_SHORT).show();
                    Intent goLoginAct = new Intent(getApplicationContext(),LoginAct.class);
                    goLoginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goLoginAct);
                    finish();

                }else{

                    Toast.makeText(RegistrationAct.this, "Registration Error..!", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
        
        
    }


        //Permission access

    private void grantPermissions() {


        if(ContextCompat.checkSelfPermission(RegistrationAct.this,
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                             ){
        }else{

            requestPermission();
        }



    }

    private void requestPermission() {


        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)){

            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed..!")
                    .setMessage("This permission is needed to Complete your App functionalities..!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(RegistrationAct.this,new String[] {
                                    android.Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else{

            ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.SEND_SMS},SMS_PERMISSION_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == SMS_PERMISSION_CODE){

            if(grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this, "Permission GRANTED..!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permission DENIED..!", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    public void onBackPressed() {
        Intent goLoginAct = new Intent(getApplicationContext(),LoginAct.class);
        goLoginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goLoginAct);
        finish();
    }
}
