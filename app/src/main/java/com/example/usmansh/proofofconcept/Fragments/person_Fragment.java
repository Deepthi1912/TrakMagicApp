package com.example.usmansh.proofofconcept.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.usmansh.proofofconcept.R;

import java.util.List;

public class person_Fragment extends Fragment {


    ListView frndListV;
    EditText searchfrndEd;
    View view;



    public person_Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view  = inflater.inflate(R.layout.fragment_people_, container, false);
        frndListV = (ListView)view.findViewById(R.id.fragfrndListV);
        searchfrndEd = (EditText)view.findViewById(R.id.searchfrndEd);



        return view;
        //return inflater.inflate(R.layout.fragment_person_, container, false);
    }



}
