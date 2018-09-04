package com.example.anmol.leaveapp;

import android.app.Fragment;
import android.content.Context;
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
import android.widget.LinearLayout;
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

import static android.content.Context.MODE_PRIVATE;

public class ReceivedApplication extends android.support.v4.app.Fragment {

    private RecyclerView list;
    ArrayList<status> state;
    DatabaseReference ref;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.sentapplication,container,false);

        preferences=getActivity().getSharedPreferences("User",MODE_PRIVATE);

        list=(RecyclerView)view.findViewById(R.id.Recyclerview);

        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        list.setHasFixedSize(true);

        state=new ArrayList<>();

        ref= FirebaseDatabase.getInstance().getReference().child("Status");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    status data=snapshot.getValue(status.class);
                    if(data.getFrom().equals(preferences.getString("name",null))){
                        state.add(data);
                    }
                }
                Myadapter adapter=new Myadapter(state);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.MyHolder>{

        ArrayList<status> state=new ArrayList<>();


        public Myadapter(ArrayList<status> state){

            this.state=state;

        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sentlayout,parent,false);

            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, final int position) {


            if(state.get(position).getState().contains("Reject")){

                holder.card.setCardBackgroundColor(-1560346624);
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MaterialStyledDialog dialog=new MaterialStyledDialog.Builder(getActivity())
                                .setDescription("Your "+state.get(position).getCategory()+" has been "+state.get(position).getState())
                                .setStyle(Style.HEADER_WITH_ICON)
                                .setIcon(R.drawable.comment)
                                .setPositiveText("Ok")
                                .setHeaderColorInt(-1560346624)
                                .setCancelable(true)
                                .withDarkerOverlay(true)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });
            }
            else{

                holder.card.setCardBackgroundColor(getResources().getColor(R.color.colortwo));
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MaterialStyledDialog dialog=new MaterialStyledDialog.Builder(getActivity())
                                .setDescription("Your "+state.get(position).getCategory()+" has been "+state.get(position).getState())
                                .setStyle(Style.HEADER_WITH_ICON)
                                .setIcon(R.drawable.comment)
                                .setPositiveText("Ok")
                                .setHeaderColor(R.color.colortwo)
                                .setCancelable(true)
                                .withDarkerOverlay(true)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });
            }
            holder.subject.setText("Your "+state.get(position).getCategory()+" has been "+state.get(position).getState());
            holder.to.setText(state.get(position).getTo());

        }

        @Override
        public int getItemCount() {
            return state.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder{

            TextView to;
            TextView subject;
            View view;
            CardView card;

            public MyHolder(View itemView) {
                super(itemView);

                card=(CardView)itemView.findViewById(R.id.card);
                view = itemView;
                to = (TextView) itemView.findViewById(R.id.towhom);
                subject = (TextView) itemView.findViewById(R.id.title);
            }
        }
    }
}
