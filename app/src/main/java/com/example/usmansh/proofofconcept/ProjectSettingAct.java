package com.example.usmansh.proofofconcept;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class ProjectSettingAct extends AppCompatActivity {

    TextView ProDetSet,ProRemSet;
    String ProName,userId,ProActiveStatus,comingFrom;
    int syear,smonth,sday,shour,sminute;
    int eyear,emonth,eday,ehour,eminute;
    int rem1H,rem1M,rem2H,rem2M,rem3H,rem3M,rem4H,rem4M;
    String startDetails,endDetails;
    String rem1D,rem2D,rem3D,rem4D;
    Button closeProject,cancelProject,editReminders;
    DatabaseReference projectActive,myProjectList;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_setting);


        ProDetSet = (TextView)findViewById(R.id.ProDetSet);
        ProRemSet = (TextView)findViewById(R.id.ProRemSet);

        closeProject  = (Button)findViewById(R.id.closeProject);
        cancelProject = (Button)findViewById(R.id.cancelProject);
        editReminders = (Button)findViewById(R.id.editReminders);


        projectActive = FirebaseDatabase.getInstance().getReference("ActiveUsers");
        myProjectList = FirebaseDatabase.getInstance().getReference("MyProjects");

        comingFrom =  getIntent().getStringExtra("comingFrom");
        ProActiveStatus = getIntent().getStringExtra("proActive");
        userId   = getIntent().getStringExtra("userId");
        ProName  = getIntent().getStringExtra("ProName");
        syear    = getIntent().getIntExtra("syear",0);
        smonth   = getIntent().getIntExtra("smonth",0);
        sday     = getIntent().getIntExtra("sday",0);
        shour    = getIntent().getIntExtra("shour",0);
        sminute  = getIntent().getIntExtra("sminute",0);
        eyear    = getIntent().getIntExtra("eyear",0);
        emonth   = getIntent().getIntExtra("emonth",0);
        eday     = getIntent().getIntExtra("eday",0);
        ehour    = getIntent().getIntExtra("ehour",0);
        eminute  = getIntent().getIntExtra("eminute",0);
        rem1H    = getIntent().getIntExtra("rem1H",0);
        rem1M    = getIntent().getIntExtra("rem1M",0);
        rem2H    = getIntent().getIntExtra("rem2H",0);
        rem2M    = getIntent().getIntExtra("rem2M",0);
        rem3H    = getIntent().getIntExtra("rem3H",0);
        rem3M    = getIntent().getIntExtra("rem3M",0);
        rem4H    = getIntent().getIntExtra("rem4H",0);
        rem4M    = getIntent().getIntExtra("rem4M",0);



        closeProject.setVisibility(View.INVISIBLE);
        editReminders.setVisibility(View.INVISIBLE);

        Toast.makeText(this, "Coming From: "+comingFrom, Toast.LENGTH_SHORT).show();



        ShowProDetailsOnSettingScreen(ProName);
        ShowRemDetailsOnSettingScreen();

        if(ProActiveStatus!=null && comingFrom!=null&&
                ProActiveStatus.equalsIgnoreCase("ON") && comingFrom.equalsIgnoreCase("MyProject")){

            closeProject.setVisibility(View.VISIBLE);
            editReminders.setVisibility(View.VISIBLE);
        }


        closeProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectSettingAct.this);
                builder

                        .setCancelable(false)
                        .setMessage("Do you want to Close the Project ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {

                                projectActive.child(userId).child("isActive").setValue("no").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){

                                            myProjectList.child(userId).child(ProName).child("isactive").setValue("OFF");

                                            closeAllAlarms();

                                            Toast.makeText(ProjectSettingAct.this, "Your Project has been closed successfully..!", Toast.LENGTH_SHORT).show();
                                            Intent goMainAct = new Intent(getApplicationContext(),HomeActivity.class);
                                            goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(goMainAct);
                                            finish();
                                        }else{
                                            Toast.makeText(ProjectSettingAct.this, "", Toast.LENGTH_SHORT).show();
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




        editReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent gotoSetReminder = new Intent(getApplicationContext(), setReminderAct.class);
                gotoSetReminder.putExtra("ProName",ProName);
                gotoSetReminder.putExtra("coming","ProSet");

                if(sday != eday) {
                    gotoSetReminder.putExtra("dayChoice", "MD");
                }
                startActivityForResult(gotoSetReminder, 2);


            }
        });



        cancelProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {

            Toast.makeText(this, "Reminders Set Successfully..!", Toast.LENGTH_SHORT).show();
            rem1H = data.getIntExtra("rem1H",0);
            rem1M = data.getIntExtra("rem1M",0);
            rem2H = data.getIntExtra("rem2H",0);
            rem2M = data.getIntExtra("rem2M",0);
            rem3H = data.getIntExtra("rem3H",0);
            rem3M = data.getIntExtra("rem3M",0);
            rem4H = data.getIntExtra("rem4H",0);
            rem4M = data.getIntExtra("rem4M",0);

            updateRemindersOnDatabase();

            ShowProDetailsOnSettingScreen(ProName);
            ShowRemDetailsOnSettingScreen();


        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Reminders is not Set successfully..!", Toast.LENGTH_SHORT).show();

        }
    }



    private void updateRemindersOnDatabase() {

        ProReminder proReminder = new ProReminder();
        proReminder.setRem1hour(rem1H);
        proReminder.setRem1minute(rem1M);
        proReminder.setRem2hour(rem2H);
        proReminder.setRem2minute(rem2M);
        proReminder.setRem3hour(rem3H);
        proReminder.setRem3minute(rem3M);
        proReminder.setRem4hour(rem4H);
        proReminder.setRem4minute(rem4M);

         project = new Project();

        project.setProreminder(proReminder);
        project.setIsactive("ON");
        project.setPromanager(userId);
        project.setProname(ProName);
        project.setSyear(syear);
        project.setSmonth(smonth);
        project.setSday(sday);
        project.setShour(shour);
        project.setSminute(sminute);
        project.setEyear(eyear);
        project.setEmonth(emonth);
        project.setEday(eday);
        project.setEhour(ehour);
        project.setEminute(eminute);



        myProjectList.child(userId).child(ProName).setValue(project).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(ProjectSettingAct.this, "Project Updated on My Project List..!", Toast.LENGTH_SHORT).show();
                    projectActive.child(userId).child("isActive").setValue("yes");
                    Intent goMainAct = new Intent(getApplicationContext(),HomeActivity.class);
                    goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goMainAct);
                    finish();

                }else{
                    Toast.makeText(ProjectSettingAct.this, "Error: Not Updated in My Project List", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*projectList.child(ProName).setValue(project).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(ProjectSettingAct.this, "Project's Data Updated..!", Toast.LENGTH_SHORT).show();
                    projectActive.child(userId).child("isActive").setValue("yes");
                    sendToMyProject();

                    //finish();
                } else {
                    Toast.makeText(ProjectSettingAct.this, "Data is not Updated..!", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


    }




    private void sendToMyProject() {


        myProjectList.child(userId).child(ProName).setValue(project).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(ProjectSettingAct.this, "Project Updated on My Project List..!", Toast.LENGTH_SHORT).show();
                    projectActive.child(userId).child("isActive").setValue("yes");
                    Intent goMainAct = new Intent(getApplicationContext(),HomeActivity.class);
                    goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goMainAct);
                    finish();

                }else{
                    Toast.makeText(ProjectSettingAct.this, "Error: Not Updated in My Project List", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }








    @SuppressLint("SetTextI18n")
    private void ShowProDetailsOnSettingScreen(String proName) {




        if(shour<12){
            startDetails = "Start Time: "+shour+" : "+sminute+" AM\nStart Date: "+sday+"/"+smonth+"/"+syear+"\n\n";

        }else {

            startDetails = "Start Time: "+shour+" : "+sminute+" PM\nStart Date: "+sday+"/"+smonth+"/"+syear+"\n\n";

        }

        if(ehour<12){
            endDetails   = "End Time: "+ehour+" : "+eminute+" AM\nEnd Date: "+eday+"/"+emonth+"/"+eyear;

        }else{
            endDetails   = "End Time: "+ehour+" : "+eminute+" PM\nEnd Date: "+eday+"/"+emonth+"/"+eyear;

        }

        ProDetSet.setText("Project Status: "+ProActiveStatus+"\n\nProject Title: "+ProName+"\n\n"+startDetails+endDetails);




    }

    @SuppressLint("SetTextI18n")
    private void ShowRemDetailsOnSettingScreen() {

        if(rem1H < 12){
            rem1D = "Rem 1 Set: "+rem1H+":"+rem1M+" AM";
        }else{
            rem1D = "Rem 1 Set: "+rem1H+":"+rem1M+" PM";
        }


        if(rem2H < 12){
            rem2D = "Rem 2 Set: "+rem2H+":"+rem2M+" AM";
        }else{
            rem2D = "Rem 2 Set: "+rem2H+":"+rem2M+" PM";
        }


        if(rem3H < 12){
            rem3D = "Rem 3 Set: "+rem3H+":"+rem3M+" AM";
        }else{
            rem3D = "Rem 3 Set: "+rem3H+":"+rem3M+" PM";
        }


        if(rem4H < 12){
            rem4D = "Rem 4 Set: "+rem4H+":"+rem4M+" AM";
        }else{
            rem4D = "Rem 4 Set: "+rem4H+":"+rem4M+" PM";
        }



        ProRemSet.setText(rem1D+"\n\n"+rem2D+"\n\n"+rem3D+"\n\n"+rem4D);



    }


}
