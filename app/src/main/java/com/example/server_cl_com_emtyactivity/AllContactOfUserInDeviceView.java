package com.example.server_cl_com_emtyactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DAoFiles.massege_Dao;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DataContainerClasses.contactDetailsHolderForSync;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.MainDatabaseClass;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.ContactWithMassenger_entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AllContactOfUserInDeviceView extends Activity {

    private ProgressBar loadingPB;
    private static final String url = "http://192.168.43.48:10000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("log-AllContactOfUserInDeviceView", "onCreate: enter here");
        super.onCreate(savedInstanceState);
        loadingPB = findViewById(R.id.idLoadingPB_of_AllContactView);
        setContentView(R.layout.activity_all_contact_of_user_in_device_view);
        Log.d("log-AllContactOfUserInDeviceView", "after set activity_all_contact_of_user_in_device_view, enter here");
        syncContactDetails();
    }

    private void syncContactDetails() {
        Log.d("log-AllContactOfUserInDeviceView", "syncContactDetails: enter here");
        JSONArray ContactDetails = new JSONArray();
        String endpoint = url + "syncContactOfUser";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("log-AllContactOfUserInDeviceView", "syncContactDetails: enter here endpoint is : " + endpoint);
//        loadingPB.setVisibility(View.VISIBLE);
        Log.d("log-AllContactOfUserInDeviceView", "syncContactDetails:  before request initialize");
        StringRequest request = new StringRequest(Request.Method.POST, endpoint, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
                        MainDatabaseClass.class, "MassengerDatabase").allowMainThreadQueries().build();
                contactDetailsHolderForSync contactDetailsHolder = new contactDetailsHolderForSync(db);
                List<ContactWithMassenger_entity> contactList = contactDetailsHolder.getData();

                int contactList_size = contactList.size();
                Log.d("log-sync contact-database", "  setChatDetails Database Response: " + contactList_size);
                Log.d("log-response-in-syncContact", response);
                try {
                    JSONObject respObj = new JSONObject(response);
                    String mainString = respObj.getString("ja");
                    int isOn_number = Integer.parseInt(respObj.getString("isOnnumber"));
                    Log.d("log-allcoantact sync response", "onResponse: isOnnumber is : " + isOn_number);
                    JSONArray responseArray = new JSONArray(mainString);
                    int l = responseArray.length();
                    for (int i = 0; i < isOn_number; i++) {
                        String RowOfArray = responseArray.getString(i);
                        JSONObject RowObject = new JSONObject(RowOfArray);
                        Log.d("log-setChatDetails-Response", "  setChatDetails onResponse C_ID is: " + RowObject.getString("C_ID"));
                        Log.d("log-setChatDetails-Response", "  setChatDetails onResponse number is : " + RowObject.getString("number"));
                        Log.d("log-setChatDetails-Response", "  setChatDetails onResponse nme is : : " + RowObject.getString("name"));

                        if (contactList_size == 0) {
                            //now we have to save all details in contact table
                            Log.d("log-sync contact-database", "  we have to save all contact with C_ID :" +Integer.parseInt(RowObject.getString("C_ID")));
                            ContactWithMassenger_entity newEntity = new ContactWithMassenger_entity(Long.valueOf(RowObject.getString("number")), RowObject.getString("name"), Integer.parseInt(RowObject.getString("C_ID")));
                            massege_Dao MassegeDao = db.MassegeDao();
                            MassegeDao.SaveContactDetailsInDatabase(newEntity);
                        } else {
                            //we have to check whether user is all ready connected or not
                            //if not then save details into chat
                            boolean already = false;
                            for (int j = 0; j < contactList_size; j++) {
                                ContactWithMassenger_entity contactEntity = contactList.get(j);
                                if (contactEntity.getMobileNumber().equals(Long.valueOf(RowObject.getString("number")))) {
                                    already = true;
                                }
//                                Log.d("log-sync contact-database", "  setChatDetails Database Response: " + contactEntity.getMobileNumber());
                            }
                            if (!already) {
                                ContactWithMassenger_entity newEntity = new ContactWithMassenger_entity(Long.valueOf(RowObject.getString("number")), RowObject.getString("name"), Integer.parseInt(RowObject.getString("C_ID")));
                                massege_Dao MassegeDao = db.MassegeDao();
                                MassegeDao.SaveContactDetailsInDatabase(newEntity);
                            }
                        }
                    }
                    //now we have delete of those contact who delete their account
                    for (int j = 0; j < contactList.size(); j++) {
                        boolean already = false;
                        ContactWithMassenger_entity contactEntity = contactList.get(j);
                        for (int i = 0; i < isOn_number; i++) {
                            String RowOfArray = responseArray.getString(i);
                            JSONObject RowObject = new JSONObject(RowOfArray);
//                            Log.d("log-setChatDetails-Response", "  setChatDetails onResponse id : " + RowObject.getString("id"));
//                            Log.d("log-setChatDetails-Response", "  setChatDetails onResponse number: " + RowObject.getString("number"));
//                            Log.d("log-setChatDetails-Response", "  setChatDetails onResponse name: " + RowObject.getString("name"));

//                            Log.d("log-setChatDetails-Response", "  database onResponse number is : " + contactEntity.getMobileNumber() +" and "+ (RowObject.getString("number")));
                            if (contactEntity.getMobileNumber().equals(Long.valueOf(RowObject.getString("number")))) {
                                already = true;
                            }
                        }
                        if (!already) {
                            massege_Dao MassegeDao = db.MassegeDao();
                            MassegeDao.deleteContactDetailsInDatabase(contactEntity.getMobileNumber());
                        }
                    }
//                    loadingPB.setVisibility(View.GONE);
                    setContentDetailsInView(responseArray, isOn_number);
                } catch (JSONException e) {
                    Toast.makeText(AllContactOfUserInDeviceView.this, "application error! , please try again after some time", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
//                    loadingPB.setVisibility(View.GONE);
//                    setContentDetailsInView(responseArray);
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Log.d("log-setChatDetails-Response", "  setChatDetails error: " + error);
//                loadingPB.setVisibility(View.GONE);
                Toast.makeText(AllContactOfUserInDeviceView.this, "sync failed Duo to  Server side error :  " + error, Toast.LENGTH_SHORT).show();
//                setContentDetailsInView();
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
                        counter++;
                    }
                }
                Log.d("log-contact", "json array list : " + ContactDetails);
                params.put("ContactDetails", ContactDetails.toString());
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void setLable(LinearLayout newLinearlayout) {


        TextView textView1 = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(10, 10, 10, 10);
        textView1.setPadding(20, 20, 20, 20);
        textView1.setMinHeight(175);
        textView1.setTextSize(15);
        textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView1.setLayoutParams(layoutParams);
//            textView1.setOnClickListener(Contact_massege_details_page_renderer(contactEntity.getMobileNumber()));
        textView1.setText("bellow this not in your contact");
        textView1.setId(Integer.parseInt("2000"));
        textView1.setBackgroundColor(0xFF6200EE); // hex color 0xAARRGGBB
        textView1.setPadding(0, 10, 0, 0);// in pixels (left, top, right, bottom)
        newLinearlayout.addView(textView1);
    }

    private void setContentDetailsInView(JSONArray responseArray, int isOn_number) {
        LinearLayout linearLayout = findViewById(R.id.ContactContent_of_All_contact_view);
        MainDatabaseClass db = Room.databaseBuilder(getApplicationContext(),
                MainDatabaseClass.class, "MassengerDatabase").allowMainThreadQueries().build();
        contactDetailsHolderForSync contactDetailsHolder = new contactDetailsHolderForSync(db);
        List<ContactWithMassenger_entity> contactList = contactDetailsHolder.getData();
        Log.d("log-setContentDetailsInView", "  setChatDetails Database Response: " + contactList.size());

        for (int j = 0; j < contactList.size(); j++) {
            ContactWithMassenger_entity contactEntity = contactList.get(j);

            LinearLayout newLinearlayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            newLinearlayout.setId(j);
            newLinearlayout.setLayoutParams(layoutParams3);
            newLinearlayout.setClickable(true);

            TextView textView1 = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.LEFT;
            layoutParams.setMargins(10, 10, 10, 10);
            textView1.setPadding(20, 20, 20, 20);
            textView1.setMinHeight(175);
            textView1.setTextSize(15);
            textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView1.setLayoutParams(layoutParams);
//            textView1.setOnClickListener(Contact_massege_details_page_renderer(contactEntity.getMobileNumber()));
            textView1.setText(contactEntity.getDisplay_name());
            textView1.setId(j);
            textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
            textView1.setPadding(0, 10, 0, 0);// in pixels (left, top, right, bottom)
            newLinearlayout.addView(textView1);


            TextView textView2 = new TextView(this);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.RIGHT;
            layoutParams.setMargins(10, 10, 10, 10);
            textView2.setPadding(20, 20, 20, 20);
            textView2.setMinHeight(175);
            textView2.setTextSize(15);
            textView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView2.setLayoutParams(layoutParams2);
            textView2.setClickable(true);
//            textView1.setOnClickListener(Contact_massege_details_page_renderer(contactEntity.getMobileNumber()));
            String number = String.valueOf(contactEntity.getMobileNumber());
            textView2.setText(number);
            textView2.setId(j);
            textView2.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
            textView2.setPadding(0, 10, 0, 0);// in pixels (left, top, right, bottom)
            newLinearlayout.addView(textView2);
            linearLayout.addView(newLinearlayout);
        }

        //after this we set lable
        setLable(linearLayout);

        //now we set all rest contact
        int l = responseArray.length();
        for (int i = isOn_number; i < l; i++) {
            String RowOfArray = null;
            try {
                RowOfArray = responseArray.getString(i);
                JSONObject RowObject = new JSONObject(RowOfArray);
//                Log.d("log-setChatDetails-Response", "  setChatDetails onResponse id is: " + RowObject.getString("id"));
//                Log.d("log-setChatDetails-Response", "  setChatDetails onResponse number is : " + RowObject.getString("number"));
//                Log.d("log-setChatDetails-Response", "  setChatDetails onResponse nme is : : " + RowObject.getString("name"));

                LinearLayout newLinearlayout = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                newLinearlayout.setId(i);
                newLinearlayout.setLayoutParams(layoutParams3);
                newLinearlayout.setClickable(true);

                TextView textView1 = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.LEFT;
                layoutParams.setMargins(10, 10, 10, 10);
                textView1.setPadding(20, 20, 20, 20);
                textView1.setMinHeight(175);
                textView1.setTextSize(15);
                textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView1.setLayoutParams(layoutParams);
//            textView1.setOnClickListener(Contact_massege_details_page_renderer(contactEntity.getMobileNumber()));
                textView1.setText(RowObject.getString("name"));
                textView1.setId(i);
                textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                textView1.setPadding(0, 10, 0, 0);// in pixels (left, top, right, bottom)
                newLinearlayout.addView(textView1);


                TextView textView2 = new TextView(this);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.RIGHT;
                layoutParams.setMargins(10, 10, 10, 10);
                textView2.setPadding(20, 20, 20, 20);
                textView2.setMinHeight(175);
                textView2.setTextSize(15);
                textView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView2.setLayoutParams(layoutParams2);
                textView2.setClickable(true);
//            textView1.setOnClickListener(Contact_massege_details_page_renderer(contactEntity.getMobileNumber()));
                String number = RowObject.getString("number");
                textView2.setText(number);
                textView2.setId(i);
                textView2.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                textView2.setPadding(0, 10, 0, 0);// in pixels (left, top, right, bottom)
                newLinearlayout.addView(textView2);
                linearLayout.addView(newLinearlayout);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}