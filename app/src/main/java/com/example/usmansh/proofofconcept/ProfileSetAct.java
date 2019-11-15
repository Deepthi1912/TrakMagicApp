package com.example.usmansh.proofofconcept;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileSetAct extends AppCompatActivity {


    TextView ProfName;
    EditText ProfPassword;
    Button  ProfDoneBt,ProfCancelBt;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference getUserData,updateUserData;
    String user_id;
    User RegUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set);


            ProfName     = (TextView)findViewById(R.id.ProfEmail);

            ProfPassword = (EditText)findViewById(R.id.ProfPassword);

            ProfDoneBt   = (Button)findViewById(R.id.ProfDoneBt);
            ProfCancelBt = (Button)findViewById(R.id.ProfCancelBt);

            mAuth = FirebaseAuth.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();
            getUserData = FirebaseDatabase.getInstance().getReference("RegUsers");





        String email = mAuth.getCurrentUser().getEmail();
        String[] parts = email.split("@");
          user_id = parts[0]; // 004



    //Fetching User object data
        getUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 RegUser = dataSnapshot.child(user_id).getValue(User.class);
                if(RegUser != null){

                    ProfName.setText(user_id);
                    ProfPassword.setText(RegUser.getPassword());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        ProfDoneBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if( ProfPassword.getText().length() > 5) {

                        user.updatePassword(ProfPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                updateUserDataFunc();
                            }

                        });
                    }else{
                        Toast.makeText(ProfileSetAct.this, "Password Must contain at lest 6 characters", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            ProfCancelBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent goMain = new Intent(getApplicationContext(), HomeActivity.class);
                    goMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goMain);
                    finish();

                }
            });



    }




    private void updateUserDataFunc() {


        getUserData.child(user_id).child("password").setValue(ProfPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if(task.isSuccessful()) {
                    Toast.makeText(ProfileSetAct.this, "Password Updated..!", Toast.LENGTH_SHORT).show();

                    Intent goMain = new Intent(getApplicationContext(), HomeActivity.class);
                    goMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goMain);
                    finish();
                }
                else{
                    Toast.makeText(ProfileSetAct.this, "Error: Password not updated..!", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }
}
