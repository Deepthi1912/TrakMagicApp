package com.example.usmansh.proofofconcept.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmansh.proofofconcept.R;
import com.example.usmansh.proofofconcept.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class peopleListAdapter extends BaseAdapter {






    DatabaseReference addIntoMyfrnd,frndCount,addIntoUserFrnd,userfrndCount,getFrndListREF;
    FirebaseAuth mAuth;
    DatabaseReference getUsersListREF;
    ArrayList<User> UserList = new ArrayList<>();
    ArrayList<String> UserNameList = new ArrayList<>();
    ArrayList<String> FrndNameList = new ArrayList<>();
    String myId;
    boolean frndAlreadyExist = false;
    AlertDialog.Builder adb;

    ArrayList<User> userList;
    Context contxt = null;
    User user = null;



    public peopleListAdapter(Context c, ArrayList<User> user)

    {
        this.userList = user;
        this.contxt = c;

        //calling functions
        getmyId();
        getUserList();
        getFriendList();

    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int i, View view, ViewGroup parent)

    {

        // Toast.makeText(contxt, "In Adaptor", Toast.LENGTH_SHORT).show();

        View V = new View(contxt);

        LayoutInflater inflat = (LayoutInflater) contxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (view == null) {
            V = inflat.inflate(R.layout.peoplelistdesign, null);
        } else {
            V = view;
        }


        final TextView ppNametv = (TextView) V.findViewById(R.id.FListnametv);
        final TextView ppStatustv = (TextView) V.findViewById(R.id.PPstatustv);
        final ImageButton ppAdduser = (ImageButton) V.findViewById(R.id.PPadduserbt);

        user = userList.get(i);

        String email = user.getUsername();
        String[] parts = email.split("@");
        String user_id = parts[0]; // 004

        ppNametv.setText(user_id);
        ppStatustv.setText("Status will show here");


        //Set on click listner
        ppAdduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                adb = new AlertDialog.Builder(contxt);
                adb.setTitle("Add Friend..!");
                adb.setMessage("Do you want to Add this Person in your Friend List ?");
                adb.setIcon(R.drawable.person);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        makingFriend(i);


                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                adb.show();

            }
        });


        return V;


    }



    private void getmyId() {

        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        String[] parts = email.split("@");
        myId = parts[0];

    }

    private void getUserList() {

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
                        String user_id = parts[0]; // 004

                        if(!user_id.equalsIgnoreCase(myId))
                            UserNameList.add(user_id);
                       // arrayAdapter.notifyDataSetChanged();

                    }else{
                        Toast.makeText(contxt, "List Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getFriendList(){


        getFrndListREF = FirebaseDatabase.getInstance().getReference("MyFriend").child(myId);


        getFrndListREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postData : dataSnapshot.getChildren()) {

                    if (postData != null) {

                        FrndNameList.add(postData.getValue(String.class));
                        //Toast.makeText(ShowProDataList.this, "dataName size: "+dataNames.size(), Toast.LENGTH_SHORT).show();
                        // arrayAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(contxt, "Data is Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void makingFriend(final int i) {

            // Internet Available

                for(int j= 0 ; j < FrndNameList.size() ; j++ ){
                    if(UserNameList.get(i).equals(FrndNameList.get(j))){
                        frndAlreadyExist = true;
                    }
                }


                if(frndAlreadyExist){
                    Toast.makeText(contxt, "User Already Exist in your Friend List..!", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(contxt, "Friend Successfully added..!", Toast.LENGTH_SHORT).show();
                               // gotoMainAct();

                            }else{

                                checkCounterFirstThenUploadDataFRND(i);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


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
                                Toast.makeText(contxt, "Friend Successfully added..!", Toast.LENGTH_SHORT).show();
                                //gotoMainAct();

                            }else {
                                Toast.makeText(contxt, "Error while adding Friend..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(contxt, "Friend count Value is null" , Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }





    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void clearAdapter() {
        userList.clear();
    }

}

