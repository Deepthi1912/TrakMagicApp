package com.example.usmansh.proofofconcept;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.NameList;

import java.util.ArrayList;

public class addFriendAct extends AppCompatActivity {

    DatabaseReference addIntoMyfrnd,frndCount,addIntoUserFrnd,userfrndCount,getFrndListREF;
    ListView userListV;
    DatabaseReference getUsersListREF;
    ArrayList<User> UserList = new ArrayList<>();
    ArrayList<String> UserNameList = new ArrayList<>();
    ArrayList<String> FrndNameList = new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;
    String user_id;
    String myId;
    boolean frndAlreadyExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);




        userListV = (ListView)findViewById(R.id.userListV);

        myId = getIntent().getStringExtra("myId");

        addIntoMyfrnd   = FirebaseDatabase.getInstance().getReference("MyFriend");

              getUsersListREF = FirebaseDatabase.getInstance().getReference("RegUsers");
        getUsersListREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot postData:dataSnapshot.getChildren()){

                    User user = postData.getValue(User.class);

                    if(user != null) {

                        UserList.add(user);

                        String email = user.getUsername();
                        String[] parts = email.split("@");
                        user_id = parts[0]; // 004

                        if(!user_id.equalsIgnoreCase(myId))
                            UserNameList.add(user_id);
                        arrayAdapter.notifyDataSetChanged();

                    }else{
                        Toast.makeText(addFriendAct.this, "List Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getFrndListREF = FirebaseDatabase.getInstance().getReference("MyFriend").child(myId);


        getFrndListREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postData : dataSnapshot.getChildren()) {

                    if (postData != null) {

                        FrndNameList.add(postData.getValue(String.class));
                        //Toast.makeText(ShowProDataList.this, "dataName size: "+dataNames.size(), Toast.LENGTH_SHORT).show();
                        arrayAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(addFriendAct.this, "Data is Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, UserNameList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setTextSize(16);

                // Generate ListView Item using TextView
                return view;
            }
        };

        userListV.setAdapter(arrayAdapter);




        userListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                Toast.makeText(addFriendAct.this, "Selected User: "+UserNameList.get(i), Toast.LENGTH_SHORT).show();



                AlertDialog.Builder builder = new AlertDialog.Builder(addFriendAct.this);
                builder

                        .setCancelable(false)
                        .setMessage("Do you want to Add this Person in your Friend List ?")
                        .setTitle("Add Friend..!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                //send notification msg received
                               // Toast.makeText(addFriendAct.this, "User Added to be Friend", Toast.LENGTH_SHORT).show();

                                ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                assert mgr != null;
                                NetworkInfo netInfo = mgr.getActiveNetworkInfo();
                                if (netInfo != null) {
                                    if (netInfo.isConnected()) {
                                        // Internet Available

                                            for(int j= 0 ; j < FrndNameList.size() ; j++ ){
                                                if(UserNameList.get(i).equals(FrndNameList.get(j))){
                                                    frndAlreadyExist = true;
                                                }
                                            }


                                            if(frndAlreadyExist){
                                                Toast.makeText(addFriendAct.this, "User Already Exist in your Friend List..!", Toast.LENGTH_LONG).show();
                                                frndAlreadyExist = false;
                                            }else{


                                                addIntoUserFrnd = FirebaseDatabase.getInstance().getReference("MyFriend").child(UserNameList.get(i));
                                                userfrndCount   = FirebaseDatabase.getInstance().getReference("MyFriendCount").child(UserNameList.get(i));
                                                addIntoMyfrnd = FirebaseDatabase.getInstance().getReference("MyFriend").child(myId);
                                                frndCount = FirebaseDatabase.getInstance().getReference("MyFriendCount").child(myId);


                                                addIntoMyfrnd.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                                        if(dataSnapshot.getValue() == null){

                                                            addIntoUserFrnd.child(myId).setValue(myId);
                                                            addIntoMyfrnd.child(UserNameList.get(i)).setValue(UserNameList.get(i));

                                                            userfrndCount.child("CountFrndValue").setValue("1");
                                                            frndCount.child("CountFrndValue").setValue("1");
                                                            Toast.makeText(addFriendAct.this, "Friend Successfully added..!", Toast.LENGTH_SHORT).show();
                                                            gotoMainAct();

                                                        }else{

                                                            checkCounterFirstThenUploadDataFRND(i);
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                    }else {
                                        noInternetAlert();
                                    }
                                } else {
                                    //No internet
                                    noInternetAlert();
                                }



                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                //send notification msg received
                                Toast.makeText(addFriendAct.this, "Friend Cancel..!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();


            }

        });




    }

    private void checkCounterFirstThenUploadDataFRND(final int position) {



        frndCount.child("CountFrndValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                    //Toast.makeText(AlertDialogAct.this, "textResValue Get: " + counter, Toast.LENGTH_SHORT).show();

                    int count = Integer.parseInt(counter);
                    count++;
                    // Toast.makeText(AlertDialogAct.this, "textResValue Set: "+count, Toast.LENGTH_SHORT).show();
                    frndCount.child("CountFrndValue").setValue(String.valueOf(count));
                    userfrndCount.child("CountFrndValue").setValue(String.valueOf(count));

                    addIntoUserFrnd.child(myId).setValue(myId);
                    addIntoMyfrnd.child(UserNameList.get(position)).setValue(UserNameList.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(addFriendAct.this, "Friend Successfully added..!", Toast.LENGTH_SHORT).show();
                                gotoMainAct();

                            }else {
                                Toast.makeText(addFriendAct.this, "Error while adding Friend..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(addFriendAct.this, "Friend count Value is null" , Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    private void noInternetAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(addFriendAct.this);
        builder

                .setCancelable(false)
                .setTitle("No Internet..!")
                .setMessage("You cannot add Friend without internet.. Try Again..!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();



    }



    public  void gotoMainAct() {


        Intent goMainAct = new Intent(getApplicationContext(),MainActivity.class);
        goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goMainAct);
        finish();
    }


}
