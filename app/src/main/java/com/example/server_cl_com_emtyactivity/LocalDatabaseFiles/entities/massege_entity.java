package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "massege")
public class massege_entity {
    @PrimaryKey(autoGenerate = true)
    public int Chat_id;

    @ColumnInfo(name = "sender_id")
    public int SenderId;

    @ColumnInfo(name = "receiver_id")
    public int ReceiverId;

    @ColumnInfo(name = "massege")
    public String Massege;

    @ColumnInfo(name = "massege_status")
    public int MassegeStatus;

    public massege_entity() {}

    public massege_entity(int UserId, int C_ID, String massege, int massegeStatus) {
        this.SenderId = UserId;
        this.ReceiverId = C_ID;
        this.Massege = massege;
        this.MassegeStatus = massegeStatus;
    }

    public int getChat_id(){
        return this.Chat_id;
    }
    public int getUserId(){
        return this.SenderId;
    }
    public int getC_Id(){
        return this.ReceiverId;
    }
    public String getMassege(){
        return this.Massege;
    }
    public int getMassegeStatus(){
        return this.MassegeStatus;
    }

}








