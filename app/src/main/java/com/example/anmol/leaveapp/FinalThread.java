package com.example.anmol.leaveapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class FinalThread extends AppCompatActivity {


    private ArrayList<String> data=new ArrayList<>();
    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager pager;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_thread);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences=getSharedPreferences("User",MODE_PRIVATE);

        tabs=(TabLayout)findViewById(R.id.tabs);

        pager=(ViewPager)findViewById(R.id.pager);

        setPager(pager);

        tabs.setupWithViewPager(pager);


    }

    public void setPager(ViewPager pager){


        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());

        Log.i("desig",preferences.getString("designation",null));

        if(preferences.getString("designation",null).equals("Student")){
            ReceivedApplication receive=new ReceivedApplication();
            SentApplication sent=new SentApplication();
            CreateApplication createApplication=new CreateApplication();
            adapter.addFragment(createApplication,"Create");
            adapter.addFragment(sent,"Sent");
            adapter.addFragment(receive,"Status");
        }
        else{

            AppReceive sent = new AppReceive();
            adapter.addFragment(sent, "Application Received");
        }
        adapter.notifyDataSetChanged();
        pager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> FragmntList = new ArrayList<>();
        private ArrayList<String> FragmentContent = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return FragmntList.get(position);
        }

        @Override
        public int getCount() {
            return FragmntList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            FragmntList.add(fragment);
            FragmentContent.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return FragmentContent.get(position);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.mainmenu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.logout){

            preferences.edit().clear().commit();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        return true;
    }
}
