package com.example.anmol.leaveapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FragmentManager manager;
    private LoginFragment loginFragment;
    private FragmentTransaction transaction;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    toolbar=(Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    preferences=getSharedPreferences("User",MODE_PRIVATE);

    Intent intent=getIntent();
    String str=intent.getStringExtra("LoginDesig");

    if(preferences.getString("email",null)!=null){
        startActivity(new Intent(getApplicationContext(),FinalThread.class));
    }
else {

        loginFragment = new LoginFragment();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.layout, loginFragment, "Login");
        transaction.commit();

    }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
