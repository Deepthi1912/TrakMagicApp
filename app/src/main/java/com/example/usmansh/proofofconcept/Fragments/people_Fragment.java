package com.example.usmansh.proofofconcept.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.usmansh.proofofconcept.Adapter.peopleListAdapter;
import com.example.usmansh.proofofconcept.R;
import com.example.usmansh.proofofconcept.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class people_Fragment extends Fragment implements View.OnClickListener{



    ListView userListV;
    DatabaseReference getUsersListREF;
    ArrayList<User> UserList = new ArrayList<>();
    ArrayList<String> UserNameList = new ArrayList<>();
    ArrayList<String> FrndNameList = new ArrayList<>();
    FloatingActionButton invitefrndbt;
    ArrayAdapter<String> arrayAdapter;
    String user_id;
    String myId;
    peopleListAdapter adapter;
    boolean frndAlreadyExist = false;
    View view;
    EditText searchuserEd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view         = inflater.inflate(R.layout.fragment_people_, container, false);
        userListV    = (ListView)view.findViewById(R.id.fraguserListV);
        invitefrndbt = (FloatingActionButton)view.findViewById(R.id.invitefrnFbt);
        searchuserEd = (EditText)view.findViewById(R.id.searchfrndEd);


        //myId = getActivity().getIntent().getStringExtra("myId");

        myId = getArguments().getString("userId");

        getUsersListREF = FirebaseDatabase.getInstance().getReference("RegUsers");
        getUsersListREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot postData:dataSnapshot.getChildren()){

                    User user = postData.getValue(User.class);

                    if(user != null) {

                        String email = user.getUsername();
                        String[] parts = email.split("@");
                        user_id = parts[0]; // 004

                        if(!user_id.equalsIgnoreCase(myId))
                            //UserNameList.add(user_id);
                            //arrayAdapter.notifyDataSetChanged();
                        UserList.add(user);
                        adapter.notifyDataSetChanged();

                    }else{
                        Toast.makeText(getActivity(), "List Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new peopleListAdapter(getActivity(),UserList);
        userListV.setAdapter(adapter);


        invitefrndbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showInviteFrndDialog(view);
            }
        });


        //Searching Function coding
        searchuserEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //When user changed the Text
                ArrayList<User> secondarr = new ArrayList<User>();
                for (int i = 0; i < UserList.size(); i++) {
                    User per = UserList.get(i);
                    if (per.getUsername().contains(cs))
                    {
                        secondarr.add(per);
                    }
                }
                userListV.setAdapter(new peopleListAdapter(getActivity(), secondarr));
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        return view;

        //return inflater.inflate(R.layout.fragment_people_, container, false);
    }




    private void showInviteFrndDialog(View v) {


        //Dialog Box Function and its Coding

            final Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.invitefrnd_dialog);

            //linking with dialog.xml components
        final EditText inviteNum   = (EditText)dialog.findViewById(R.id.inviteNumb);
        final EditText inviteMsg   = (EditText)dialog.findViewById(R.id.inviteMesg);
        final Button sendInviteBtt = (Button)dialog.findViewById(R.id.Invitebt);
        final Button closeDBbt     = (Button)dialog.findViewById(R.id.closeDBbt);


        sendInviteBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sedning message

                String numb = inviteNum.getText().toString().trim();
                String msg = inviteMsg.getHint().toString();

                if (TextUtils.isEmpty(numb)) {
                    Toast.makeText(getActivity(), "Enter Mobile Number..!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(getActivity(), "Enter Invite Message..!", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(numb, null, msg, null, null);
                        Toast.makeText(getActivity(), "Message sent successfully..!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                    } catch (Exception ex) {

                        Toast.makeText(getActivity(), "Message Not Sent..!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });




        closeDBbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });



    dialog.show();

    }






    @Override
    public void onClick(View v) {



    }
}
