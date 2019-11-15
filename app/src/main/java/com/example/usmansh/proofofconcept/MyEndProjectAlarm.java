package com.example.usmansh.proofofconcept;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

class MyEndProjectAlarm extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {



        String ProName = intent.getStringExtra("ProName");
        String userId  = intent.getStringExtra("userId");
        Toast.makeText(context, "END Service: "+ProName, Toast.LENGTH_SHORT).show();

       /* Intent i = new Intent(context, AlertDialogAct.class);
        i.putExtra("ProName",ProName);
        i.putExtra("userId",userId);
        i.putExtra("remCode",5);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);*/

    }
}
