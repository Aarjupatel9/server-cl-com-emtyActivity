package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "massege")
public class massege_entity {
    @PrimaryKey
    public int Chat_id;

    @ColumnInfo(name = "sender_id")
    public int SenderId;

    @ColumnInfo(name = "massege")
    public String Massege;

    @ColumnInfo(name = "massege_status")
    public int MassegeStatus;
}


//
//package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities;
//
//        import androidx.room.ColumnInfo;
//        import androidx.room.Entity;
//        import androidx.room.PrimaryKey;
//
//@Entity(tableName = "login")
//public class login_details_entity {
//    @PrimaryKey
//    public int MobileNumber;
//
//    @ColumnInfo(name = "password")
//    public String Password;
//}








