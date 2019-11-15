package com.example.usmansh.proofofconcept;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChangePassAct extends AppCompatActivity {


    Button Cp_Finishbt;
    EditText Cp_NewPassed,Cp_verifyPassed;
    String newPass,verNewPass;
    DatabaseReference getUserData;
    FirebaseUser user;
    FirebaseAuth mAuth;
    ArrayList<User> AllUsersList = new ArrayList<>();
    User RegUser;
    String PhNo;
    String user_id,previousPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        PhNo = getIntent().getStringExtra("Phno");

        Cp_Finishbt     = (Button)findViewById(R.id.Cp_Finishbt);
        Cp_NewPassed    = (EditText)findViewById(R.id.Cp_NewPassed);
        Cp_verifyPassed = (EditText)findViewById(R.id.Cp_VerPassed);

        mAuth = FirebaseAuth.getInstance();
        getUserData = FirebaseDatabase.getInstance().getReference("RegUsers");
        getAllusers();


        Cp_Finishbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newPass    = Cp_NewPassed.getText().toString().trim();
                verNewPass = Cp_verifyPassed.getText().toString().trim();

                if(TextUtils.isEmpty(newPass)){
                    Toast.makeText(ChangePassAct.this, "Enter New Password..!", Toast.LENGTH_SHORT).show();
                }
                else if(newPass.length() < 6){
                    Toast.makeText(ChangePassAct.this, "Password must contain 6 characters at least", Toast.LENGTH_SHORT).show();
                }
                else if(!newPass.equals(verNewPass)){
                    Toast.makeText(ChangePassAct.this, "Password not matching..!", Toast.LENGTH_SHORT).show();
                }
                else {

                    if(AllUsersList.size() > 0){
                    for(int i=0; i<AllUsersList.size() ; i++){
                         RegUser = AllUsersList.get(i);
                        if(RegUser.getPhno().equals(PhNo)){
                            //Toast.makeText(ChangePassAct.this, "Matching User PhNo", Toast.LENGTH_SHORT).show();
                            previousPass = RegUser.getPassword();
                            UpdatePassword(RegUser.getUsername());
                        }

                    }
                    }else{
                        Toast.makeText(ChangePassAct.this, "Loading Data..Try Again..!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }






    private void UpdatePassword(String username) {

        String email = username;
        String[] parts = email.split("@");
         user_id = parts[0]; // 004

        if( verNewPass.length() > 5) {

           // Toast.makeText(this, "Logging In..!", Toast.LENGTH_SHORT).show();
            mAuth.signInWithEmailAndPassword(username,previousPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        user = FirebaseAuth.getInstance().getCurrentUser();
                        if( verNewPass.length() > 5) {
                            assert user != null;
                            user.updatePassword(verNewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ChangePassAct.this, "Auth Pass Updated..!", Toast.LENGTH_SHORT).show();
                                    updateUserDataFunc();
                                }

                            });
                        }else{
                            Toast.makeText(ChangePassAct.this, "Password Must contain at lest 6 characters", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(ChangePassAct.this, "Sign In Error..!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(ChangePassAct.this, "Password Must contain at lest 6 characters", Toast.LENGTH_SHORT).show();
        }

    }


    private void getAllusers() {


        getUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postData : dataSnapshot.getChildren()) {

                    User user = postData.getValue(User.class);

                    if (user != null) {
                        AllUsersList.add(user);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void updateUserDataFunc() {


        getUserData.child(user_id).child("password").setValue(verNewPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if(task.isSuccessful()) {
                    Toast.makeText(ChangePassAct.this, "Password Updated Successfully..!", Toast.LENGTH_SHORT).show();

                    Intent goMain = new Intent(getApplicationContext(), LoginAct.class);
                    goMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goMain);
                    finish();
                }
                else{
                    Toast.makeText(ChangePassAct.this, "Error: Password not updated..!", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }


}
