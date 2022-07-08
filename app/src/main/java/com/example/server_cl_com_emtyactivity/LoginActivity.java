package com.example.server_cl_com_emtyactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DAoFiles.massege_Dao;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DataContainerClasses.holdLoginData;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.MainDatabaseClass;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.loginDetails_entity;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.massege_entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String url = "http://192.168.43.48:10000/";

    EditText userPhone;
    EditText userPassword;
    Button login;
    private ProgressBar loadingPB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("log-not logined", "onCreate: inside Oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity_main);

        userPhone = findViewById(R.id.userPhone);
        userPassword = findViewById((R.id.userPassword));
        login = findViewById(R.id.login_button);
        loadingPB = findViewById(R.id.LoadingPBOfLoginPage);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_password = userPassword.getText().toString();
                String user_number = userPhone.getText().toString();
                Log.d("log-loginbutton hit ", user_number + " and " + user_password);
                if (!user_number.equals("")) {
                    checkHaveToRegister(user_number, user_password);
//                    checkHaveToRegisterInDatabase(user_number, user_password);
                } else {
                    Log.d("log-else ", "enter in else cond");
//                    checkHaveToRegisterInDatabase(user_number, user_password);
                }
            }
        });
    }

    private void mainactivity_caller() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void checkHaveToRegisterInDatabase(String user_number, String user_password) {

        Log.d("log-reached", "enter in checkHaveToRegisterInDatabase ");

        Long number = Long.valueOf(user_number);
//        int number = Integer.parseInt(user_number);
        loginDetails_entity login_details = new loginDetails_entity(user_password, number);
        Log.d("log-reached", "before db initialize : " + login_details.getMobileNumber() + " qnd " + login_details.getPassword());


        MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
                MainDatabaseClass.class, "MassengerDatabase").allowMainThreadQueries().build();


        Log.d("log-reached", "after db initialize");
        massege_Dao MassegeDao = db.MassegeDao();

        MassegeDao.SaveLoginDetailsInDatabase(login_details);

        holdLoginData holdLoginData = new holdLoginData(db);
//        loginDetails_entity dataFromDatabase = MassegeDao.getLoginDetailsFromDatabase();
        loginDetails_entity dataFromDatabase = holdLoginData.getData();
//        while(true){
//            if(dataFromDatabase.getPassword() == null ) {
        Log.d("log-databse data is ", " : " + dataFromDatabase.getPassword() + " qnd " + dataFromDatabase.getMobileNumber());
//            }
//        }


//        massege_entity massege_details  = new massege_entity();
//        massege_details.Massege = "hi i mam aarju patel";
//        massege_details.Chat_id = 1;
//        massege_details.MassegeStatus = 1;
//        massege_details.SenderId = 1;
//        MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
//                MainDatabaseClass.class, "MassengerDatabase").build();
//        massege_Dao massegeDao = db.MassegeDao();
//        massegeDao.insertMassegeIntoChat(massege_details);
//        Log.d("log-databse data is ", " : data is inserted into database");
//        massege_entity dataFromDatabase = (massege_entity) massegeDao.getAllChatMaster();
//        Log.d("log-databse data is ", " : "+dataFromDatabase);


    }

    private void checkHaveToRegister(String user_number, String user_password) {
        loadingPB.setVisibility(View.VISIBLE);
        // creating a new variable for our request queue
        String endpoint = url + "checkHaveToRegister";
        Log.d("log-e", endpoint);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, endpoint, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Login succsesfull enjoy it!", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject respObj = new JSONObject(response);
                    String status = respObj.getString("status");
                    Log.d("log-respone-status", status);

                    if (status.equals("1")) {
                        login(user_number, user_password);
                    } else if (status.equals("2")) {
                        Register(user_number, user_password);
                    }
                    else if(status.equals("0")){
                        Toast.makeText(LoginActivity.this, "Wrong Password!!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("log-error", "onResponse: err in try bracet : " + e);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Server side error :  " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("number", user_number);
                params.put("password", user_password);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        requestQueue.add(request);
    }

    public void login(String user_number, String user_password) {

        //here we are storing login details to local database
        Long number = Long.valueOf(user_number);

        loginDetails_entity login_details = new loginDetails_entity(user_password, number);
        Log.d("log-reached", "before db initialize : " + login_details.getMobileNumber() + " qnd " + login_details.getPassword());

        MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
                MainDatabaseClass.class, "MassengerDatabase").allowMainThreadQueries().build();

        Log.d("log-reached", "after db initialize");
        massege_Dao MassegeDao = db.MassegeDao();
        MassegeDao.SaveLoginDetailsInDatabase(login_details);
        Log.d("log-login details saved", "after saved Details");
        mainactivity_caller();
    }
    public void Register(String user_number, String user_password) {
        Toast.makeText(this, "work in prosecc", Toast.LENGTH_SHORT).show();
    }
}