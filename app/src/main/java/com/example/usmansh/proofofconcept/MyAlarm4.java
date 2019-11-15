package com.example.usmansh.proofofconcept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

class MyAlarm4 extends BroadcastReceiver {


    int alarmCode;
    String ProName,userId;

    @Override
    public void onReceive(Context context, Intent intent) {


        try{


         alarmCode = intent.getIntExtra("key", 0);
         ProName = intent.getStringExtra("ProName");
         userId  = intent.getStringExtra("userId");

        if (alarmCode == 4) {

            Intent i = new Intent(context, AlertDialogAct.class);
            i.putExtra("userId",userId);
            i.putExtra("ProName",ProName);
            i.putExtra("remCode",alarmCode);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            //Toast.makeText(context, "Alarm code is : " + alarmCode, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Alarm Code is not recognized..!", Toast.LENGTH_SHORT).show();
        }


        }catch (Exception ex){
            Toast.makeText(context, "Mobile's OS is not able to Active Service..!", Toast.LENGTH_LONG).show();
        }
    }

}
