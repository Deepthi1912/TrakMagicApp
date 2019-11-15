
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

import java.util.ArrayList;

public class seeFriendAct extends AppCompatActivity {

    ListView frndListV;
    DatabaseReference getFrndListREF,delFromMyfrnd,delFromCount,DelfromUserFrnd,DelfromuserfrndCount  ;
    ArrayList<User> FrndList = new ArrayList<>();
    ArrayList<String> FrndNameList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    String frnd_id;
    String myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_project);


        frndListV = (ListView)findViewById(R.id.frndListV);

        myId = getIntent().getStringExtra("myId");

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
                        Toast.makeText(seeFriendAct.this, "Data is Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, FrndNameList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.parseColor("#ffffff"));
                tv.setTextSize(16);

                // Generate ListView Item using TextView
                return view;
            }
        };
        frndListV.setAdapter(arrayAdapter);




       frndListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent goForProData = new Intent(getApplicationContext(),SelectedUserProjects.class);
                goForProData.putExtra("UserName",FrndNameList.get(i));
                startActivity(goForProData);
            }
        });




       frndListV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

               AlertDialog.Builder builder = new AlertDialog.Builder(seeFriendAct.this);
               builder

                       .setCancelable(false)
                       .setMessage("Do you want to Delete this Friend ?")
                       .setTitle("Delete Friend..!")
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

                                       DelfromUserFrnd = FirebaseDatabase.getInstance().getReference("MyFriend").child(FrndNameList.get(position));
                                       DelfromuserfrndCount = FirebaseDatabase.getInstance().getReference("MyFriendCount").child(FrndNameList.get(position));

                                       delFromMyfrnd = FirebaseDatabase.getInstance().getReference("MyFriend").child(myId);
                                       delFromCount = FirebaseDatabase.getInstance().getReference("MyFriendCount").child(myId);
                                       checkCounterFirstThenDeleteFRND(position);

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
                               Toast.makeText(seeFriendAct.this, "Friend Cancel..!", Toast.LENGTH_SHORT).show();
                               dialog.dismiss();
                           }
                       });

               AlertDialog alert = builder.create();
               alert.show();
               return false;
           }
       });


    }




    private void noInternetAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(seeFriendAct.this);
        builder

                .setCancelable(false)
                .setTitle("No Internet..!")
                .setMessage("You cannot delete the friend without internet.. Try Again..!")
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


    private void checkCounterFirstThenDeleteFRND(final int position) {



        delFromCount.child("CountFrndValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {

                    String counter = dataSnapshot.getValue(String.class);
                    //Toast.makeText(AlertDialogAct.this, "textResValue Get: " + counter, Toast.LENGTH_SHORT).show();

                    int count = Integer.parseInt(counter);
                    count--;

                    DelfromUserFrnd.child(myId).removeValue();
                    DelfromuserfrndCount.child("CountFrndValue").setValue(String.valueOf(count));


                    delFromCount.child("CountFrndValue").setValue(String.valueOf(count));
                    delFromMyfrnd.child(FrndNameList.get(position)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(seeFriendAct.this, "Friend Successfully Removed..!", Toast.LENGTH_SHORT).show();
                                gotoMainAct();

                            }else {
                                Toast.makeText(seeFriendAct.this, "Error while Removing Friend..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(seeFriendAct.this, "Friend count Value is null" , Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public  void gotoMainAct() {


        Intent goMainAct = new Intent(getApplicationContext(),MainActivity.class);
        goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goMainAct);
        finish();
    }


}
