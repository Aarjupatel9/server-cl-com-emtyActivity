package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "login")
public class loginDetails_entity {
    @PrimaryKey
    public int U_ID;
    @ColumnInfo(name = "MobileNumber")
    public Long MobileNumber;
    @ColumnInfo(name = "password")
    public String Password;

    public loginDetails_entity() {
    }

//    public loginDetails_entity(@NonNull String Password, Long MobileNumber) {
//        this.Password = Password;
//        this.MobileNumber = MobileNumber;
//    }

    public loginDetails_entity(int U_ID, @NonNull String Password, Long MobileNumber) {
        this.Password = Password;
        this.MobileNumber = MobileNumber;
        this.U_ID = U_ID;
    }

    @NonNull
    public void setMobileNumber(Long mobileNumber) {
        this.MobileNumber = mobileNumber;
    }

    @NonNull
    public void setPassword(String password) {
        this.Password = password;
    }

    @NonNull
    public Long getMobileNumber() {
        return this.MobileNumber;
    }
    @NonNull
    public void setU_ID(int U_ID) {
        this.U_ID = U_ID;
    }

    @NonNull
    public int getU_ID() {
        return this.U_ID;
    }

    @NonNull
    public String getPassword() {
        return this.Password;
    }
}








