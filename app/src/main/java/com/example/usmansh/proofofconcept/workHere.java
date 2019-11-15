package com.example.usmansh.proofofconcept;

import android.support.annotation.NonNull;

import androidx.work.Worker;

public class workHere extends Worker {
    @NonNull
    @Override
    public WorkerResult doWork() {


            //do work here


        return WorkerResult.SUCCESS;
    }
}
