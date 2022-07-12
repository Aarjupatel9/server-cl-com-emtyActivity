package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

public class userIdEntityForApp {
    public int U_ID;

    public void setU_ID(int user_id){
        this.U_ID = user_id;
    }
    public int getU_ID(){
        return  this.U_ID;
    }
}
