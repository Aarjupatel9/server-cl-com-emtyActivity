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
import com.example.server_cl_com_emtyactivity.HomePageWithContactActivity;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.ContactWithMassenger_entity;
import com.example.server_cl_com_emtyactivity.MainActivity;
import com.example.server_cl_com_emtyactivity.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<ContactWithMassenger_entity> contactList;

    public RecyclerViewAdapter(Context context, List<ContactWithMassenger_entity> contactList) {
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactviewrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        ContactWithMassenger_entity contact = contactList.get(position);
        holder.Display_Name.setText(contact.getDisplay_name());
        holder.Number.setText(String.valueOf(contact.getMobileNumber()));

    }

    @Override
    public int getItemCount() {
        Log.d("log-getItemCount", "getItemCount: size is : "+ contactList.size());
        return contactList.size();
    }

    public void notifyDataStateChanged() {
        Log.d("log-notifyDataStateChanged", "notifyDataStateChanged: enter in method");
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView Display_Name;
        public TextView Number;
        public ImageView DPImageButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            Display_Name = itemView.findViewById(R.id.Display_Name);
            Number = itemView.findViewById(R.id.PhoneNumber);
            DPImageButton = itemView.findViewById(R.id.DPimageButton);
            DPImageButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.d("log-clicked", "you clicked Contact");
            int position = this.getAdapterPosition();
            ContactWithMassenger_entity contact = contactList.get(position);
            String name = contact.getDisplay_name();
            long phone = contact.getMobileNumber();
            Toast.makeText(context, "The position is " + String.valueOf(position) +
                    " Name: " + name + ", Phone:" + phone, Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(context, ContactMassegeDetailsView.class);
//            intent.putExtra("Rname", name);
//            intent.putExtra("Rphone", phone);
//            context.startActivity(intent);
            int C_ID = contact.getC_ID();
            String ContactName = contact.getDisplay_name();
//            HomePageWithContactActivity.call_ContactMassegeDetailsView(C_ID);

            Intent intent = new Intent(context.getApplicationContext(), ContactMassegeDetailsView.class);
            intent.putExtra("user_login_id",user_login_id);
            intent.putExtra("C_ID",C_ID);
            intent.putExtra("ContactName",ContactName);
            Log.d("log-not logined", "onCreate: not login cond. reached");
            context.startActivity(intent);
        }
    }
}
