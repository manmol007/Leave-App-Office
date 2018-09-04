package com.example.anmol.leaveapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    Spinner Designation;
    Button register;
    TextInputEditText RegName;
    TextInputEditText RegEmail;
    TextInputEditText RegPassword,RegCollege,RegCity;
    FirebaseAuth mAuth;
    String Desig;
    String Name_Reg,key=null;
    static int REQ=1;
    Uri img;
    String Email_Reg;
    CircleImageView profile;
    List<String> emails;
    ACProgressFlower mProgress;
    String Password_Reg,College_Reg,City_Reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String[] designation={"Student","Head of Department","Director","Accountant","Professor","Examination department"};

        emails=new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        Designation=(Spinner)findViewById(R.id.designation);
        register =(Button)findViewById(R.id.RegisterSignUp);
        RegEmail=(TextInputEditText)findViewById(R.id.EmailRegister);
        RegName=(TextInputEditText)findViewById(R.id.NameRegister);
        RegPassword=(TextInputEditText)findViewById(R.id.PasswordRegister);
        RegCollege=(TextInputEditText)findViewById(R.id.CollegeRegister);
        RegCity=(TextInputEditText)findViewById(R.id.CityRegister);
        profile=(CircleImageView) findViewById(R.id.profile);
        ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),R.layout.spin,designation);
        Designation.setAdapter(adapter);
        mProgress=new ACProgressFlower.Builder(Register.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .text("Creating your account").textSize(15).textColor(Color.WHITE)
                .themeColor(getResources().getColor(R.color.colorPrimary)).build();
        register.setOnClickListener(this);
        Designation.setOnItemSelectedListener(this);
        profile.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ&&resultCode==RESULT_OK){

            img=data.getData();
            profile.setImageURI(img);
        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.profile){
            Intent intent= new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent,REQ);
        }
        else if(view.getId()==R.id.RegisterSignUp){


            Name_Reg=RegName.getText().toString();
            Email_Reg=RegEmail.getText().toString();
            Password_Reg=RegPassword.getText().toString();
            College_Reg=RegCollege.getText().toString();
            City_Reg=RegCity.getText().toString();

            if(TextUtils.isEmpty(Name_Reg)||TextUtils.isEmpty(City_Reg)|| TextUtils.isEmpty(College_Reg)||TextUtils.isEmpty(Email_Reg)||TextUtils.isEmpty(Password_Reg)||TextUtils.isEmpty(Desig)){
                Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
            }
            else {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (Email_Reg.matches(emailPattern) && Email_Reg.length() > 0){
                    if(Password_Reg.length()>=8){

                        mProgress.show();
                        mAuth.fetchProvidersForEmail(Email_Reg).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                                    if(task.getResult().getProviders().size()>0){

                                        mProgress.dismiss();
                                        Toast.makeText(getApplicationContext(), "User already exist", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        mAuth.createUserWithEmailAndPassword(Email_Reg,Password_Reg).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (task.isSuccessful()){
                                                    FirebaseUser user=mAuth.getCurrentUser();
                                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){
                                                                Toast.makeText(getApplicationContext(),"Check your mail for email verification",Toast.LENGTH_LONG).show();
                                                                final DatabaseReference RegisterReference= FirebaseDatabase.getInstance().getReference().child("User");
                                                                DatabaseReference ref = RegisterReference.push();
                                                                ref.child("name").setValue(Name_Reg);
                                                                ref.child("email").setValue(Email_Reg);
                                                                ref.child("designation").setValue(Desig);
                                                                ref.child("college").setValue(College_Reg);
                                                                ref.child("city").setValue(City_Reg);
                                                                if(img!=null){
                                                                    ref.child("imageurl").setValue(img.toString());
                                                                }
                                                                mProgress.dismiss();
                                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                startActivity(intent);

                                                            }
                                                        }
                                                    });
                                                }

                                            }
                                        });
                                    }
                            }
                        });

                    }
                    else
                        Toast.makeText(this,"Minimum password size is 8",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(this,"EMail is not valid",Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Desig=Designation.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
