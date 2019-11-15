package com.example.usmansh.proofofconcept;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class LoginAct extends AppCompatActivity {


    EditText logEmail, logPassword;
    Button logBt, logReg,resPass,HomeBt;
    String email, password;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        logEmail    = (EditText) findViewById(R.id.logEmail);
        logPassword = (EditText) findViewById(R.id.logPassword);
        logBt       = (Button) findViewById(R.id.logBt);
        logReg      = (Button) findViewById(R.id.logReg);
        resPass     = (Button)findViewById(R.id.resPass);


       /* HomeBt      = (Button)findViewById(R.id.homebt);
        HomeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(goHome);
            }
        });*/


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent goLoginAct = new Intent(getApplicationContext(), HomeActivity.class);
                    goLoginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goLoginAct);
                    finish();
                }
            }
        };


        logBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = logEmail.getText().toString();
                password = logPassword.getText().toString();

                LoginUser();
            }
        });


        logReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goRegAct = new Intent(getApplicationContext(), RegistrationAct.class);
                goRegAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goRegAct);
                finish();


            }
        });


        resPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goReSetPassAct = new Intent(getApplicationContext(), ResetPasswordAct.class);
                startActivity(goReSetPassAct);

            }
        });


    }




    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void LoginUser() {

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
        email = email.toLowerCase()+"@poc.com";

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Intent goLoginAct = new Intent(getApplicationContext(),HomeActivity.class);
                    goLoginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goLoginAct);
                    finish();
                }else{
                    Toast.makeText(LoginAct.this, "Login Error..!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }
        else {
            Toast.makeText(this, "Field is empty..!", Toast.LENGTH_SHORT).show();
        }

    }
}
