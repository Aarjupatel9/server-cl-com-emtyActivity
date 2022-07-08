package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "login")
public class loginDetails_entity {
    @PrimaryKey
    public Long MobileNumber;
    @ColumnInfo(name = "password")
    public String Password;

    public loginDetails_entity(){}
    public loginDetails_entity(@NonNull String Password, Long MobileNumber){
        this.Password = Password;
        this.MobileNumber = MobileNumber;
    }

    @NonNull
    public void setMobileNumber(Long mobileNumber) {
        this.MobileNumber = mobileNumber;
    }

    @NonNull
    public void setPassword(String password) {
        this.Password =password;
    }

    @NonNull
    public Long getMobileNumber() {
        return this.MobileNumber;
    }

    @NonNull
    public String getPassword() {
        return this.Password;
    }
}








