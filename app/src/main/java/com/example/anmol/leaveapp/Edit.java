package com.example.anmol.leaveapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Edit extends AppCompatActivity {

    TextView date,subject,last,t1,t2;
    String designation,college,city;
    EditText main;
    Toolbar toolbar;
    String name;
    Button submit;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        date=(TextView)findViewById(R.id.date);
    main=(EditText)findViewById(R.id.main);
    last=(TextView)findViewById(R.id.last);
    submit=(Button)findViewById(R.id.Submit);
    t1=(TextView)findViewById(R.id.t1);
    t2=(TextView)findViewById(R.id.t2);

    final Intent intent=getIntent();

        date.setText(intent.getStringExtra("currentdate"));
        ref= FirebaseDatabase.getInstance().getReference().child("User");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    if(user.getName().equals(intent.getStringExtra("to"))){
                        designation=user.getDesignation();
                        college=user.getCollege();
                        city=user.getCity();
                    }
                }

                t1.setText("To,\nThe "+designation);
                t2.setText(college+"\n"+city);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        last.setText("Thanking you\nYours Sincerely\n " + intent.getStringExtra("from"));

        main.setText(intent.getStringExtra("subject"));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String content=main.getText().toString();

                if(TextUtils.isEmpty(content)) {

               Toast.makeText(getApplicationContext(),"Incomplete fields",Toast.LENGTH_LONG).show();
                }
                else{
                    ref= FirebaseDatabase.getInstance().getReference().child("Application");

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                Application_leave data=snapshot.getValue(Application_leave.class);

                                if(data.getSubject().equals(intent.getStringExtra("subject"))&&data.getCategory().equals(intent.getStringExtra("category"))){
                                    ref.child(snapshot.getKey()).child("subject").setValue(content);
                                }

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    startActivity(new Intent(getApplicationContext(), FinalThread.class));
                    }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
