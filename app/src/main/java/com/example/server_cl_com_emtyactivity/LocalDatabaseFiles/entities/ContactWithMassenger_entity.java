package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contactDetails")
public class ContactWithMassenger_entity {

    @PrimaryKey
    @ColumnInfo(name = "C_ID")
    public int C_ID;
    @ColumnInfo(name = "MobileNumber")
    public Long MobileNumber;
    @ColumnInfo(name = "DisplayName")
    public String Display_name;


    public ContactWithMassenger_entity(){}
    public ContactWithMassenger_entity(Long MobileNumber, String Display_name, int C_ID){
        this.MobileNumber = MobileNumber;
        this.Display_name = Display_name;
        this.C_ID = C_ID;
    }


    @NonNull
    public void setMobileNumber(Long mobileNumber) {
        this.MobileNumber = mobileNumber;
    }

    @NonNull
    public void setC_ID(int C_ID) {
        this.C_ID =C_ID;
    }
    @NonNull
    public void setDisplay_name(String Display_name){
        this.Display_name = Display_name;
    }

    @NonNull
    public Long getMobileNumber() {
        return this.MobileNumber;
    }

    @NonNull
    public int getC_ID() {
        return this.C_ID;
    }

    @NonNull
    public String getDisplay_name(){
        return  this.Display_name;
    }
}
