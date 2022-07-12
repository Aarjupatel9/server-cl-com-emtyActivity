package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "massege")
public class massege_entity {
    @PrimaryKey(autoGenerate = true)
    public int Chat_id;

    @ColumnInfo(name = "user_id")
    public int UserId;

    @ColumnInfo(name = "C_id")
    public int C_Id;

    @ColumnInfo(name = "massege")
    public String Massege;

    @ColumnInfo(name = "massege_status")
    public int MassegeStatus;

    public massege_entity() {}

    public massege_entity(int UserId, int C_ID, String massege, int massegeStatus) {
        this.UserId = UserId;
        this.C_Id = C_ID;
        this.Massege = massege;
        this.MassegeStatus = massegeStatus;
    }

    public int getChat_id(){
        return this.Chat_id;
    }
    public int getUserId(){
        return this.UserId;
    }
    public int getC_Id(){
        return this.C_Id;
    }
    public String getMassege(){
        return this.Massege;
    }
    public int getMassegeStatus(){
        return this.MassegeStatus;
    }

}








