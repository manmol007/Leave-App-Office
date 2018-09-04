package com.example.anmol.leaveapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppReceive extends android.support.v4.app.Fragment{

    RecyclerView list;
    ArrayList<Application_leave> data;
    private myAdapter adapter;
    private DatabaseReference AppRef;
    private SharedPreferences preferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.sentapplication,container,false);

        data=new ArrayList<>();
        list=(RecyclerView)view.findViewById(R.id.Recyclerview);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setHasFixedSize(true);

        preferences=getActivity().getSharedPreferences("User",Context.MODE_PRIVATE);

        AppRef= FirebaseDatabase.getInstance().getReference().child("Application");

        AppRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Application_leave app=snapshot.getValue(Application_leave.class);
                    if(app.getTo().equals(preferences.getString("name",null))){
                        data.add(app);
                    }
                }
                adapter = new myAdapter(data);
                adapter.notifyDataSetChanged();
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
        }


    public class myAdapter extends RecyclerView.Adapter<myAdapter.myHolder> {

        ArrayList<Application_leave> data=new ArrayList<>();

        public myAdapter(ArrayList<Application_leave> data) {

            this.data = data;
        }


        @NonNull
        @Override
        public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sentlayout, parent, false);
            return new myHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull myHolder holder, final int position) {

            holder.to.setText(data.get(position).getFrom());
            holder.subject.setText(data.get(position).getCategory());


            holder.card.setCardBackgroundColor(getResources().getColor(R.color.layout));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), AppReceiveFinal.class);
                    intent.putExtra("from",data.get(position).getFrom());
                    intent.putExtra("to",data.get(position).getTo());
                    intent.putExtra("fromdate",data.get(position).getFromdate());
                    intent.putExtra("todate",data.get(position).getTodate());
                    intent.putExtra("category",data.get(position).getCategory());
                    intent.putExtra("subject",data.get(position).getSubject());
                    intent.putExtra("currentdate",data.get(position).getCurrentdate());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class myHolder extends RecyclerView.ViewHolder {

            TextView to;
            TextView subject;
            View view;
            CardView card;

            public myHolder(View itemView) {
                super(itemView);

                view = itemView;
                card=(CardView)itemView.findViewById(R.id.card);
                to = (TextView) itemView.findViewById(R.id.towhom);
                subject = (TextView) itemView.findViewById(R.id.title);
            }
        }
    }
}

