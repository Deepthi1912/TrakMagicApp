package com.example.usmansh.proofofconcept;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addInviteAct extends AppCompatActivity {


    String userIdd,numb,msg;
    EditText inviteNum,inviteMsg;
    Button inviteSendbt,inviteBackbt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invite);


        userIdd = getIntent().getStringExtra("userId");

        inviteNum    = (EditText)findViewById(R.id.inviteNum);
        inviteMsg    = (EditText)findViewById(R.id.inviteMesg);
        inviteSendbt = (Button)findViewById(R.id.invitesendbt);
        inviteBackbt = (Button)findViewById(R.id.inviteBackbt);


        inviteSendbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                numb = inviteNum.getText().toString().trim();
                msg  = inviteMsg.getText().toString().trim()+"\nDownload TrackMagic App:\n https://trakmagic.com/?page_id=5";

                if(TextUtils.isEmpty(numb)){
                    Toast.makeText(addInviteAct.this, "Enter Mobile Number..!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(msg)){
                    Toast.makeText(addInviteAct.this, "Enter Invite Message..!", Toast.LENGTH_SHORT).show();
                }else {

                    try {

                        SmsManager smsManager  = SmsManager.getDefault();
                        smsManager.sendTextMessage(numb,null,msg,null,null);
                        Toast.makeText(addInviteAct.this, "Message sent successfully..!", Toast.LENGTH_SHORT).show();
                        gotoMainAct();

                        AudioManager am =
                                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                        am.setStreamVolume(
                                AudioManager.STREAM_RING,
                                am.getStreamMaxVolume(AudioManager.STREAM_RING),
                                0);
                        
                    }catch (Exception ex){

                        Toast.makeText(addInviteAct.this, "Message Not Sent..!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


        inviteBackbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent goMainAct = new Intent(getApplicationContext(),MainActivity.class);
                goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goMainAct);
                finish();

            }
        });


    }

    private void sendSms(Context mContext) {


        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,new Intent(DELIVERED), 0);


// ---when the SMS has been sent---
        mContext.registerReceiver(
                new BroadcastReceiver()
                {
                    @Override
                    public void onReceive(Context arg0,Intent arg1)
                    {
                        switch(getResultCode())
                        {
                            case Activity.RESULT_OK:

                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                break;
                        }
                    }
                }, new IntentFilter(SENT));
        // ---when the SMS has been delivered---
        mContext.registerReceiver(
                new BroadcastReceiver()
                {

                    @Override
                    public void onReceive(Context arg0, Intent arg1)
                    {
                        switch(getResultCode())
                        {
                            case Activity.RESULT_OK:
                                break;
                            case Activity.RESULT_CANCELED:
                                break;
                        }
                    }
                }, new IntentFilter(DELIVERED));


        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(numb, null,msg,sentPI, deliveredPI);



    }

    public  void gotoMainAct() {


        Intent goMainAct = new Intent(getApplicationContext(),MainActivity.class);
        goMainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goMainAct);
        finish();
    }

}
