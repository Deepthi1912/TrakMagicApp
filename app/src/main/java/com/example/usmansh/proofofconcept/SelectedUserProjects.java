package com.example.usmansh.proofofconcept;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectedUserProjects extends AppCompatActivity {


    ListView ProListV;
    DatabaseReference getProListREF;
    ArrayList<Project> ProjectList = new ArrayList<>();
    ArrayList<String> ProNameList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    String ProNamee,ProManager,currentProName="";
    TextView UsercurrentProTxtt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_user_projects);


        ProManager = getIntent().getStringExtra("UserName");

        ProListV = (ListView)findViewById(R.id.ProListV);
        UsercurrentProTxtt = (TextView)findViewById(R.id.userCurrentProTxt);

        getProListREF = FirebaseDatabase.getInstance().getReference("MyProjects").child(ProManager);

        getProListREF.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot postData:dataSnapshot.getChildren()){

                    Project project = postData.getValue(Project.class);

                    if(project != null) {
                        ProNameList.add(project.getProname());
                        ProjectList.add(project);
                        arrayAdapter.notifyDataSetChanged();


                        if(project.getIsactive() != null && project.getIsactive().equalsIgnoreCase("ON")) {
                            currentProName = project.getProname();
                        }


                    }else{
                        Toast.makeText(SelectedUserProjects.this, "List Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }

                if(!currentProName.equals("")) {
                    //setActiveProject(currentProName);
                    UsercurrentProTxtt.setText(currentProName);
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, ProNameList){
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent){
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
        ProListV.setAdapter(arrayAdapter);




        ProListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Project pro = ProjectList.get(i);
                ProReminder reminders = pro.getProreminder();

                Toast.makeText(SelectedUserProjects.this, "Selected Pro Name: "+pro.getProname(), Toast.LENGTH_SHORT).show();
                Toast.makeText(SelectedUserProjects.this, "ProManager if: "+ProManager, Toast.LENGTH_SHORT).show();
                Intent goForProData = new Intent(getApplicationContext(),ShowProDetails.class);
                goForProData.putExtra("comingFrom","UserProject");
                goForProData.putExtra("userId",ProManager);
                goForProData.putExtra("ProName",pro.getProname());
                goForProData.putExtra("proActive",pro.getIsactive());
                goForProData.putExtra("syear",pro.getSyear());
                goForProData.putExtra("smonth",pro.getSmonth());
                goForProData.putExtra("sday",pro.getSday());
                goForProData.putExtra("shour",pro.getShour());
                goForProData.putExtra("sminute",pro.getSminute());
                goForProData.putExtra("eyear",pro.getEyear());
                goForProData.putExtra("emonth",pro.getEmonth());
                goForProData.putExtra("eday",pro.getEday());
                goForProData.putExtra("ehour",pro.getEhour());
                goForProData.putExtra("eminute",pro.getEminute());
                goForProData.putExtra("rem1H",reminders.getRem1hour());
                goForProData.putExtra("rem1M",reminders.getRem1minute());
                goForProData.putExtra("rem2H",reminders.getRem2hour());
                goForProData.putExtra("rem2M",reminders.getRem2minute());
                goForProData.putExtra("rem3H",reminders.getRem3hour());
                goForProData.putExtra("rem3M",reminders.getRem3minute());
                goForProData.putExtra("rem4H",reminders.getRem4hour());
                goForProData.putExtra("rem4M",reminders.getRem4minute());
                startActivity(goForProData);

            }
        });





    }

    private void setActiveProject(String currentProName) {

        UsercurrentProTxtt.setText(currentProName);
        UsercurrentProTxtt.invalidate();
        UsercurrentProTxtt.requestLayout();

    }

}
