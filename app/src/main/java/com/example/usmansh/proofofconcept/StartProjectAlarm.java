package com.example.usmansh.proofofconcept;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

public class StartProjectAlarm extends BroadcastReceiver {


    String ProName,userId;
    long alarmTime;
    int currenthour;

    @Override
    public void onReceive(Context context, Intent intent) {





        try{

            Calendar calendar = Calendar.getInstance();
            currenthour = calendar.get(Calendar.HOUR_OF_DAY);

        ProName = intent.getStringExtra("ProjectName");
        userId  = intent.getStringExtra("userId");
        alarmTime = intent.getLongExtra("time",0);



            Intent i = new Intent(context, AlertDialogAct.class);
            i.putExtra("userId", intent.getStringExtra("userId"));
            i.putExtra("ProName", intent.getStringExtra("ProjectName"));
            i.putExtra("remCode", 0);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);




            //scheduleAlarm(context);
            // Toast.makeText(context, "CHour: "+currenthour, Toast.LENGTH_SHORT).show();

        /*if(currenthour > 9) {

            Intent i = new Intent(context, AlertDialogAct.class);
            i.putExtra("userId", intent.getStringExtra("userId"));
            i.putExtra("ProName", intent.getStringExtra("ProjectName"));
            i.putExtra("remCode", 0);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }else{
            Toast.makeText(context, "Its Sleepy Time..!", Toast.LENGTH_SHORT).show();
        }*/



    }catch (Exception ex){
        Toast.makeText(context, "Mobile's OS is not able to Active Service..!", Toast.LENGTH_LONG).show();
    }


    }


    public  void scheduleAlarm(Context context) {



        AlarmManager alarmManagerStart = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent startServiceIntent = new Intent(context,StartProjectAlarm.class);
        startServiceIntent.putExtra("ProjectName",ProName);
        startServiceIntent.putExtra("userId",userId);
        startServiceIntent.putExtra("time",alarmTime + 2 * 3600000);
        //2 hours delay to next alarm 3600000 ms in 1 hour
        PendingIntent startPendingIntent = PendingIntent.getBroadcast(context,0,startServiceIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        assert alarmManagerStart != null;

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManagerStart.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarmTime + 2 * 60000 ,startPendingIntent);
            Toast.makeText(context, "setExactAndAllowWhileIdle", Toast.LENGTH_SHORT).show();

        } else {
            alarmManagerStart.setExact(AlarmManager.RTC_WAKEUP, alarmTime + 2 * 60000,startPendingIntent);
            Toast.makeText(context, "setExact", Toast.LENGTH_SHORT).show();
        }
        //alarmManagerStart.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis,startPendingIntent);
        Toast.makeText(context, "Start Alam is Set Again..!", Toast.LENGTH_SHORT).show();






    }

}
