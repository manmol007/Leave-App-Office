package com.example.anmol.leaveapp;

import android.app.FragmentTransaction;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AppReceiveFinal extends AppCompatActivity {

    TextView t1;
    TextView t2;
    TextView t3,t5;
    TextView t4;
    ImageView accept;
    ImageView reject;
    CardView card;
    Button submit;
    Toolbar toolbar;
    EditText remark;
    Intent intent;
    SharedPreferences preferences;
    DatabaseReference ref;
    String from,to,fromdate,todate,category,subject,currentdate,designation,college;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_receive_final);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accept=(ImageView)findViewById(R.id.accept);
        reject=(ImageView)findViewById(R.id.reject);
        card=(CardView)findViewById(R.id.card);

        intent=getIntent();
        t1=(TextView)findViewById(R.id.t1);

        t2=(TextView)findViewById(R.id.t2);

        t3=(TextView)findViewById(R.id.t3);

        t4=(TextView)findViewById(R.id.t4);
        t5=(TextView)findViewById(R.id.t5);

        from=intent.getStringExtra("from");
        to=intent.getStringExtra("to");
        fromdate=intent.getStringExtra("fromdate");
        subject=intent.getStringExtra("subject");
        todate=intent.getStringExtra("todate");
        category=intent.getStringExtra("category");
        currentdate=intent.getStringExtra("currentdate");

        preferences=getSharedPreferences("User",MODE_PRIVATE);
        ref= FirebaseDatabase.getInstance().getReference().child("User");


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
                t3.setText("Respected Sir/Madam,\n");
                t4.setText(subject);
                t5.setText("\nThanking you\nYours Sincerely\n " + from);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ref= FirebaseDatabase.getInstance().getReference().child("Application");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Application_leave data=snapshot.getValue(Application_leave.class);
                            if(data.getSubject().equals(subject)){
                                DatabaseReference statusRef=FirebaseDatabase.getInstance().getReference().child("Status").push();
                                statusRef.child("state").setValue("Accepted");
                                statusRef.child("category").setValue(category);
                                statusRef.child("from").setValue(from);
                                statusRef.child("to").setValue(to);
                                ref.child(snapshot.getKey()).removeValue();
                                startActivity(new Intent(getApplicationContext(),FinalThread.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.status, null);

                final EditText edt = (EditText) dialogView.findViewById(R.id.remark);

                new MaterialStyledDialog.Builder(AppReceiveFinal.this)
                        .setTitle("Do you want to reject the application?")
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setPositiveText("Submit")
                        .setHeaderColor(R.color.colorPrimary)
                        .setCancelable(true)
                        .setCustomView(dialogView).withDarkerOverlay(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                ref= FirebaseDatabase.getInstance().getReference().child("Application");
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                            Application_leave data=snapshot.getValue(Application_leave.class);
                                            if(data.getSubject().equals(subject)){
                                                DatabaseReference statusRef=FirebaseDatabase.getInstance().getReference().child("Status").push();
                                                statusRef.child("state").setValue("Rejected because of "+edt.getText().toString());
                                                statusRef.child("category").setValue(category);
                                                statusRef.child("from").setValue(from);
                                                statusRef.child("to").setValue(to);
                                                ref.child(snapshot.getKey()).removeValue();
                                                startActivity(new Intent(getApplicationContext(),FinalThread.class));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }).show();
                }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
