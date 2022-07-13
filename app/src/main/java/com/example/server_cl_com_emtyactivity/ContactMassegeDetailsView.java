package com.example.server_cl_com_emtyactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DataContainerClasses.ContactMassegeHolder;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.MainDatabaseClass;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.ContactWithMassenger_entity;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.massege_entity;
import com.example.server_cl_com_emtyactivity.RecyclerViewClassesFolder.ContactMassegeRecyclerViewAdapter;
import com.example.server_cl_com_emtyactivity.RecyclerViewClassesFolder.RecyclerViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactMassegeDetailsView extends Activity {
    private static final String url = "http://192.168.43.48:10000/";
    static int counter = 0;
    TextView Contact_id_textfiled;
    TextView massege_field;
    int user_login_id;
    int C_ID;
    String ContactName;

    private TextView Contact_name_of_user;
    private RecyclerView massege_recyclerView;
    private ContactMassegeRecyclerViewAdapter massegeRecyclerViewAdapter;
    private ArrayList<massege_entity> massegeArrayList;
    private ArrayAdapter<String> ContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        user_login_id = intent.getIntExtra("user_login_id", -1);
        C_ID = intent.getIntExtra("C_ID", -1);
        ContactName = intent.getStringExtra("ContactName");
        setContentView(R.layout.activity_contact_massege_details_view);
        Contact_id_textfiled = findViewById(R.id.Contact_id);
        Contact_name_of_user = findViewById(R.id.Contact_name_of_user);
        Contact_name_of_user.setText(ContactName);
        massege_field = findViewById(R.id.write_massege);
        massege_recyclerView = findViewById(R.id.ContactMassegeRecyclerView);

        Contact_id_textfiled.setText(String.valueOf(C_ID));

        setAllMassege(C_ID);

    }

    private void setAllMassege(int c_ID) {
        MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
                MainDatabaseClass.class, "MassengerDatabase").allowMainThreadQueries().build();
        ContactMassegeHolder massegeHolder = new ContactMassegeHolder(db, c_ID);
        List<massege_entity> massegeList = massegeHolder.getMassegeList();

        massegeArrayList = new ArrayList<>();
        for (massege_entity e : massegeList) {
            Log.d("log-massege","massge is : "+e.getMassege());
        }
        massegeArrayList.addAll(massegeList);

        massege_recyclerView.setHasFixedSize(true);
        massege_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        massegeRecyclerViewAdapter = new ContactMassegeRecyclerViewAdapter(this, massegeArrayList);
        massege_recyclerView.setAdapter(massegeRecyclerViewAdapter);
    }


    public void getContactDetailsOfUser(View view) {
        Toast.makeText(this, "you Cllicked in User name", Toast.LENGTH_SHORT).show();
    }


    public void Send_massege_of_user(View view) {
        counter++;
        String user_massege = massege_field.getText().toString();
        String Contact_id = Contact_id_textfiled.getText().toString();
        Log.d("log-massege is : ", user_massege + " and contact_id is : " + Contact_id);
        sendMassegeToserver(user_massege, Contact_id);
        massege_field.setText("");
    }

    private void sendMassegeToserver(String user_massege, String Contact_id) {
        Log.d("log-counter is ", "sendMassegeToServer: counter is : " + String.valueOf(counter));
        String endpoint = url + "sendMassegeToServer";
        Log.d("log-d", endpoint);
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, endpoint, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ContactMassegeDetailsView.this, "Login succsesfull enjoy it!", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject respObj = new JSONObject(response);
                    String status = respObj.getString("status");
                    Log.d("log-massege-response-status", status);

                    if (status.equals("1")) {
                        Log.d("log-massege", "massege sent succsesfully");
                        saveMassegeIntoLocalDatabase(user_massege, Contact_id);
                    } else if (status.equals("0")) {
                        Log.d("log-user-id", "status is else");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("log-error", "onResponse: err in try bracet : " + e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ContactMassegeDetailsView.this, "Server side error :  " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("user_login_id", String.valueOf(user_login_id));
                params.put("C_ID", Contact_id);
                params.put("massege", user_massege);

                // at last we are
                // returning our params.
                return params;
            }
        };
        Log.d("log-d", "before add in request queue");
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(request);
        Log.d("log-d", "after add in request queue");
    }

    private void saveMassegeIntoLocalDatabase(String user_massege, String Contact_id) {

    }
}