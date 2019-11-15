package com.example.usmansh.proofofconcept.Fragments;

//import android.app.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usmansh.proofofconcept.Adapter.ProListAdapter;
import com.example.usmansh.proofofconcept.Adapter.friendListAdapter;
import com.example.usmansh.proofofconcept.Project;
import com.example.usmansh.proofofconcept.R;
import com.example.usmansh.proofofconcept.SelectedUserProjects;
import com.example.usmansh.proofofconcept.User;
import com.example.usmansh.proofofconcept.seeFriendAct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MyFriendFragment extends Fragment {


    //Using singalton technique for instance
    ListView myfriendlistV;
    View view;

    //Getting Project List



    //Using singalton technique for instance
    private static MyFriendFragment INSTANCE = null;

    DatabaseReference getFrndListREF,delFromMyfrnd,delFromCount,DelfromUserFrnd,DelfromuserfrndCount  ;
    ArrayList<User> FrndList = new ArrayList<>();
    ArrayList<String> FrndNameList = new ArrayList<>();
    DatabaseReference getUsersListREF;
    ArrayList<User> UserList = new ArrayList<>();
    String frnd_id;
    String myId;
    FirebaseAuth mAuth;
    friendListAdapter frndadapter;

    public MyFriendFragment() {
        // Required empty public constructor
    }

    public static MyFriendFragment getInstance() {

        if (INSTANCE == null)
            INSTANCE = new MyFriendFragment();
        return INSTANCE;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentt

        view = inflater.inflate(R.layout.fragment_myfriend, container, false);
        myfriendlistV = (ListView)view.findViewById(R.id.myfriendlistV);

        frndadapter = new friendListAdapter(getActivity(),FrndList);
        myfriendlistV.setAdapter(frndadapter);

        //DataBase Linking
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        String[] parts = email.split("@");
        myId = parts[0];

       // Toast.makeText(getActivity(), "myId: "+myId, Toast.LENGTH_SHORT).show();

        FrndNameList.clear();
        UserList.clear();
        FrndList.clear();
        frndadapter.clearAdapter();
        getFrndNameList();



        myfriendlistV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent goForProData = new Intent(getActivity(), SelectedUserProjects.class);
                goForProData.putExtra("UserName",FrndNameList.get(position));
                startActivity(goForProData);
            }
        });


        myfriendlistV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false)
                        .setMessage("Do you want to Delete this Friend ?")
                        .setTitle("Delete Friend..!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                DelfromUserFrnd = FirebaseDatabase.getInstance().getReference("MyFriend").child(FrndNameList.get(position));
                                DelfromuserfrndCount = FirebaseDatabase.getInstance().getReference("MyFriendCount").child(FrndNameList.get(position));
                                delFromMyfrnd = FirebaseDatabase.getInstance().getReference("MyFriend").child(myId);
                                delFromCount = FirebaseDatabase.getInstance().getReference("MyFriendCount").child(myId);

                                checkCounterFirstThenDeleteFRND(position);
                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                //send notification msg received
                                Toast.makeText(getActivity(), "Friend Cancel..!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });


        return view;
        //return inflater.inflate(R.layout.fragment_myfriend, container, false);
    }

    private void selectFrndsFromUser() {


        //removing existing frnd data
        for(int i=0;i< FrndNameList.size();i++){

            String frndName = FrndNameList.get(i);
            for(int j=0;j<UserList.size();j++){
                User user = UserList.get(j);
                String email = user.getUsername();
                String[] parts = email.split("@");
                String user_name = parts[0]; // 004
                if(frndName.equalsIgnoreCase(user_name)){
                    FrndList.add(UserList.get(j));
                    frndadapter.notifyDataSetChanged();
                }
            }
        }


    }

    private void getUserAllUserList() {

        //Toast.makeText(getActivity(), "Getting Users", Toast.LENGTH_SHORT).show();
        getUsersListREF = FirebaseDatabase.getInstance().getReference("RegUsers");
        getUsersListREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postData:dataSnapshot.getChildren()){

                    User user = postData.getValue(User.class);
                    if(user != null) {
                        String email = user.getUsername();
                        String[] parts = email.split("@");
                        String user_id = parts[0]; // 004

                        if(!user_id.equalsIgnoreCase(myId))
                            UserList.add(user);

                    }else{
                        Toast.makeText(getActivity(), "List Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }

                selectFrndsFromUser();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getFrndNameList() {

        //Toast.makeText(getActivity(), "Getting Friends", Toast.LENGTH_SHORT).show();
        getFrndListREF = FirebaseDatabase.getInstance().getReference("MyFriend").child(myId);
        getFrndListREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postData : dataSnapshot.getChildren()) {
                    if (postData != null) {
                        FrndNameList.add(postData.getValue(String.class));
                    } else {
                        Toast.makeText(getActivity(), "Data is Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }

                getUserAllUserList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




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
                                Toast.makeText(getActivity(), "Friend Successfully Removed..!", Toast.LENGTH_SHORT).show();
                                //update adapter
                                frndadapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(getActivity(), "Error while Removing Friend..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }else{
                    Toast.makeText(getActivity(), "Friend count Value is null" , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

}
