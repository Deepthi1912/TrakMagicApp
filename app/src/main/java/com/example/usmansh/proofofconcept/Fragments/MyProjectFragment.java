package com.example.usmansh.proofofconcept.Fragments;

//import android.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.usmansh.proofofconcept.Adapter.ProListAdapter;
import com.example.usmansh.proofofconcept.HomeActivity;
import com.example.usmansh.proofofconcept.Project;
import com.example.usmansh.proofofconcept.R;
import com.example.usmansh.proofofconcept.addProjectAct;
import com.example.usmansh.proofofconcept.seeMyProjectAct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyProjectFragment extends Fragment implements View.OnClickListener{

        //Using singalton technique for instance
    private static MyProjectFragment INSTANCE = null;
    RelativeLayout startNewProRL;
    ListView myProListview;
    View view;

    //Getting Project List
    DatabaseReference getProListREF;
    ArrayList<Project> ProjectList = new ArrayList<>();
    ArrayList<String> ProNameList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    String currentProNamee="";
    String userId;
    DatabaseReference delActiveUser,delMyproject,delResponseCount,delResponseData;
    ProListAdapter proAdapter;
    FirebaseAuth mAuth;




    public  MyProjectFragment(){
        //Required Public empty constructor
    }


    public static MyProjectFragment getInstance() {

        if (INSTANCE == null)
            INSTANCE = new MyProjectFragment();
            return INSTANCE;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_myproject, container, false);
        startNewProRL = (RelativeLayout) view.findViewById(R.id.startNewProRL);
        startNewProRL.setOnClickListener(this);

        myProListview = (ListView)view.findViewById(R.id.myProListview);


        //DataBase Linking
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        String[] parts = email.split("@");
        userId = parts[0];

        //userId = getActivity().getIntent().getStringExtra("userId");
        getProListREF = FirebaseDatabase.getInstance().getReference("MyProjects").child(userId);

        delActiveUser = FirebaseDatabase.getInstance().getReference("ActiveUsers");
        delMyproject  = FirebaseDatabase.getInstance().getReference("MyProjects");
        delResponseCount = FirebaseDatabase.getInstance().getReference("ResponseCount");
        delResponseData  =FirebaseDatabase.getInstance().getReference("ResponseData");


        proAdapter = new ProListAdapter(getActivity(),ProjectList);
        myProListview.setAdapter(proAdapter);
        proAdapter.clearAdapter();
        getProjectList();

        myProListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Clicked Item", Toast.LENGTH_SHORT).show();
                Intent gotoSeeProject = new Intent(getActivity(), seeMyProjectAct.class);
                gotoSeeProject.putExtra("userId", userId);
                startActivity(gotoSeeProject);
            }
        });


        return view;

        //return inflater.inflate(R.layout.fragment_myproject, container, false);
    }


    private void getProjectList() {



        getProListREF.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot postData:dataSnapshot.getChildren()){

                    Project project = postData.getValue(Project.class);

                    if(project != null) {
                        ProNameList.add(project.getProname());
                        ProjectList.add(project);
                        proAdapter.notifyDataSetChanged();

                        if(project.getIsactive() != null && project.getIsactive().equalsIgnoreCase("ON")) {
                            currentProNamee = project.getProname();
                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "List Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }


                proAdapter = new ProListAdapter(getActivity(),ProjectList);
                myProListview.setAdapter(proAdapter);



                if(!currentProNamee.equals("")) {
                    //setActiveProject(currentProNamee);
                    //currentProTxtt.setText(currentProNamee);
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }



    @Override
    public void onClick(View v) {

        //do what you want to do when button is clickedd
        switch (v.getId()) {
            case R.id.startNewProRL:
                Toast.makeText(getActivity(), "Start New Pro..!", Toast.LENGTH_SHORT).show();
                Intent gotoAddProject = new Intent(getActivity(), addProjectAct.class);
                gotoAddProject.putExtra("userId",userId);
                startActivity(gotoAddProject);
                break;
        }
    }
}
