package com.example.server_cl_com_emtyactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ContactMassegeDetailsView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_massege_details_view);
    }


    public void getContactDetailsOfUser(View view){
        Toast.makeText(this, "you Cllicked in User name", Toast.LENGTH_SHORT).show();
    }
}