package com.example.server_cl_com_emtyactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomePageWithContactActivity extends Activity {
    private static final String url = "http://192.168.43.48:10000/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChatDetails();
    }

    public void SetChatsView(View view) {
        Toast.makeText(HomePageWithContactActivity.this, "You clicked Chats Button", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_home_page_with_contact);

        setChatDetails();

    }

    public void SetStatusView(View view) {
        Toast.makeText(HomePageWithContactActivity.this, "You clicked status Button", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_status_view_page);
    }

    public void SetCallsView(View view) {
        Toast.makeText(HomePageWithContactActivity.this, "You clicked Calls Button", Toast.LENGTH_SHORT).show();
    }



    private View.OnClickListener Contact_massege_details_page_renderer( String number) {
        return new View.OnClickListener() {
            public void onClick(View v) {
//                    button.setText("text now set.. ");
                Toast.makeText(HomePageWithContactActivity.this, "hii from dynamic contact click method number is " + number, Toast.LENGTH_SHORT).show();
                call_ContactMassegeDetailsView(number);
            }
        };
    }

    private void call_ContactMassegeDetailsView(String number){
        Intent intent = new Intent(this, ContactMassegeDetailsView.class);

        Log.d("log-not logined", "onCreate: not login cond. reached");
        startActivity(intent);
    }

    //functions
    private void setChatDetails() {
        setContentView(R.layout.activity_home_page_with_contact);


        JSONArray ContactDetails = new JSONArray();

        String endpoint = url + "syncContactOfUser";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, endpoint, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(HomePageWithContactActivity.this, "Login succsesfull enjoy it!", Toast.LENGTH_SHORT).show();
//                try {
                Log.d("log-response-in-syncContact", response.toString());

                try {
                    JSONArray responseArray = new JSONArray(response);
                    int l = responseArray.length();
//                    ConstraintLayout constraintLayout =  findViewById(R.id.ChatsContent);
                    LinearLayout linearLayout = findViewById(R.id.ChatsContent);

                    for (int i = 0; i < l; i++) {
                        String RowOfArray = responseArray.getString(i);
                        JSONObject RowObject = new JSONObject(RowOfArray);
                        Log.d("log-setChatDetails-Response", "  setChatDetails onResponse: " + RowObject);
                        Log.d("log-setChatDetails-Response", "  setChatDetails onResponse: " + RowObject.getString("id"));
                        Log.d("log-setChatDetails-Response", "  setChatDetails onResponse: " + RowObject.getString("number"));
                        Log.d("log-setChatDetails-Response", "  setChatDetails onResponse: " + RowObject.getString("name"));

//                        //here i will set layout
//                        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT);
////                        layoutParams.gravity = Gravity.RIGHT;
//                        layoutParams.setMargins(10, 10, 10, 10);
//                        ConstraintLayout contactField = new ConstraintLayout(HomePageWithContactActivity.this);
//                        contactField.setLayoutParams(layoutParams);
////                        contactField.setText(massege);
//                        contactField.setId(i);
//                        contactField.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
//                        contactField.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
//                        constraintLayout.addView(contactField);
//                        //here will set content of layout
//                        ConstraintLayout constraintLayout1 =  findViewById(R.id.);

                        TextView textView1 = new TextView(HomePageWithContactActivity.this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.CENTER;
                        layoutParams.setMargins(10, 10, 10, 10);
                        textView1.setMinHeight(200);
                        textView1.setTextSize(20);
                        textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        textView1.setLayoutParams(layoutParams);
                        textView1.setClickable(true);
                        textView1.setOnClickListener(Contact_massege_details_page_renderer(RowObject.getString("number")));
                        textView1.setText(RowObject.getString("name"));
                        textView1.setId(i);
                        textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                        textView1.setPadding(0, 10,0,0);// in pixels (left, top, right, bottom)
                        linearLayout.addView(textView1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                    JSONObject respObj = new JSONObject(response);
//                    String status = respObj.getString("status");
//                    Log.d("log-respone-status", status);`

//                    if(status.equals("1")){
//                        loginOrRegister(user_number, user_password);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.d("log-error", "onResponse: err in try bracet : "+ e);
//                }
            }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Log.d("log-setChatDetails-Response", "  setChatDetails error: " + error);
                Toast.makeText(HomePageWithContactActivity.this, "Server side error :  " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                ContentResolver contentResolver = getContentResolver();
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

                Cursor cursor = contentResolver.query(uri, null, null, null, null);
                Log.d("log-contact", "getContacts: enterd and number is " + cursor.getCount());

                if (cursor.getCount() > 0) {
                    int counter = 0;
                    while (cursor.moveToNext()) {
//                Log.d("log-contact", "getContacts: enterd");
                        @SuppressLint("Range") String display_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                display_name.replaceAll("\\s","");
                        @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        number = number.replaceAll("\\s", "");
                        number = number.replaceAll("-", "");

                        JSONObject jsonParam = new JSONObject();
                        try {
                            //Add string params
                            jsonParam.put("id", counter);
                            jsonParam.put("name", display_name);
                            jsonParam.put("number", number);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ContactDetails.put(jsonParam);
//                Log.d("log-contact", "json array list : " + jsonParam);

//                        if (Objects.equals(number, "6353884460")) {
//                            Log.d("log-contact", "getContacts: enterd");
//                            Log.d("log-contact", display_name + " number is : " + number);
//                        }
                        counter++;
                    }
                }
                Log.d("log-contact", "json array list : " + ContactDetails);

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("ContactDetails", ContactDetails.toString());

                // at last we are
                // returning our params.
                return params;
            }
        };
        requestQueue.add(request);
    }
}