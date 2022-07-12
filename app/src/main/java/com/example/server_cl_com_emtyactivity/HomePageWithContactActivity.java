package com.example.server_cl_com_emtyactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DataContainerClasses.contactDetailsHolderForSync;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.MainDatabaseClass;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.ContactWithMassenger_entity;
import com.example.server_cl_com_emtyactivity.RecyclerViewClassesFolder.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomePageWithContactActivity extends Activity {
    private static final String url = "http://192.168.43.48:10000/";
    public static int  user_login_id;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<ContactWithMassenger_entity> contactArrayList;
    private ArrayAdapter<String> ContactAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_with_contact);

        recyclerView = findViewById(R.id.ContactRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        user_login_id = Integer.parseInt(intent.getStringExtra("user_login_id"));
        setContactDetails();


        Toast.makeText(this, "user_id of login is : "+user_login_id, Toast.LENGTH_SHORT).show();
//        setChatDetails();
    }

    public void SetChatsView(View view) {
        Toast.makeText(HomePageWithContactActivity.this, "You clicked Chats Button", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_home_page_with_contact);
//        setChatDetails();
        setContactDetails();

    }

    public void SetStatusView(View view) {
        Toast.makeText(HomePageWithContactActivity.this, "You clicked status Button", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_status_view_page);
    }

    public void SetCallsView(View view) {
        Toast.makeText(HomePageWithContactActivity.this, "You clicked Calls Button", Toast.LENGTH_SHORT).show();
    }



    private View.OnClickListener Contact_massege_details_page_renderer(int number) {
        return new View.OnClickListener() {
            public void onClick(View v) {
//                    button.setText("text now set.. ");
                Toast.makeText(HomePageWithContactActivity.this, "hii from dynamic contact click method number is " + number, Toast.LENGTH_SHORT).show();
                call_ContactMassegeDetailsView(number);
            }
        };
    }

    public void call_ContactMassegeDetailsView(int C_ID){
        Intent intent = new Intent(this, ContactMassegeDetailsView.class);
        intent.putExtra("user_login_id",user_login_id);
        intent.putExtra("C_ID",C_ID);
        Log.d("log-not logined", "onCreate: not login cond. reached");
        startActivity(intent);
    }
    public  void callContactMassegeDetailsView(int C_ID, int user_login_id){
        Intent intent = new Intent(this, ContactMassegeDetailsView.class);
        intent.putExtra("user_login_id",user_login_id);
        intent.putExtra("C_ID",C_ID);
        Log.d("log-not logined", "onCreate: not login cond. reached");
        startActivity(intent);
    }

    //functions

//    private void setChatDetails() {
//        LinearLayout linearLayout = findViewById(R.id.ChatsContent);
//
//        MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
//                MainDatabaseClass.class, "MassengerDatabase").allowMainThreadQueries().build();
//        contactDetailsHolderForSync contactDetailsHolder = new contactDetailsHolderForSync(db);
//        List<ContactWithMassenger_entity> contactList =  contactDetailsHolder.getData();
//        Log.d("log-sync contact-database", "  setChatDetails Database Response contactList size : " + contactList.size());
//
//        for (int i=0; i<contactList.size(); i++) {
//            ContactWithMassenger_entity contactEntity = contactList.get(i);
//            Log.d("log-sync contact-database", "  setChatDetails Database Response: " + contactEntity.getC_ID());
//            Log.d("log-sync contact-database", "  setChatDetails Database Response: " + contactEntity.getDisplay_name());
//            Log.d("log-sync contact-database", "  setChatDetails Database Response: " + contactEntity.getMobileNumber());
//
//            TextView textView1 = new TextView(this);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.gravity = Gravity.LEFT;
//            layoutParams.setMargins(10, 10, 10, 10);
//            textView1.setPadding(20,20,20,20);
//            textView1.setMinHeight(175);
//            textView1.setTextSize(15);
//            textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            textView1.setLayoutParams(layoutParams);
//            textView1.setClickable(true);
//            Log.d("log-setChatDetails: C_ID from database", "setChatDetails: C_ID from database is : " +contactEntity.getC_ID());
//            textView1.setOnClickListener(Contact_massege_details_page_renderer(contactEntity.getC_ID()));
//            textView1.setText(contactEntity.getDisplay_name());
//            textView1.setId(i);
//            textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
//            textView1.setPadding(0, 10, 0, 0);// in pixels (left, top, right, bottom)
//            linearLayout.addView(textView1);
//        }
//    }

    public void getListOfAllUserContact(View view) {
        Toast.makeText(this, "you Click all contact details", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomePageWithContactActivity.this, AllContactOfUserInDeviceView.class);
        Log.d("log-getListOfAllUserContact", "calling getListOfAllUserContact activity");
        startActivity(intent);
    }

    private void setContactDetails(){

        MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
                MainDatabaseClass.class, "MassengerDatabase").allowMainThreadQueries().build();
        contactArrayList = new ArrayList<>();
        contactDetailsHolderForSync contactDetailsHolder = new contactDetailsHolderForSync(db);
        List<ContactWithMassenger_entity> contactList =  contactDetailsHolder.getData();
        Log.d("log-setContactDetails", "setContactDetails- reach here ");

        contactArrayList.addAll(contactList);
                    Log.d("log-setContactDetails", "setContactDetails- name is : "+contactArrayList.get(3).getDisplay_name());

//        recyclerViewAdapter.notifyDataStateChanged();
        recyclerViewAdapter = new RecyclerViewAdapter(this, contactArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

    }
}