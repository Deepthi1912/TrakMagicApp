package com.example.usmansh.proofofconcept;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MyAlarm1 extends BroadcastReceiver {

   int alarmCode, arrIndex, remCode;
   String ProName,userId;
    ArrayList<Long> alarmTimeArr = new ArrayList<>();
   Handler handler;
    public  static MyAlarm1 myAlarm1;
    Runnable myRunnable;

    @Override
    public void onReceive(final Context context, Intent intent) {


    //Toast.makeText(context, "My Alarm 1", Toast.LENGTH_SHORT).show();
    alarmCode = intent.getIntExtra("key", 0);
    ProName = intent.getStringExtra("ProName");
    userId = intent.getStringExtra("userId");
    arrIndex = intent.getIntExtra("arrIndex",0);
    alarmTimeArr = (ArrayList<Long>) intent.getSerializableExtra("TimeList");

        remCode = alarmCode;

        arrIndex++;
        alarmCode++;

        if(arrIndex <= alarmTimeArr.size()-1) {

            scheduleAlarm(context);

        }else {

            arrIndex = 0;
            alarmCode = 1;
            updateTimeValueArr(context);
        }



        Intent i = new Intent(context, AlertDialogAct.class);
        i.putExtra("userId", userId);
        i.putExtra("ProName", ProName);
        i.putExtra("remCode", remCode);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);




}

    private void updateTimeValueArr(Context context) {

        for(int i = 0 ; i <alarmTimeArr.size() ; i++){

            //Adding 1 day delay on Time values in array
            long value = alarmTimeArr.get(i)+86400000;
            alarmTimeArr.set(i,value);
        }

        Toast.makeText(context, "Array Updated..!", Toast.LENGTH_SHORT).show();

        scheduleAlarmRepeat(context);
    }


    public  void scheduleAlarm(Context context) {

        //Toast.makeText(context, "NewIndex: "+arrIndex, Toast.LENGTH_SHORT).show();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context,MyAlarm1.class);
            intent.putExtra("key",alarmCode);
            intent.putExtra("userId",userId);
            intent.putExtra("ProName",ProName);
            intent.putExtra("TimeList",alarmTimeArr);
            intent.putExtra("arrIndex",arrIndex);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            if (Build.VERSION.SDK_INT >= 23) {
                assert alarmManager != null;
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeArr.get(arrIndex),pendingIntent);
                Toast.makeText(context, "setExactAndAllowWhileIdle", Toast.LENGTH_SHORT).show();

            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeArr.get(arrIndex),pendingIntent);
                Toast.makeText(context, "setExact", Toast.LENGTH_SHORT).show();

            }





        }

    public  void scheduleAlarmRepeat(Context context) {

        //Toast.makeText(context, "NewIndex: "+arrIndex, Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context,MyAlarm1.class);
        intent.putExtra("key",alarmCode);
        //Toast.makeText(context, "Reminder 1 is set AGAIN For NEXT DAY", Toast.LENGTH_SHORT).show();
        intent.putExtra("userId",userId);
        intent.putExtra("ProName",ProName);
        intent.putExtra("TimeList",alarmTimeArr);
        intent.putExtra("arrIndex",arrIndex);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);


       // Calendar calendar =  Calendar.getInstance();
        //calendar.add (Calendar.DATE,1);


        if (Build.VERSION.SDK_INT >= 23) {
            assert alarmManager != null;
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeArr.get(arrIndex),pendingIntent);
            Toast.makeText(context, "setExactAndAllowWhileIdle", Toast.LENGTH_SHORT).show();

        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeArr.get(arrIndex),pendingIntent);
            Toast.makeText(context, "setExact", Toast.LENGTH_SHORT).show();

        }





    }




}


