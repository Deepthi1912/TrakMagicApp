package com.example.usmansh.proofofconcept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

class MyStartProjectAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        String ProName = intent.getStringExtra("ProName");
        String userId  = intent.getStringExtra("userId");

        Toast.makeText(context, "Star Service : "+ProName, Toast.LENGTH_SHORT).show();

        Intent i = new Intent(context, AlertDialogAct.class);
        i.putExtra("userId",userId);
        i.putExtra("ProName",ProName);
        i.putExtra("remCode",0);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
}
