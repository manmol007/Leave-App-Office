package com.example.anmol.leaveapp;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.karthikraj.shapesimage.ShapesImage;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class CreateApplication extends android.support.v4.app.Fragment {

    private Button submit,edit;
    private TextInputEditText subject;
    private String sentto;
    private String sentsubject;
    private TextView FromText;
    private String categoRY;
    private TextView ToText;
    private int mYear,mMonth,mDay;
    private Spinner category;
    long time1,time2,result;
    String time3,time4,sub=null;
    DatabaseReference UserRef;
    String college;
    ShapesImage propic;
    SharedPreferences preferences;
    private Spinner towhom;
    private ArrayList<String> Towhom=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.createapplication,container,false);

        preferences=getActivity().getSharedPreferences("User",MODE_PRIVATE);

        Log.i("pref",preferences.toString());

        category=(Spinner)v.findViewById(R.id.category);

        propic=(ShapesImage) v.findViewById(R.id.propic);

        towhom=(Spinner)v.findViewById(R.id.towhom);

        edit=(Button)v.findViewById(R.id.edit);
        Towhom.clear();

        String[] cat={"Select Leave","Sick Leave","Casual Leave","Privilege Leave","Festival Leave","Maternity Leave","Leave without pay"};

        ArrayAdapter adapter=new ArrayAdapter(getActivity(),R.layout.spin,cat);

        college=preferences.getString("college",null);


        category.setAdapter(adapter);

        if(preferences.getString("image",null)!=null){

            propic.setImageURI(Uri.parse(preferences.getString("image",null)));
                    }
        UserRef= FirebaseDatabase.getInstance().getReference().child("User");
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User u = snapshot.getValue(User.class);
                    if (!u.getDesignation().equals("Student")) {
                        Towhom.add(u.getName());
                    }
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.spin, Towhom);
                towhom.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        submit=(Button)v.findViewById(R.id.Submit);
        subject=(TextInputEditText)v.findViewById(R.id.Subject);

        FromText=(TextView)v.findViewById(R.id.fromtext);
        ToText=(TextView) v.findViewById(R.id.totext);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sentto=towhom.getSelectedItem().toString();
                sentsubject=subject.getText().toString();
                categoRY=category.getSelectedItem().toString();

                result=((time2-time1)/(24*60*60*1000))+1;

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
                String s= mdformat.format(calendar.getTime());

                if(sentto.equals("Select Recipient")){
                    Toast.makeText(getActivity(),"Invalid recipient",Toast.LENGTH_LONG).show();
                }
                else{
                    if(result<2){
                        Toast.makeText(getActivity(),"Invalid Date",Toast.LENGTH_LONG).show();
                    }
                    else {


                        if(categoRY.equals("Select Leave")){

                            Toast.makeText(getActivity(),"Select a valid leave",Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(TextUtils.isEmpty(sentsubject)){
                                Toast.makeText(getActivity(),"Reason field is empty",Toast.LENGTH_LONG).show();
                            }
                            else{
                                if(categoRY.equals("Sick Leave")) {
                                    sub="It is to notify you that I am suffering from "+sentsubject+" and currently facing health issues.On these grounds of being sick, I am not able to join office for "+result+" days. I want you to kindly consider my please and grant me leave from"+time3+" to"+time4+". I shall be utterly obliged.";
                                }
                                else if(categoRY.equals("Casual Leave")) {
                                    sub="This is to request you to kindly grant me a casual leave for "+result+" days i.e. "+time3+" to"+time4+". I need this leave because of "+sentsubject+" which is unavoidable.\n\nI request you to grant me leave for " +result + " days.";
                                }
                                else if(categoRY.equals("Privilege Leave")) {

                                    sub="With dues respect I would like to inform you that from past so many months I havnt completed my leaves so I want you to give me leave for " + sentsubject + " from my left leaves .Hence I require leave for " + result + " days i.e. from " + time3 + " to " + time4 + ".\n\nI request you to grant me leave for " + result+ " days.";
                                }

                                else if(categoRY.equals("Festival Leave")) {
                                    sub="I would like to state that our festival of " +sentsubject+" is coming. In our culture it is celebrated with great anticipation. Due to which I wont be able to come to job.\n\nKindly grant "+result+" days leave to be a part of this festival i.e." +time3+" to "+ time4+".";
                                }
                                else if(categoRY.equals("Maternity Leave")) {

                                    sub="Most respectfully I am requesting you to leave for "+ result +" days from "+ time3+" to " + time4+"Because I am pregnent and my family doctor suggests me bed rest as for few days before and after delivery because of "+sentsubject+".During my absense my colleague will handle all the routine tasks as I have already given him the details.\n\nI request you to grant me leave for " +result + " days.";
                                }
                                else {
                                    sub="I must regretfully request a "+result+" days leave starting from " +time3+" to "+time4+" because of the "+sentsubject+".During my absense my colleague will handle all the routine tasks as I have already given him the details.\n\nI request you to grant me leave for " + result + " days.";
                                }
                                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                                intent.putExtra("from", preferences.getString("name", null));
                                intent.putExtra("to", sentto);
                                intent.putExtra("fromdate", time3);
                                intent.putExtra("todate", time4);
                                intent.putExtra("category", categoRY);
                                intent.putExtra("subject", sub);
                                intent.putExtra("currentdate", s);
                                subject.setText("");
                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Application").push();
                                ref.child("subject").setValue(sub);
                                ref.child("category").setValue(categoRY);
                                ref.child("to").setValue(sentto);
                                ref.child("currentdate").setValue(s);
                                ref.child("fromdate").setValue(FromText.getText().toString());
                                ref.child("todate").setValue(ToText.getText().toString());
                                ref.child("from").setValue(preferences.getString("name",null));
                                ref.child("status").setValue("false");
                                FromText.setText("");
                                ToText.setText("");
                                startActivity(intent);
                            }
                            }

                    }
                }



            }
        });

        FromText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        time3=i2 + "-" + (i1 + 1) + "-" + i;

                        FromText.setText(time3);
                        c.set(i,i1,i2);

                        time1=c.getTimeInMillis();


                    }
                },mYear,mMonth,mDay);

                dialog.getDatePicker().setMinDate(c.getTimeInMillis());

                dialog.show();

            }
        });



        ToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog=new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        c.set(i,i1,i2);
                        time4=i2 + "-" + (i1 + 1) + "-" + i;
                        ToText.setText(time4);

                        time2=c.getTimeInMillis();

                    }
                },mYear,mMonth,mDay);

                if(time1<0){
                    dialog.getDatePicker().setMinDate(c.getTimeInMillis());
                }else{
                    dialog.getDatePicker().setMinDate(time1);
                }

                dialog.show();

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sentto=towhom.getSelectedItem().toString();
                sentsubject=subject.getText().toString();
                categoRY=category.getSelectedItem().toString();

                result=((time2-time1)/(24*60*60*1000))+1;

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy ");
                String s= mdformat.format(calendar.getTime());

                if(sentto.equals("Select Recipient")){
                    Toast.makeText(getActivity(),"Invalid recipient",Toast.LENGTH_LONG).show();
                }
                else{
                    if(result<2){
                        Toast.makeText(getActivity(),"Invalid Date",Toast.LENGTH_LONG).show();
                    }
                    else {
                        if(categoRY.equals("Select Leave")){

                            Toast.makeText(getActivity(),"Select a valid leave",Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(TextUtils.isEmpty(sentsubject)){
                                    Toast.makeText(getActivity(),"Reason is empty",Toast.LENGTH_LONG).show();
                            }
                            else{
                                if(categoRY.equals("Sick Leave")) {
                                    sub="It is to notify you that I am suffering from "+sentsubject+" and currently facing health issues.On these grounds of being sick, I am not able to join office for "+result+" days. I want you to kindly consider my please and grant me leave from"+time3+" to"+time4+". I shall be utterly obliged.";
                                }
                                else if(categoRY.equals("Casual Leave")) {
                                    sub="This is to request you to kindly grant me a casual leave for "+result+" days i.e. "+time3+" to"+time4+". I need this leave because of "+sentsubject+" which is unavoidable.\n\nI request you to grant me leave for " +result + " days.";
                                }
                                else if(categoRY.equals("Privilege Leave")) {

                                    sub="With dues respect I would like to inform you that from past so many months I havnt completed my leaves so I want you to give me leave for " + sentsubject + " from my left leaves .Hence I require leave for " + result + " days i.e. from " + time3 + " to " + time4 + ".\n\nI request you to grant me leave for " + result+ " days.";
                                }

                                else if(categoRY.equals("Festival Leave")) {
                                    sub="I would like to state that our festival of " +sentsubject+" is coming. In our culture it is celebrated with great anticipation. Due to which I wont be able to come to job.\n\nKindly grant "+result+" days leave to be a part of this festival i.e." +time3+" to "+ time4+".";
                                }
                                else if(categoRY.equals("Maternity Leave")) {

                                    sub="Most respectfully I am requesting you to leave for "+ result +" days from "+ time3+" to " + time4+"Because I am pregnent and my family doctor suggests me bed rest as for few days before and after delivery because of "+sentsubject+".During my absense my colleague will handle all the routine tasks as I have already given him the details.\n\nI request you to grant me leave for " +result + " days.";
                                }
                                else {
                                    sub="I must regretfully request a "+result+" days leave starting from " +time3+" to "+time4+" because of the "+sentsubject+".During my absense my colleague will handle all the routine tasks as I have already given him the details.\n\nI request you to grant me leave for " + result + " days.";
                                }
                                subject.setText("");
                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Application").push();
                                ref.child("subject").setValue(sub);
                                ref.child("category").setValue(categoRY);
                                ref.child("to").setValue(sentto);
                                ref.child("currentdate").setValue(s);
                                ref.child("fromdate").setValue(FromText.getText().toString());
                                ref.child("todate").setValue(ToText.getText().toString());
                                ref.child("from").setValue(preferences.getString("name",null));
                                ref.child("status").setValue("false");
                            }
                        }

                    }
                }
                        }
        });
        return v;
    }

}
