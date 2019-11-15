package com.example.usmansh.proofofconcept.Adapter;

import android.content.Context;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.usmansh.proofofconcept.Fragments.MyFriendFragment;
import com.example.usmansh.proofofconcept.Fragments.MyProjectFragment;
;
import com.example.usmansh.proofofconcept.HomeActivity;

public class MyFragmentAdapter extends FragmentPagerAdapter {



    private Context context;

    public MyFragmentAdapter(FragmentManager fm, HomeActivity homeActivity) {
        super(fm);
        this.context = context;
    }



    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        if(position == 0)
            return MyProjectFragment.getInstance();

        else if(position == 1)
            return MyFriendFragment.getInstance();

        else
            return null;
    }



    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){

            case 0:
                return  "My Projects";

            case 1:
                return "My Friends";

        }

        return "";

    }
}
