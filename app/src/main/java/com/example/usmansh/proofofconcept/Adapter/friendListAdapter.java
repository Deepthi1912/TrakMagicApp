package com.example.usmansh.proofofconcept.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usmansh.proofofconcept.R;
import com.example.usmansh.proofofconcept.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class friendListAdapter  extends BaseAdapter {


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



    public friendListAdapter(Context c, ArrayList<User> user)

    {
        this.userList = user;
        this.contxt = c;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int i, View view, ViewGroup parent)

    {

        View V = new View(contxt);
        LayoutInflater inflat = (LayoutInflater) contxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            V = inflat.inflate(R.layout.frndlistdesign, null);
        } else {
            V = view;
        }


        final TextView FListNametv = (TextView) V.findViewById(R.id.FListnametv);
        final TextView FListStatustv = (TextView) V.findViewById(R.id.FListstatustv);
        final ImageView FListImgV = (ImageView) V.findViewById(R.id.FListImgV);

        user = userList.get(i);
        String email = user.getUsername();
        String[] parts = email.split("@");
        String user_id = parts[0]; // 004

        FListNametv.setText(user_id);
        FListStatustv.setText("Status will show here");

        return V;


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

