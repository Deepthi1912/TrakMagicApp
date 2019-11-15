package com.example.usmansh.proofofconcept;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class seeMyProjectAct extends AppCompatActivity {

    Button delAllPro;
    ListView MyProListV;
    TextView currentProTxtt;
    DatabaseReference getProListREF;
    ArrayList<Project> ProjectList = new ArrayList<>();
    ArrayList<String> ProNameList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    String currentProNamee="";

    DatabaseReference delActiveUser,delMyproject,delResponseCount,delResponseData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_my_project);




        MyProListV      = (ListView)findViewById(R.id.MyProListV);
        currentProTxtt  = (TextView)findViewById(R.id.currentProTxt);
        delAllPro       = (Button)findViewById(R.id.delAllPro);

        final String userId = getIntent().getStringExtra("userId");
        getProListREF = FirebaseDatabase.getInstance().getReference("MyProjects").child(userId);

        delActiveUser = FirebaseDatabase.getInstance().getReference("ActiveUsers");
        delMyproject  = FirebaseDatabase.getInstance().getReference("MyProjects");
        delResponseCount = FirebaseDatabase.getInstance().getReference("ResponseCount");
        delResponseData  =FirebaseDatabase.getInstance().getReference("ResponseData");




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
                            currentProNamee = project.getProname();
                        }
                    }
                    else{
                        Toast.makeText(seeMyProjectAct.this, "List Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }


                if(!currentProNamee.equals("")) {
                    //setActiveProject(currentProNamee);
                    currentProTxtt.setText(currentProNamee);
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
        MyProListV.setAdapter(arrayAdapter);





        MyProListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Project pro = ProjectList.get(i);
                ProReminder reminders = pro.getProreminder();

                Toast.makeText(seeMyProjectAct.this, "Selected Pro Name: "+pro.getProname(), Toast.LENGTH_SHORT).show();
                Intent goForProData = new Intent(getApplicationContext(),ShowProDetails.class);
                goForProData.putExtra("userId",userId);
                goForProData.putExtra("comingFrom","MyProject");
                goForProData.putExtra("ProOwner",pro.getPromanager());
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






    delAllPro.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            AlertDialog.Builder builder = new AlertDialog.Builder(seeMyProjectAct.this);
            builder

                    .setCancelable(false)
                    .setMessage("Do you want to Delete All Projects ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {

                            delActiveUser.child(userId).removeValue();
                            delMyproject.child(userId).removeValue();
                            delResponseCount.child(userId).removeValue();
                            delResponseData.child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(seeMyProjectAct.this, "Projects Deleted Successfully..!", Toast.LENGTH_SHORT).show();
                                        closeAllAlarms();
                                        Intent goMainAct = new Intent(getApplicationContext(),MainActivity.class);
                                        goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(goMainAct);
                                        finish();
                                    }else{
                                        Toast.makeText(seeMyProjectAct.this, "Projects Not Deleted..!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });



                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            //send notification msg received
                            dialog.dismiss();
                        }
                    })
                    .create().show();


        }
    });



    }





    private void closeAllAlarms() {


        Toast.makeText(this, "All Alarm Canceled..!", Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent1 = new Intent(getApplicationContext(), StartProjectAlarm.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(
                getApplicationContext(), 0, myIntent1, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager1.cancel(pendingIntent1);



        AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent2 = new Intent(getApplicationContext(), EndProjectAlarm.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                getApplicationContext(), 5, myIntent2, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager2.cancel(pendingIntent2);



        AlarmManager alarmManager3 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent3 = new Intent(getApplicationContext(), MyAlarm1.class);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(
                getApplicationContext(), 1, myIntent3, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager3.cancel(pendingIntent3);

         }


}
