package com.example.server_cl_com_emtyactivity;

//import com.example.server_cl_com_emtyactivity.login_activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DAoFiles.massege_Dao;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DataContainerClasses.holdLoginData;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DataContainerClasses.userIdEntityHolder;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.MainDatabaseClass;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.loginDetails_entity;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.massege_entity;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.userIdEntityForApp;
import com.example.server_cl_com_emtyactivity.LoginMenagement.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class MainActivity extends Activity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int INTERNET_PERMISSION_CODE = 102;
    private static final int ACCESS_NETWORK_STATE_PERMISSION_CODE = 103;
    private static final int STORAGE_PERMISSION_CODE = 104;
    private static final int CONTACTS_PERMISSION_CODE = 105;

    private static final String url = "http://192.168.43.48:10000/";

    private int user_login_id;
    String name, email;
    Button fetchButton;
    TextView Contact_name_of_user;
    String filename = "user_login_details";
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(Manifest.permission.INTERNET, INTERNET_PERMISSION_CODE, "Internet");
        MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
                MainDatabaseClass.class, "MassengerDatabase").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        Login login = new Login();

        if (login.isLogIn(db) == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            Log.d("log-not logined", "onCreate: not login cond. reached");
            startActivity(intent);
        } else {
            //we have to get user_login_id from database

            userIdEntityHolder userIdEntityHolder = new userIdEntityHolder(db);
            userIdEntityForApp user_idClass = userIdEntityHolder.getData();
            user_login_id = user_idClass.getU_ID();

            Intent intent = new Intent(this, HomePageWithContactActivity.class);
            intent.putExtra("user_login_id", String.valueOf(user_login_id));
            startActivity(intent);
//            setContentView(R.layout.activity_main);

            checkPermission(Manifest.permission.READ_CONTACTS, CONTACTS_PERMISSION_CODE, "CONTACT");
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, "camera");
            checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, ACCESS_NETWORK_STATE_PERMISSION_CODE, "internet state");
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE, "storage");

            syncNewMassegesFromServer();

            loadingPB = findViewById(R.id.idLoadingPB);
            Contact_name_of_user = findViewById(R.id.Contact_name_of_user);

//            fetchButton = findViewById(R.id.fetchButton);
//            fetchButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    name = "aarju";
//                    email = "aarju@gmail.com";
//                    postDataUsingVolley(name, email);
//                }
//            });
        }
    }


    private int logIned() {
        Log.d("log-login-check method", "myfilereadmethod: enter in logIned check  method");
        //database relatd work
        MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
                MainDatabaseClass.class, "MassengerDatabase").allowMainThreadQueries().build();

        holdLoginData hold_LoginData = new holdLoginData(db);
        loginDetails_entity dataFromDatabase = hold_LoginData.getData();
        if (dataFromDatabase != null) {
            Log.d("log-in login check method  database data is ", " : " + dataFromDatabase.getPassword() + " qnd " + dataFromDatabase.getMobileNumber());
            int l = 0;
            long num = dataFromDatabase.getMobileNumber();
            while (num != 0) {
                num = num / 10;
                l++;
            }
            Log.d("log-in login check method  database data is ", " : " + l);

            if (l == 10) {
                return 1;
            } else {
                return 0;
            }
        }
        Log.d("log-login-check method", " not logIned cond. ");
        return 0;
    }


    //server comunication area
    private void fetUserDetailsFromServer(String email_in_file, String password_in_file) {

        String endPoint = url + "getUserDetails/";
        Log.d("log-endpoint", endPoint);

        final String[] user_email_from_server = new String[2];
        user_email_from_server[0] = "";
        user_email_from_server[1] = email_in_file;
        final String[] user_password_from_server = new String[2];
        user_password_from_server[0] = "";
        user_password_from_server[1] = password_in_file;
        final String[] user_id_from_server = new String[1];
        user_id_from_server[0] = "";
        final int[] pass = {0};
        Log.d("log-after array declaration ", "declared");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(endPoint, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadingPB.setVisibility(View.GONE);
                Log.d("log-responce ", response.toString());
                int l = response.length();
                Log.d("log-responce ", String.valueOf(l));
                for (int i = 0; i < l; i++) {
                    try {
                        String massege = response.getString(0);

                        JSONObject jsonObject_of_response = new JSONObject(massege);
                        Log.d("log-user_email_form server inside response", "fetUserDetailsFromServer: " + user_email_from_server[0]);
                        user_email_from_server[0] = user_email_from_server[0] + jsonObject_of_response.getString("user_number");
                        user_password_from_server[0] = jsonObject_of_response.getString("userPassword");
                        user_id_from_server[0] = jsonObject_of_response.getString("user_id");


                        Log.d("log-masssage-0", "at index " + i + " : " + massege);
                        Log.d("log-userDetails-from server at login check method", user_email_from_server[0] + " " + user_password_from_server[0] + " " + user_id_from_server[0]);
                        Log.d("log-userDetails-from server at login check method", user_email_from_server[0] + " " + user_email_from_server[1]);
                        Contact_name_of_user.setText(jsonObject_of_response.getString("name"));
                        Contact_name_of_user.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
//                        Contact_name_of_user.setTextColor(9);
                        Contact_name_of_user.setTextSize(20);


                        if (Objects.equals(user_email_from_server[0], user_email_from_server[1])) {
                            if (Objects.equals(user_password_from_server[0], user_password_from_server[1])) {
                                pass[0] = 1;
                                Log.d("log-masssage-0", "mached " + String.valueOf(pass[0]));
                            } else {
                                Log.d("log-masssage-0", "not mached -pass " + String.valueOf(pass[0]));
                            }
                        } else {
                            Log.d("log-masssage-0", "not mached " + String.valueOf(pass[0]));
                        }

                    } catch (JSONException e) {
                        Log.d("log-err-convert-object", e.toString() + " at index " + i);
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "server is down", Toast.LENGTH_SHORT).show();
                Log.d("log-error-responce", "Error: " + error.getMessage());
                //pDialog.dismiss();
            }
        });

        Log.d("log-after array declaration ", "after 4 secound");
        requestQueue.add(jsonObjectRequest);
    }

    private void postDataUsingVolley(String name, String job) {
        // url to post our data
        Log.d("log-p", url);
        loadingPB.setVisibility(View.VISIBLE);
        // creating a new variable for our request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadingPB.setVisibility(View.GONE);
                Log.d("log-responce ", response.toString());
                int l = response.length();
                Log.d("log-responce ", String.valueOf(l));
                for (int i = 0; i < l; i++) {
                    try {
                        JSONObject jresponse = response.getJSONObject(i);
                        String massege = jresponse.getString("massage");
                        Log.d("log-masssage-0", "at index " + i + " : " + massege);

                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.massege_layout);
                        TextView textView1 = new TextView(MainActivity.this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.RIGHT;
                        layoutParams.setMargins(10, 10, 10, 10);
                        textView1.setLayoutParams(layoutParams);

                        textView1.setText(massege);
                        textView1.setId(i);
                        textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                        textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                        linearLayout.addView(textView1);
                    } catch (JSONException e) {
                        Log.d("log-err-convert-object", e.toString() + " at index " + i);
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Log.d("log-error-responce", "Error: " + error.getMessage());
                //pDialog.dismiss();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode, String per_name) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void syncNewMassegesFromServer() {

        String endpoint = url + "syncNewMassegeFromServer";
        Log.d("log-endpoint", endpoint);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, endpoint, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "in sync new massege function!", Toast.LENGTH_SHORT).show();

                JSONArray responseArray = null;
                try {
                    responseArray = new JSONArray(response);
                    int l = responseArray.length();
                    Log.d("log-massegeSyncResponse", "onResponse- massege length is :" + l);

                    for (int i = 0; i < l; i++) {
                        String RowOfArray = responseArray.getString(i);
                        JSONObject RowMassege = new JSONObject(RowOfArray);
                        Log.d("log-massegeSyncResponse", "onResponse- massege is :" + RowMassege.getString("massage"));
                        Log.d("log-massegeSyncResponse", "onResponse- massege is :" + RowMassege.getString("sender_id"));
                        Log.d("log-massegeSyncResponse", "onResponse- massege is :" + RowMassege.getString("receiver_id"));

                        massege_entity massegeEntity = new massege_entity( Integer.parseInt(RowMassege.getString("sender_id")), Integer.parseInt(RowMassege.getString("receiver_id")),RowMassege.getString("massage"),0);
                        MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
                                MainDatabaseClass.class, "MassengerDatabase").fallbackToDestructiveMigration().allowMainThreadQueries().build();
                        massege_Dao MassegeDao = db.MassegeDao();
                        MassegeDao.insertMassegeIntoChat(massegeEntity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Server side error in sync new massege function :  " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_login_id", String.valueOf(user_login_id));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);


    }
    public void setContact_list(View view) {
        Toast.makeText(MainActivity.this, "setContact list clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomePageWithContactActivity.class);
        intent.putExtra("user_login_id", "1");
        Log.d("log-not logined", "onCreate: not login cond. reached");
        startActivity(intent);
    }
}


