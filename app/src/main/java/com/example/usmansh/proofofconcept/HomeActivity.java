package com.example.usmansh.proofofconcept;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.usmansh.proofofconcept.Adapter.MyFragmentAdapter;
import com.example.usmansh.proofofconcept.Adapter.ProListAdapter;
import com.example.usmansh.proofofconcept.Fragments.people_Fragment;
import com.example.usmansh.proofofconcept.Fragments.person_Fragment;
import com.google.android.gms.vision.Frame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ViewPager viewPager;
    TabLayout tabLayout;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLO;
    MyFragmentAdapter adapter;
    FirebaseAuth mAuth;
    String user_id,proStatus;
    String introdunction_text, work_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TrakMagic");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        adapter = new MyFragmentAdapter(getSupportFragmentManager(),HomeActivity.this);
        viewPager.setAdapter(adapter);

        frameLO = (FrameLayout)findViewById(R.id.frameLO);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        String[] parts = email.split("@");
        user_id = parts[0];

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        Intent showDoc = new Intent(getApplicationContext(),TextResAct.class);


        //noinspection SimplifiableIfStatement
        if (id == R.id.item1) {
            String introText = getIntrodunction_text();
            showDoc.putExtra("text",introText);
            startActivity(showDoc);
            return true;
        }


        else if (id == R.id.item2) {
            String workingText = getWork_text();
            showDoc.putExtra("text",workingText);
            startActivity(showDoc);
            return true;
        }

        else if(id == R.id.item3){
            Intent goMain = new Intent(getApplicationContext(), LoginAct.class);
            goMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goMain);
            finish();
            mAuth.signOut();

        }

        else if(id == R.id.item4){

            Intent goMain = new Intent(getApplicationContext(), PubGuardAct.class);
            goMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goMain);
            finish();
        }

        else if(id == R.id.item5){

            Intent goProfileAct = new Intent(getApplicationContext(), ProfileSetAct.class);
            goProfileAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goProfileAct);
            finish();

        }



        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public String getIntrodunction_text() {

        introdunction_text = "Introduction\n" +
                "\n" +
                "TrakMagic has many uses and can be used for many situations by all kinds of people.\n" +
                "\n" +
                "A simple example might be if you wish to undertake a journey on your own. The sensible approach would be to let someone know when you are leaving, when you get there, and when you get back.\n" +
                "\n" +
                "TrakMagic does this easily for you with no need to contact one specific person, you can share the journey plan and details with one or more persons and they can simply monitor your project.\n" +
                "\n" +
                "As you travel through the journey, you get reminers from the app at pre-determined times, each reminder will log your location automatically, and give you an option to leave a message or take an image.\n" +
                "\n" +
                "Your ‘Friends’ have access to this information and they can see you are making progress and are safe.\n" +
                "\n" +
                "In an emergency situation, the records can be accessed and your rout can be traced using the information you have provided.\n" +
                "\n" +
                "In a normal situation you sinply capture the GPS location, comments and images that can be later used as a reference of your projects events.\n";


        return introdunction_text;
    }


    public String getWork_text() {

        work_text = "\n" +
                "Instructions:\n" +
                "\n" +
                "1.\tBefore you can use TrakMagic you need to create an account.\n" +
                "   ->\tClick the ‘Create An Account’ button and enter your Phone Number, a uername and a password.\n" +
                "\n" +
                "2.\tLog into TrakMagic\n" +
                "\n" +
                "   ->\tClick the ‘Login’ Button and enter your Username and Password  .\n" +
                "\n" +
                "3.\tAll Users Button\n" +
                "\n" +
                "   ->\tThis will list all the users on the network\n" +
                "   ->\tAdd user as a friend\n" +
                "   ->\tChoose a friend from the list, and click on the name.\n" +
                "   ->\tA window will pop open to add the user.\n" +
                "\n" +
                "4.\tTo Remove a user as a friend.\n" +
                "   ->\tOpen the ‘My Friends’ list and click/hold down the name of the friend.\n" +
                "\n" +
                "5.\tCreating a ‘Project’ (A Project is your journey, trip or adventure.)\n" +
                "\n" +
                "   ->\tEach project requires several settings\n" +
                "   ->\tProject Name\n" +
                "   ->\tThis is a name for the project\n" +
                "   ->\tIs this a Day Project?\n" +
                "   ->\tDefault is a project up to 24 hours.(Red)  \n" +
                "   ->\t(For Multi Day projects, slide the button left. (White), instructions below)\n" +
                "   ->\tSet the Start Time\n" +
                "   ->\tSelect Hour, Minutes am/pm\n" +
                "   ->\tSet the End Time\n" +
                "   ->\tSelect Hour, Minutes am/pm\n" +
                "\n" +
                "*-*.\tSet the reminders\n" +
                "   ->\tSelect the time for the first reminders\n" +
                "   ->\tPress the reminder 1 button to set the time.\n" +
                "   ->\tSelect the time for the second reminders\n" +
                "   ->\tPress the reminder 2 button to set the time\n" +
                "   ->\tSelect the time for the third reminders\n" +
                "   ->\tPress the reminder 3 button to set the time\n" +
                "   ->\tSelect the time for the fourth reminders\n" +
                "   ->\tPress the reminder 4 button to set the time\n" +
                "***\tNOTE: You can set any number of reminders from 1 to 4.\n" +
                "\n" +
                "*-*.\tIs this a Day Project? - No! (Multi-Day Project)\n" +
                "   ->\tSlide the button left for multi day(White)\n" +
                "   ->\tClick the Start Button.\n" +
                "   ->\tSelect the Day the project will start. > ok.\n" +
                "   ->\tSelect the time of the day to start. > ok.\n" +
                "   ->\tYou will return to the main panel\n" +
                "\n" +
                "*-*\tClick the End Button.\n" +
                "   ->\tSelect the Day the project will end. > ok.\n" +
                "   ->\tSelect the time of the day to end. > ok.\n" +
                "   ->\tYou will return to the main panel\n" +
                "\n" +
                "*-*tSet Reminders\n" +
                "   ->\tFollow the same procedure as above.\n" +
                "\n" +
                "*-*\tAdd Project\n" +
                "   ->\tClick the Add Project Button.\n" +
                "\n" +
                "6.\tView your projects\n" +
                "\n" +
                "   ->\tClick the ‘My Projects List’ button\n" +
                "   ->\tCurrent Project will be shown in red text at the top.\n" +
                "   ->\tClick the current Project in Project History list.\n" +
                "\n" +
                "*-*\tNotes\n" +
                "   ->\tThis will show you all the notes created during this projects\n" +
                "   ->\tClick note to open\n" +
                "\n" +
                "*-*\tImages\n" +
                "   ->\tThis will show all the images taken\n" +
                "   ->\tClick link to view image\n" +
                "\n" +
                "*-*\tGPS Location\n" +
                "   ->\tThis will list the GPS locations\n" +
                "   ->\tClick to view location on Google Maps.\n" +
                "\n" +
                "*-*\tSettings\n" +
                "   ->\tActive Project Buttons\n" +
                "   ->\tClose Projects\n" +
                "   ->\tThis will Close the projects\n" +
                "\n" +
                "*-*\tSet reminders\n" +
                "   ->\tThis will let you reset the reminders\n" +
                "\n" +
                "*-*\tBack\n" +
                "   ->\tReturn to the previous panel with no changes.\n" +
                "\n" +
                "*-*\tAdd Response\n" +
                "   ->\tThis will manually add an aditional reminder.\n" +
                "   ->\tGPS will be auto logged\n" +
                "   ->\tAdd text or skip\n" +
                "   ->\tAdd image or skip.\n" +
                "\n" +
                "7.\tInvite Friend\n" +
                "\n" +
                "   ->\tYou can send an invite to someone to be your friend.\n" +
                "   ->\tEnter the persons phone number\n" +
                "\n" +
                "   ->\tEnter a text message\n" +
                "\n" +
                "   ->\tClick ‘Send’\n" +
                "   ->\tYour friend will get your text message and a link to download the TrakMagic app.\n" +
                "   ->\tYour friend will show up in the ‘All Users’ list, click to add to ‘My Friends’.\n" +
                "\n" +
                "8.\tView a friends project \n" +
                "\n" +
                "   ->\tClick the ‘My Friends’ button\n" +
                "   ->\tYou will see a list of all your friends.\n" +
                "   ->\tClick on a Friend to see their project.\n" +
                "\n" +
                "*-*\tIf a project is current, it will be shown in red text at the top.\n" +
                "   ->\tClick the current Project in Project History list.\n" +
                "   ->\tYou can also click to see a Closed project.\n" +
                "\n" +
                "*-*\tWhen a project is open, you will see some buttons.\n" +
                "\n" +
                " *->\tNotes\n" +
                "   ->\tThis will show you all the notes created during this project\n" +
                "   ->\tClick note to open\n" +
                "\n" +
                " *->\tImages\n" +
                "   ->\tThis will show all the images taken\n" +
                "   ->\tClick link to view image\n" +
                "\n" +
                " *->\tGPS Location\n" +
                "   ->\tThis will list the GPS locations\n" +
                "   ->\tClick to view location on Google Maps.\n" +
                "\n" +
                " *->\tSettings\n" +
                "   ->\tOpen this to see their settings, like Start/Close and reminder times.\n" +
                "   ->\tFrom their settings, you might better judge their progress through a project.\n" +
                "\n" +
                "\n";
        return work_text;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.action_home:
                    //Toast.makeText(HomeActivity.this, "You selected Home", Toast.LENGTH_SHORT).show();
                    frameLO.removeAllViews();
                    adapter = new MyFragmentAdapter(getSupportFragmentManager(),HomeActivity.this);
                    viewPager.setAdapter(adapter);

                    return true;
                case R.id.action_people:
                    //Toast.makeText(HomeActivity.this, "You selected People", Toast.LENGTH_SHORT).show();

                    Bundle bundle=new Bundle();
                    bundle.putString("userId", user_id);
                    //set Fragmentclass Arguments
                    selectedFragment = new people_Fragment();
                    selectedFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLO,selectedFragment).commit();
                    viewPager.removeAllViews();
                    tabLayout.removeAllTabs();

                    return true;
                case R.id.action_person:
                    Toast.makeText(HomeActivity.this, "You selected Person", Toast.LENGTH_SHORT).show();
                    selectedFragment = new person_Fragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLO,selectedFragment).commit();
                    viewPager.removeAllViews();
                    tabLayout.removeAllTabs();

                    return true;
            }

            return true;
        }
    };



}
