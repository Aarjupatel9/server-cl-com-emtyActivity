package com.example.server_cl_com_emtyactivity.RecyclerViewClassesFolder;

import static com.example.server_cl_com_emtyactivity.HomePageWithContactActivity.user_login_id;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.server_cl_com_emtyactivity.ContactMassegeDetailsView;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.ContactWithMassenger_entity;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.massege_entity;
import com.example.server_cl_com_emtyactivity.R;

import java.util.List;

public class ContactMassegeRecyclerViewAdapter extends RecyclerView.Adapter<ContactMassegeRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<massege_entity> massegeList;

    public ContactMassegeRecyclerViewAdapter(Context context, List<massege_entity> massegeList) {
        this.massegeList = massegeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactMassegeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.massege_design_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactMassegeRecyclerViewAdapter.ViewHolder holder, int position) {
        massege_entity massege = massegeList.get(position);
        holder.massege_display.setText(massege.getMassege());
    }

    @Override
    public int getItemCount() {
        Log.d("log-getItemCount", "getItemCount: size is : " + massegeList.size());
        return massegeList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView massege_display;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            massege_display = itemView.findViewById(R.id.main_massege);
        }

        @Override
        public void onClick(View view) {
            Log.d("log-clicked", "you clicked on massege");
        }
    }
}
