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

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class HomePageWithContactActivity extends Activity {
    private static final String url = "http://192.168.43.48:10000/";
    public static int user_login_id;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<ContactWithMassenger_entity> contactArrayList;
    private ArrayAdapter<String> ContactAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_with_contact);
        Intent intent = getIntent();
        user_login_id = Integer.parseInt(intent.getStringExtra("user_login_id"));

        SetChatsView(new View(this));

        Toast.makeText(this, "user_id of login is : " + user_login_id, Toast.LENGTH_SHORT).show();
    }

    public void SetChatsView(View view) {
        Toast.makeText(HomePageWithContactActivity.this, "You clicked Chats Button", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_home_page_with_contact);

        recyclerView = findViewById(R.id.ContactRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
                MainDatabaseClass.class, "MassengerDatabase").allowMainThreadQueries().build();
        contactArrayList = new ArrayList<>();
        contactDetailsHolderForSync contactDetailsHolder = new contactDetailsHolderForSync(db);
        List<ContactWithMassenger_entity> contactList = contactDetailsHolder.getData();
        Log.d("log-setContactDetails", "setContactDetails- reach here ");

        contactArrayList.addAll(contactList);
//        Log.d("log-setContactDetails", "setContactDetails- name is : " + contactArrayList.get(3).getDisplay_name());

        recyclerViewAdapter = new RecyclerViewAdapter(this, contactArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    public void SetStatusView(View view) {
        Toast.makeText(HomePageWithContactActivity.this, "You clicked status Button", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_status_view_page);
    }

    public void SetCallsView(View view) {
        Toast.makeText(HomePageWithContactActivity.this, "You clicked Calls Button", Toast.LENGTH_SHORT).show();
    }


//
//    private View.OnClickListener Contact_massege_details_page_renderer(int number) {
//        return new View.OnClickListener() {
//            public void onClick(View v) {
////                    button.setText("text now set.. ");
//                Toast.makeText(HomePageWithContactActivity.this, "hii from dynamic contact click method number is " + number, Toast.LENGTH_SHORT).show();
//                call_ContactMassegeDetailsView(number);
//            }
//        };
//    }
//
//    public void call_ContactMassegeDetailsView(int C_ID){
//        Intent intent = new Intent(this, ContactMassegeDetailsView.class);
//        intent.putExtra("user_login_id",user_login_id);
//        intent.putExtra("C_ID",C_ID);
//        Log.d("log-not logined", "onCreate: not login cond. reached");
//        startActivity(intent);
//    }
//    public  void callContactMassegeDetailsView(int C_ID, int user_login_id){
//        Intent intent = new Intent(this, ContactMassegeDetailsView.class);
//        intent.putExtra("user_login_id",user_login_id);
//        intent.putExtra("C_ID",C_ID);
//        Log.d("log-not logined", "onCreate: not login cond. reached");
//        startActivity(intent);
//    }


    public void getListOfAllUserContact(View view) {
        Toast.makeText(this, "you Click all contact details", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomePageWithContactActivity.this, AllContactOfUserInDeviceView.class);
        Log.d("log-getListOfAllUserContact", "calling getListOfAllUserContact activity");
        startActivity(intent);
    }


    public void getMainSideMenu(View view) {
        makeConnectionToServer();

    }

    private void makeConnectionToServer(){
        final Socket socket;
        try{
            String endpoint = url + "/check";
            Log.d("log-endpoint", "makeConnectionToServer: endpoint is :" + endpoint);
            socket = IO.socket(endpoint);

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    socket.emit("message", "hi");
                    socket.disconnect();
                }

            }).on("message", new Emitter.Listener() {
                //message is the keyword for communication exchanges
                @Override
                public void call(Object... args) {
                    socket.emit("message", "hi");
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {}

            });
            socket.connect();

        }
        catch(Exception e){
            Log.d("log-error in socket io", "makeConnectionToServer: error is :" + e);

        }

    }
}