package com.example.usmansh.proofofconcept.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.usmansh.proofofconcept.Project;
import com.example.usmansh.proofofconcept.R;

import java.util.ArrayList;

public class ProListAdapter extends BaseAdapter {

    ArrayList<Project> proList;

    Context contxt = null;
    Project project = null;
    String sMonth="",eMonth="";

    public  ProListAdapter (Context c, ArrayList<Project> pro)

    {
        this.proList = pro;
        this.contxt =c;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup parent)

    {

        // Toast.makeText(contxt, "In Adaptor", Toast.LENGTH_SHORT).show();

        View V = new View (contxt);

        LayoutInflater inflat= (LayoutInflater) contxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (view==null){
            V = inflat.inflate(R.layout.prolistdesign,null);
        } else{
            V = view;
        }


        final TextView proLNametv = (TextView) V.findViewById(R.id.proLNametv);
        final TextView proLDatetv = (TextView)V.findViewById(R.id.proLDatetv);

        project = proList.get(i);

        proLNametv.setText(project.getProname());

        checkMonthNumber(project.getSmonth(),project.getEmonth());
        proLDatetv.setText(sMonth+" "+project.getSday()+" - "+eMonth+" "+project.getEday());

        return V;



    }



    private void checkMonthNumber(int smonth, int emonth) {

        //Checking for Starting Month
        if(smonth == 1){
            sMonth = "Jan";
        }else if(smonth == 2){
            sMonth = "Feb";
        }else if(smonth == 3){
            sMonth = "Mar";
        }else if(smonth == 4){
            sMonth = "Apr";
        }else if(smonth == 5){
            sMonth = "May";
        }else if(smonth == 6){
            sMonth = "June";
        }else if(smonth == 7){
            sMonth = "Jul";
        }else if(smonth == 8){
            sMonth = "Aug";
        }else if(smonth == 9){
            sMonth = "Sep";
        }else if(smonth == 10){
            sMonth = "Oct";
        }else if(smonth == 11){
            sMonth = "Nov";
        }else if(smonth == 12){
            sMonth = "Dec";
        }

        //Checking for Ending Month
        if(emonth == 1){
            eMonth = "Jan";
        }else if(emonth == 2){
            eMonth = "Feb";
        }else if(emonth == 3){
            eMonth = "Mar";
        }else if(emonth == 4){
            eMonth = "Apr";
        }else if(emonth == 5){
            eMonth = "May";
        }else if(emonth == 6){
            eMonth = "June";
        }else if(emonth == 7){
            eMonth = "Jul";
        }else if(emonth == 8){
            eMonth = "Aug";
        }else if(emonth == 9){
            eMonth = "Sep";
        }else if(emonth == 10){
            eMonth = "Oct";
        }else if(emonth == 11){
            eMonth = "Nov";
        }else if(emonth == 12){
            eMonth = "Dec";
        }


    }

    @Override
    public int getCount() {
        return  proList.size();
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
        proList.clear();
    }

}

