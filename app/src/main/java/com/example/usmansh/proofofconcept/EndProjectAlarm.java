package com.example.usmansh.proofofconcept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class EndProjectAlarm extends BroadcastReceiver {


    String ProName,userId;

    @Override
    public void onReceive(Context context, Intent intent) {


        try{
         ProName = intent.getStringExtra("ProjectName");
         userId  = intent.getStringExtra("userId");

       // Toast.makeText(context, "ST SERVICE : "+intent.getStringExtra("ProjectName"), Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, "ST SERVICE : "+intent.getStringExtra("userId"), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, AlertDialogAct.class);
        i.putExtra("ProName",intent.getStringExtra("ProjectName"));
        i.putExtra("userId",intent.getStringExtra("userId"));
        i.putExtra("remCode",5);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);


    }catch (Exception ex){
        Toast.makeText(context, "Mobile's OS is not able to Active Service..!", Toast.LENGTH_LONG).show();
    }
    }
}
