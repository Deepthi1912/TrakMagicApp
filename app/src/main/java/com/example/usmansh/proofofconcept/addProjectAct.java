package com.example.usmansh.proofofconcept;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.usmansh.proofofconcept.Adapter.RemListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.work.OneTimeWorkRequest;

public class addProjectAct extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    Button startTimebt, endTimebt, tickProjectbt,crossprojectBt,noseeBt;
    FloatingActionButton  setReminder;
    TextView ShowProDetails, ShowRemDetails;
    EditText proTitleEd;
    Calendar c;
    int projectTimeChoice = 0, startTimeSelected, endTimeSelected;
    int day, month, year, hour, minut;
    int dayFinal, monthFinal, yearFinal, hourFinal, minutFinal;
    String projectTitle = null;
    Switch daySwitch;
    DatabaseReference proDetails,activeUser,myProject;
    Project project;
    boolean ProStartData = false, ProEndData = false, ProName = false;
    int isReminderset = 0;
    int rem1H = 00, rem1M = 00, rem2H = 00, rem2M = 00, rem3H = 00, rem3M = 00, rem4H = 00, rem4M = 00;
    String projectname,userId,projectActive;
    ProReminder proReminder;
    int startProHour = 0, endProHour = 0, startProDate = 0, endProDate = 0;
    int startProYear = 0, endProYear = 0, startProMonth = 0, endProMonth = 0;
    int startProMinute2,endProMinute2,startProHour2,endProHour2;
    String ProjectName="";
    ArrayList<Long> alarmTimeArr = new ArrayList<>();
    ArrayList<String> remTimeArr = new ArrayList<>();
    ArrayList<String> FrndNameList = new ArrayList<>();
    ArrayList<User> UserList = new ArrayList<>();
    ArrayList<String> MobNumbList = new ArrayList<>();
    boolean buttonchecked = false;
    RemListAdapter remListAdapter ;
    String date = "";
    ListView remListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);


        project = new Project();
        userId = getIntent().getStringExtra("userId");

        noseeBt         = (Button)findViewById(R.id.noseeBt);
        startTimebt     = (Button) findViewById(R.id.picStrTimebt);
        endTimebt       = (Button) findViewById(R.id.picEndTimebt);
        crossprojectBt  = (Button)findViewById(R.id.crossProject);
        tickProjectbt   = (Button) findViewById(R.id.tickProject);
        setReminder     = (FloatingActionButton) findViewById(R.id.setreminderbt);
        proTitleEd      = (EditText) findViewById(R.id.inputProjectTile);
        daySwitch       = (Switch) findViewById(R.id.daySwitch);
        ShowProDetails  = (TextView) findViewById(R.id.ShowProDetails);
        ShowRemDetails  = (TextView) findViewById(R.id.ShowRemDetails);
        remListView     = (ListView)findViewById(R.id.remListView);

        ShowProDetails.setVisibility(View.INVISIBLE);
        ShowRemDetails.setVisibility(View.INVISIBLE);



        noseeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!buttonchecked) {
                    proTitleEd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    buttonchecked = true;
                }else{
                    proTitleEd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    buttonchecked =false;
                }

            }
        });

        activeUser = FirebaseDatabase.getInstance().getReference("ActiveUsers");
        myProject  = FirebaseDatabase.getInstance().getReference("MyProjects");

        checkProjectActive();


            startTimebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectTitle = proTitleEd.getText().toString();
                if (!TextUtils.isEmpty(projectTitle)) {
                    //name entered
                    ProName = true;

                    //Nested if Else
                    if (!daySwitch.isChecked()) {

                        Calendar calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day = calendar.get(Calendar.DAY_OF_MONTH);

                        //Starting project time
                        projectTimeChoice = 1;
                        DatePickerDialog datePickerDialog = new DatePickerDialog(addProjectAct.this, addProjectAct.this, year, month, day+1);
                        datePickerDialog.show();

                    } else {

                        c = Calendar.getInstance();
                        hour = c.get(Calendar.HOUR);
                        minut = c.get(Calendar.MINUTE);
                        //Starting project time
                        projectTimeChoice = 1;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(addProjectAct.this, addProjectAct.this
                                , hour, minut, android.text.format.DateFormat.is24HourFormat(getApplicationContext()));
                        timePickerDialog.show();
                    }
                } else {
                    Toast.makeText(addProjectAct.this, "Enter Project Tile..!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        endTimebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                projectTitle = proTitleEd.getText().toString();

                if (!TextUtils.isEmpty(projectTitle) && startTimeSelected == 1) {
                    //Nested if Else
                    if (!daySwitch.isChecked()) {

                        Calendar calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day = calendar.get(Calendar.DAY_OF_MONTH);

                        //Ending project time
                        projectTimeChoice = 2;
                        DatePickerDialog datePickerDialog = new DatePickerDialog(addProjectAct.this, addProjectAct.this, year, month, day+1);
                        datePickerDialog.show();
                    } else {

                        c = Calendar.getInstance();
                        hour = c.get(Calendar.HOUR);
                        minut = c.get(Calendar.MINUTE);

                        //Ending project
                        projectTimeChoice = 2;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(addProjectAct.this, addProjectAct.this
                                , hour, minut, android.text.format.DateFormat.is24HourFormat(getApplicationContext()));
                        timePickerDialog.show();
                    }


                } else if (TextUtils.isEmpty(projectTitle)) {
                    Toast.makeText(addProjectAct.this, "Enter Project Title..!", Toast.LENGTH_SHORT).show();
                } else if (startTimeSelected != 1) {
                    Toast.makeText(addProjectAct.this, "Select Start Time First..!", Toast.LENGTH_SHORT).show();
                }

            }
        });


       setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (startTimeSelected == 1 && endTimeSelected == 1) {

                    Intent gotoSetReminder = new Intent(getApplicationContext(), setReminderAct.class);
                    gotoSetReminder.putExtra("ProName",projectTitle);
                    gotoSetReminder.putExtra("userId",userId);

                    gotoSetReminder.putExtra("sHour",startProHour2);
                    gotoSetReminder.putExtra("sMinute",startProMinute2);

                    gotoSetReminder.putExtra("eHour",endProHour2);
                    gotoSetReminder.putExtra("eMinute",endProMinute2);

                    gotoSetReminder.putExtra("startProDate",startProDate);

                    if(!daySwitch.isChecked()) {
                        gotoSetReminder.putExtra("dayChoice", "MD");
                    }
                    startActivityForResult(gotoSetReminder, 1);



                } else if (TextUtils.isEmpty(projectTitle)) {
                    Toast.makeText(addProjectAct.this, "Enter Project Title..!", Toast.LENGTH_SHORT).show();
                } else if (startTimeSelected != 1) {
                    Toast.makeText(addProjectAct.this, "Select Start Time First..!", Toast.LENGTH_SHORT).show();
                } else if (endTimeSelected != 1) {
                    Toast.makeText(addProjectAct.this, "Select End Time First..!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        tickProjectbt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(addProjectAct.this);
                builder

                        .setCancelable(false)
                        .setMessage("Drop down the Menu bar and Enable your GPS for Location Recording..!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {

                                //send notification msg received
                                sendProjectData();
                                dialog.cancel();

                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();



            }
        });



        crossprojectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void checkProjectActive() {



        activeUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                projectActive = dataSnapshot.child(userId).child("isActive").getValue(String.class);
                Toast.makeText(addProjectAct.this, "Project Active: "+projectActive, Toast.LENGTH_SHORT).show();

                 if(projectActive != null){

                     if(projectActive.equalsIgnoreCase("yes")) {

                         AlertDialog.Builder builder = new AlertDialog.Builder(addProjectAct.this);
                         builder

                                 .setCancelable(false)
                                 .setMessage("You already have a project in progress..!")
                                 .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int id) {

                                         finish();
                                     }
                                 });
                         AlertDialog alert = builder.create();
                         alert.show();

                     }

                    }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendProjectData() {


        if (ProName && ProStartData && ProEndData) {

            if (isReminderset == 1) {

                projectname = proTitleEd.getText().toString();
                ShowProDetailsOnScreen(projectname);

                project.setPromanager(userId);
                project.setIsactive("ON");
                project.setProname(projectname);


                DatabaseReference sendMyPro = myProject.child(userId).child(proTitleEd.getText().toString());

                sendMyPro.setValue(project).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            //Getting All Friends and Users List
                            getAllFrnds();

                            Toast.makeText(addProjectAct.this, "Project Set on My Project List..!", Toast.LENGTH_SHORT).show();
                            activeUser.child(userId).child("isActive").setValue("yes");
                            Intent goMainAct = new Intent(getApplicationContext(),HomeActivity.class);
                            goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(goMainAct);
                            finish();

                        }else{
                            Toast.makeText(addProjectAct.this, "Error: Not set in My Project List", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } else {
                Toast.makeText(this, "Set Reminders..!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Complete Project's Details..!", Toast.LENGTH_SHORT).show();
        }


    }




    private void sendMessageToFriend() {


        //Toast.makeText(this, "UserList: "+UserList.size(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Frnd List: "+FrndNameList.size(), Toast.LENGTH_SHORT).show();

        try {

            if (FrndNameList.size() != 0) {
                //Storing Friends Mobile Numbers
                for (int i = 0; i < FrndNameList.size(); i++) {
                    String frnd_id = FrndNameList.get(i);
                    for (int j = 0; j < UserList.size(); j++) {
                        User user = UserList.get(j);
                        String email = user.getUsername();
                        String[] parts = email.split("@");
                        String user_id = parts[0];
                         if (user_id.equalsIgnoreCase(frnd_id)) {
                            MobNumbList.add(user.getPhno());
                        }

                    }
                }

                Toast.makeText(this, "Mob No Size: " + MobNumbList.size(), Toast.LENGTH_SHORT).show();


                sendMsgToNumbers();

            } else {
                Toast.makeText(this, "Your Friend List is empty..!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            Toast.makeText(this, "Your Friend List is empty for now..!", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendMsgToNumbers() {


        String numb,msg;

        for(int i=0; i<MobNumbList.size();i++) {
            numb = MobNumbList.get(i);
            msg = "Your Friend " + "" + userId + " has started a New Project " + projectname + " follow him by:\nDownload TrackMagic App:\n https://trakmagic.com/?page_id=5";

            try
            {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(numb, null, msg, null, null);
            } catch (Exception ex) {

                Toast.makeText(addProjectAct.this, "Message Not Sent..!", Toast.LENGTH_SHORT).show();
            }

        }

        Toast.makeText(addProjectAct.this, "Message Sent Successfully..!", Toast.LENGTH_SHORT).show();



    }


    private void getAllFrnds() {

        DatabaseReference  getFrndListREF = FirebaseDatabase.getInstance().getReference("MyFriend").child(userId);
        getFrndListREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postData : dataSnapshot.getChildren()) {

                    if (postData != null) {

                        FrndNameList.add(postData.getValue(String.class));
                        //Toast.makeText(addProjectAct.this, "FrndName size: "+FrndNameList.size(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(addProjectAct.this, "Data is Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getAllUsers();


    }

    private void getAllUsers() {


        DatabaseReference getUsersListREF = FirebaseDatabase.getInstance().getReference("RegUsers");
        getUsersListREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot postData:dataSnapshot.getChildren()){

                    User user = postData.getValue(User.class);

                    if(user != null) {

                        UserList.add(user);

                    }else{
                        Toast.makeText(addProjectAct.this, "List Empty..!", Toast.LENGTH_SHORT).show();
                    }
                }

                Toast.makeText(addProjectAct.this, "Calling Sending SMS", Toast.LENGTH_SHORT).show();
                sendMessageToFriend();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void sendToMyProject() {


        DatabaseReference sendMyPro = myProject.child(userId).child(proTitleEd.getText().toString());

        sendMyPro.setValue(project).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(addProjectAct.this, "Project Set on My Project List..!", Toast.LENGTH_SHORT).show();
                    Intent goMainAct = new Intent(getApplicationContext(),HomeActivity.class);
                    goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goMainAct);
                    finish();

                }else{
                    Toast.makeText(addProjectAct.this, "Error: Not set in My Project List", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        yearFinal = year;
        monthFinal = month;
        dayFinal = dayOfMonth;


        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minut = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(addProjectAct.this, addProjectAct.this
                , hour, minut, android.text.format.DateFormat.is24HourFormat(this));

        timePickerDialog.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        hourFinal = hourOfDay;
        minutFinal = minute;

        Calendar cal = Calendar.getInstance();
        //day Switch button defines the Timing Select option.. Is it day or not ?
        if (!daySwitch.isChecked()) {

            cal.set(yearFinal, monthFinal, dayFinal, hourFinal, minutFinal, 0);
            //Toast.makeText(this, "Not Checked time selected..!", Toast.LENGTH_SHORT).show();
            //Setting Start date+hour and END date+hour for time comparision
            if (projectTimeChoice == 1) {
                startProYear = yearFinal;
                startProMonth = monthFinal;
                startProHour = hourFinal;
                startProDate = dayFinal;

            } else if (projectTimeChoice == 2) {
                endProYear = yearFinal;
                endProMonth = monthFinal;
                endProHour = hourFinal;
                endProDate = dayFinal;
            }


            //Setting Project's Object Timing Details
            setProjectDetailsUnCh(projectTimeChoice);

        } else {
            //Toast.makeText(this, "Check Time Selected..!", Toast.LENGTH_SHORT).show();
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hourFinal, minutFinal, 0);
            yearFinal  = cal.get(Calendar.YEAR);
            monthFinal = cal.get(Calendar.MONTH);
            dayFinal   = cal.get(Calendar.DAY_OF_MONTH);

            if (projectTimeChoice == 1) {
                startProYear = cal.get(Calendar.YEAR);
                startProMonth = cal.get(Calendar.MONTH);
                startProDate = cal.get(Calendar.DAY_OF_MONTH);
                startProHour = hourFinal;

            } else if (projectTimeChoice == 2) {
                endProYear = cal.get(Calendar.YEAR);
                endProMonth = cal.get(Calendar.MONTH);
                endProDate = cal.get(Calendar.DAY_OF_MONTH);
                endProHour = hourFinal;
             }


            // /Setting Project's Object Timing Details
            setProjectDetailsCh(projectTimeChoice);
        }


        //Project Time Choice is a Variable which tells the StartTiming is Setting or EndTiming is setting of Project
        //Start Time Selected = 1 means Project's Start time has selected same case with EndTimeSelected 1 means selected 0 not selected

        if (projectTimeChoice == 1) {

            StartProjectAlarmFunc(cal.getTimeInMillis());
            //startAlarmService(cal.getTimeInMillis());
            startTimeSelected = 1;
            //startTimebt.setText("Time Set: " + hourFinal + ":" + minutFinal);
            startTimebt.setText(dayFinal+" - "+(monthFinal+1)+" - "+yearFinal);
            startProMinute2 = minutFinal;
            startProHour2 = hourFinal;

        } else if (projectTimeChoice == 2) {


            //Checking SD or MD project
            if(!daySwitch.isChecked()){

                if (endProYear >= startProYear && endProMonth >= startProMonth &&
                        endProDate >= startProDate) {

                   EndProjectAlamFunc(cal.getTimeInMillis());
                    //endAlarmService(cal.getTimeInMillis());
                    endTimeSelected = 1;
                    //endTimebt.setText("Time Set: " + hourFinal + ":" + minutFinal);
                    endTimebt.setText(dayFinal+" - "+(monthFinal+1)+" - "+yearFinal);
                    endProHour2 = hourFinal;
                    endProMinute2 = minutFinal;

                } else {
                    Toast.makeText(this, "Error: You are selecting previous dates..!", Toast.LENGTH_SHORT).show();
                }

            }else {


                if (endProYear >= startProYear && endProMonth >= startProMonth &&
                        endProDate >= startProDate && endProHour >= startProHour) {

                    EndProjectAlamFunc(cal.getTimeInMillis());
                    //endAlarmService(cal.getTimeInMillis());
                    endTimeSelected = 1;
                    //endTimebt.setText("Time Set: " + hourFinal + ":" + minutFinal);
                    endTimebt.setText(dayFinal+" - "+(monthFinal+1)+" - "+yearFinal);
                    endProHour2 = hourFinal;
                    endProMinute2 = minutFinal;

                } else {
                    Toast.makeText(this, "Error: You are selecting previous dates..!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }



    private void setProjectDetailsCh(int projectTimeChoice) {

        Calendar cal = Calendar.getInstance();;

        if(projectTimeChoice == 1){

            ProStartData = true;
            project.setSyear(cal.get(Calendar.YEAR));
            project.setSmonth(cal.get(Calendar.MONTH)+1);
            project.setSday(cal.get(Calendar.DAY_OF_MONTH));
            project.setShour(hourFinal);
            project.setSminute(minutFinal);
        }
        else{

            ProEndData = true;
            project.setEyear(cal.get(Calendar.YEAR));
            project.setEmonth(cal.get(Calendar.MONTH)+1);
            project.setEday(cal.get(Calendar.DAY_OF_MONTH));
            project.setEhour(hourFinal);
            project.setEminute(minutFinal);

        }

    }

    private void setProjectDetailsUnCh(int projectTimeChoice) {


        if(projectTimeChoice == 1){
            ProStartData = true;
            project.setSyear(yearFinal);
            project.setSmonth(monthFinal+1);
            project.setSday(dayFinal);
            project.setShour(hourFinal);
            project.setSminute(minutFinal);
        }
        else{
            ProEndData = true;
            project.setEyear(yearFinal);
            project.setEmonth(monthFinal+1);
            project.setEday(dayFinal);
            project.setEhour(hourFinal);
            project.setEminute(minutFinal);

        }



    }


    public void StartProjectAlarmFunc(long timeInMillis){



        AlarmManager alarmManagerStart = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent startServiceIntent = new Intent(getApplicationContext(),StartProjectAlarm.class);
        startServiceIntent.putExtra("ProjectName",projectTitle);
        startServiceIntent.putExtra("userId",userId);
        startServiceIntent.putExtra("time",timeInMillis);

        PendingIntent startPendingIntent = PendingIntent.getBroadcast(this,0,startServiceIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        assert alarmManagerStart != null;

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManagerStart.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis,startPendingIntent);
            Toast.makeText(this, "setExactAndAllowWhileIdle", Toast.LENGTH_SHORT).show();

        } else {
            alarmManagerStart.setExact(AlarmManager.RTC_WAKEUP, timeInMillis,startPendingIntent);
            Toast.makeText(this, "setExact", Toast.LENGTH_SHORT).show();
        }
        //alarmManagerStart.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis,startPendingIntent);
        Toast.makeText(this, "Start Alam is Set..!", Toast.LENGTH_SHORT).show();

    }

    private void EndProjectAlamFunc(long timeInMillis) {

        AlarmManager alarmManagerEnd = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent endAlarmIntent = new Intent(getApplicationContext(),EndProjectAlarm.class);
        endAlarmIntent.putExtra("ProjectName",projectTitle);
        endAlarmIntent.putExtra("userId",userId);

        PendingIntent endPendingIntent = PendingIntent.getBroadcast(this,5,endAlarmIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        assert alarmManagerEnd != null;

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManagerEnd.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis,endPendingIntent);
            Toast.makeText(this, "setExactAndAllowWhileIdle", Toast.LENGTH_SHORT).show();
        } else {
            alarmManagerEnd.setExact(AlarmManager.RTC_WAKEUP, timeInMillis,endPendingIntent);
            Toast.makeText(this, "setEzact", Toast.LENGTH_SHORT).show();
        }

        //alarmManagerEnd.setRepeating(AlarmManager.RTC_WAKEUP,timeInMillis,AlarmManager.INTERVAL_DAY,endPEndingIntent);
        Toast.makeText(this, "End Alarm is set..!", Toast.LENGTH_SHORT).show();



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            Toast.makeText(this, "Reminders Set Successfully..!", Toast.LENGTH_SHORT).show();

            alarmTimeArr = (ArrayList<Long>) data.getSerializableExtra("TimeList");
            //Toast.makeText(this, "Time List Size: "+alarmTimeArr.size(), Toast.LENGTH_SHORT).show();
            isReminderset = data.getIntExtra("isReminderSet", 0);
                rem1H = data.getIntExtra("rem1H",0);
                rem1M = data.getIntExtra("rem1M",0);
                rem2H = data.getIntExtra("rem2H",0);
                rem2M = data.getIntExtra("rem2M",0);
                rem3H = data.getIntExtra("rem3H",0);
                rem3M = data.getIntExtra("rem3M",0);
                rem4H = data.getIntExtra("rem4H",0);
                rem4M = data.getIntExtra("rem4M",0);


                setReminderAdapter();

                //Setting Remnder's Object Values
                proReminder = new ProReminder();
                proReminder.setRem1hour(rem1H);
                proReminder.setRem1minute(rem1M);
                proReminder.setRem2hour(rem2H);
                proReminder.setRem2minute(rem2M);
                proReminder.setRem3hour(rem3H);
                proReminder.setRem3minute(rem3M);
                proReminder.setRem4hour(rem4H);
                proReminder.setRem4minute(rem4M);
                //Setting Reminder's object into PROJECT object
                project.setProreminder(proReminder);

                projectname = proTitleEd.getText().toString();
                ShowProDetailsOnScreen(projectname);
                 ShowRemDetailsOnScreen();


        }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Reminders is not Set successfully..!", Toast.LENGTH_SHORT).show();

            }
        }




    private void setReminderAdapter() {

        String concatRem1;
        String concatRem2;
        String concatRem3;
        String concatRem4;



        if(rem1H < 12){
            concatRem1 = rem1H+":"+rem1M+" AM";
        }else{
            concatRem1 = rem1H+":"+rem1M+" PM";
        }


        if(rem2H < 12){
            concatRem2 = rem2H+":"+rem2M+" AM";
        }else{
            concatRem2 = rem2H+":"+rem2M+" PM";
        }


        if(rem3H < 12){
            concatRem3 = rem3H+":"+rem3M+" AM";
        }else{
            concatRem3 = rem3H+":"+rem3M+" PM";
        }


        if(rem4H < 12){
            concatRem4 = rem4H+":"+rem4M+" AM";
        }else{
            concatRem4 = rem4H+":"+rem4M+" PM";
        }





        remTimeArr.add(concatRem1);
        remTimeArr.add(concatRem2);
        remTimeArr.add(concatRem3);
        remTimeArr.add(concatRem4);

        date = dayFinal+"-"+(monthFinal+1)+"-"+yearFinal;

        //remListAdapter.clearAdapter();
        remListAdapter = new RemListAdapter(getApplicationContext(),remTimeArr,date);
        remListView.setAdapter(remListAdapter);



    }


    @SuppressLint("SetTextI18n")
    private void ShowProDetailsOnScreen(String projectname) {

        String endDetails,startDetails;
        if(project.getShour()<12){
            startDetails = "Start Time: "+project.getShour()+" : "+project.getSminute()+" AM\nStart Date: "+project.getSday()+"/"+project.getSmonth()+"/"+project.getSyear()+"\n\n";

        }else {

            startDetails = "Start Time: "+project.getShour()+" : "+project.getSminute()+" PM\nStart Date: "+project.getSday()+"/"+project.getSmonth()+"/"+project.getSyear()+"\n\n";

        }

        if(project.getEhour()<12){
            endDetails   = "End Time: "+project.getEhour()+" : "+project.getEminute()+" AM\nEnd Date: "+project.getEday()+"/"+project.getEmonth()+"/"+project.getEyear();

        }else{
            endDetails   = "End Time: "+project.getEhour()+" : "+project.getEminute()+" PM\nEnd Date: "+project.getEday()+"/"+project.getEmonth()+"/"+project.getEyear();

        }


        //String startDetails = "Start: "+project.getShour()+" : "+project.getSminute()+"     "+project.getSday()+"/"+project.getSmonth()+"/"+project.getSyear()+"\n\n";
        //String endDetails   = "End: "+project.getEhour()+" : "+project.getEminute()+"     "+project.getEday()+"/"+project.getEmonth()+"/"+project.getEyear();




        ShowProDetails.setText("Project Title: "+projectname+"\n\n"+startDetails+endDetails);

        //ShowRemDetailsOnScreen();
    }

    @SuppressLint("SetTextI18n")
    private void ShowRemDetailsOnScreen() {

        String rem1,rem2,rem3,rem4;

        if(rem1H < 12){
            rem1 = "Rem 1 Set: "+rem1H+":"+rem1M+" AM";
        }else{
            rem1 = "Rem 1 Set: "+rem1H+":"+rem1M+" PM";
        }


        if(rem2H < 12){
            rem2 = "Rem 2 Set: "+rem2H+":"+rem2M+" AM";
        }else{
            rem2 = "Rem 2 Set: "+rem2H+":"+rem2M+" PM";
        }


        if(rem3H < 12){
            rem3 = "Rem 3 Set: "+rem3H+":"+rem3M+" AM";
        }else{
            rem3 = "Rem 3 Set: "+rem3H+":"+rem3M+" PM";
        }


        if(rem4H < 12){
            rem4 = "Rem 4 Set: "+rem4H+":"+rem4M+" AM";
        }else{
            rem4 = "Rem 4 Set: "+rem4H+":"+rem4M+" PM";
        }

        ShowRemDetails.setText(rem1+"\n\n"+rem2+"\n\n"+rem3+"\n\n"+rem4);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJob(long timeInMillis) {
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);

        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("ProjectName",projectTitle);
        bundle.putString("userId",userId);


        JobInfo info = new JobInfo.Builder(123, componentName)
                .setExtras(bundle)
                .setPersisted(true)
                .setPeriodic(timeInMillis)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        assert scheduler != null;
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("addProjectAct", "Job scheduled");
            Toast.makeText(this, "Start Alarm set", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("addProjectAct", "Job scheduling failed");
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d("addProjectAct", "Job cancelled");
    }







}


