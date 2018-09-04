package com.example.anmol.leaveapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button signUp;
    private Button signin;
    private TextInputEditText email;
    DatabaseReference UserRef;
    private TextInputEditText password;
    RelativeLayout relativeLayout;
    ACProgressFlower mProgress;
    TextView forgot;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);

        mAuth=FirebaseAuth.getInstance();
        UserRef= FirebaseDatabase.getInstance().getReference().child("User");

        signUp = (Button) view.findViewById(R.id.SignUp);
        signin = (Button) view.findViewById(R.id.SignIn);
        relativeLayout=(RelativeLayout)view.findViewById(R.id.relative);
        forgot=(TextView)view.findViewById(R.id.forgot);

        email = (TextInputEditText) view.findViewById(R.id.Email);
        password = (TextInputEditText) view.findViewById(R.id.Password);

        signin.setOnClickListener(this);

        signUp.setOnClickListener(this);

        forgot.setOnClickListener(this);

        mProgress=new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .text("Signing your account").textSize(15).textColor(Color.WHITE)
                .themeColor(getResources().getColor(R.color.colorPrimary)).build();

        return view;

    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.SignUp) {

            startActivity(new Intent(getActivity(), Register.class));

        }
        if(view.getId()==R.id.forgot){

            if(TextUtils.isEmpty(email.getText().toString())){
                Toast.makeText(getActivity(),"Enter your email",Toast.LENGTH_LONG).show();
            }
            else{

                mProgress.show();
                mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            Snackbar.make(relativeLayout,"Check  your email for password change",Snackbar.LENGTH_LONG).show();

                            mProgress.dismiss();
                        }

                    }
                });

            }

        }
        if (view.getId() == R.id.SignIn) {

            if (TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {

                Toast.makeText(getActivity(), "Fields are empty", Toast.LENGTH_LONG).show();
            } else {

                mProgress.show();
                mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        final FirebaseUser userr=mAuth.getCurrentUser();

                        if(userr.isEmailVerified()){

                            if(task.isSuccessful()){
                                UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                            User data=snapshot.getValue(User.class);
                                            if(data.getEmail().equals(email.getText().toString()))
                                            {
                                                SharedPreferences preferences=getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor=preferences.edit();
                                                editor.putString("name",data.getName()).apply();
                                                editor.putString("email",data.getEmail()).apply();
                                                editor.putString("designation",data.getDesignation()).apply();
                                                editor.putString("college",data.getDesignation()).apply();
                                                editor.putString("city",data.getCity()).apply();
                                                editor.putString("image",data.getImageurl()).apply();
                                                mProgress.dismiss();
                                                startActivity(new Intent(getActivity(),FinalThread.class));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                            else{
                                Snackbar.make(relativeLayout,"Credentials are incorrect",Snackbar.LENGTH_LONG).show();
                            }

                        }
                        else{
                            Snackbar.make(relativeLayout,"Verify  your email",Snackbar.LENGTH_LONG).show();
                        }

                    }
                });


            }

        }
    }
}
