package com.example.usmansh.proofofconcept.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usmansh.proofofconcept.Project;
import com.example.usmansh.proofofconcept.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RemListAdapter extends BaseAdapter {

    ArrayList<String> remList;

    Context contxt = null;
    String reminderTime = "";
    String date = "";

    public  RemListAdapter (Context c, ArrayList<String> remList, String date)

    {
        this.remList = remList;
        this.contxt =c;
        this.date = date;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup parent)

    {

        // Toast.makeText(contxt, "In Adaptor", Toast.LENGTH_SHORT).show();

        View V = new View (contxt);

        LayoutInflater inflat= (LayoutInflater) contxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (view==null){
            V = inflat.inflate(R.layout.remlistdesign,null);
        } else{
            V = view;
        }


        final TextView remTime = (TextView) V.findViewById(R.id.remLtimetv);
        final TextView remDate = (TextView)V.findViewById(R.id.remLdatetv);
        final ImageView watch  = (ImageView)V.findViewById(R.id.imageView6);
        watch.setBackgroundResource(R.drawable.alarmwatch);

        reminderTime = remList.get(i);

        remTime.setText(reminderTime);
        remDate.setText(date);

        return V;

    }




    @Override
    public int getCount() {
        return  remList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void clearAdapter(){
        remList.clear();
    }

}


