package com.example.anmol.leaveapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity {

    TextView t1;
    TextView t3;
    TextView t4,t5;
    Intent intent;
    Toolbar toolbar;
    String from,to,fromdate,todate,category,subject,currentdate,designation,college;
    SharedPreferences preferences;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);



        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences=getSharedPreferences("User",MODE_PRIVATE);
        ref= FirebaseDatabase.getInstance().getReference().child("User");

        t1=(TextView)findViewById(R.id.t1);
        t3=(TextView)findViewById(R.id.t3);
        t4=(TextView)findViewById(R.id.t4);
        t5=(TextView)findViewById(R.id.t5);

        intent=getIntent();

        from=intent.getStringExtra("from");
        to=intent.getStringExtra("to");
        fromdate=intent.getStringExtra("fromdate");
        subject=intent.getStringExtra("subject");
        todate=intent.getStringExtra("todate");
        category=intent.getStringExtra("category");
        currentdate=intent.getStringExtra("currentdate");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        User user=snapshot.getValue(User.class);
                        if(user.getName().equals(to)){
                            designation=user.getDesignation();
                            college=user.getCollege();
                        }
                }
                t1.setText("To,\nThe "+designation+",\n"+college+".\n"+preferences.getString("city",null)+"\n\n"+currentdate);
                t3.setText("Respected Sir/Madam,");
                t4.setText(subject);
                t5.setText("Thanking you\nYours Sincerely\n " + preferences.getString("name",null));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.edit){

            Intent intent=new Intent(getApplicationContext(),Edit.class);
            intent.putExtra("from",from);
            intent.putExtra("to",to);
            intent.putExtra("fromdate",fromdate);
            intent.putExtra("currentdate",currentdate);
            intent.putExtra("todate",todate);
            intent.putExtra("subject",subject);
            intent.putExtra("category",category);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
