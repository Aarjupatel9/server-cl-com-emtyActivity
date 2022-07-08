package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DAoFiles;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.loginDetails_entity;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.massege_entity;

import java.util.List;

@Dao
public interface massege_Dao {

    @Query("SELECT * FROM massege")
    List<massege_entity> getAllChatMaster();

    @Query("SELECT * FROM massege WHERE Chat_id IN (:ChatID)")
    List<massege_entity> GetChat(int ChatID);

//    @Query("SELECT * FROM massege WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert
    void insertMassegeIntoChat(massege_entity massegeEntity);

    @Delete
    void deleteMassegeOfChat(massege_entity massegeEntity);




    //fro Login activity
    @Query("SELECT * FROM login limit 1")
    loginDetails_entity getLoginDetailsFromDatabase();


    @Insert
    void SaveLoginDetailsInDatabase(loginDetails_entity loginDetailsEntity);

    @Delete
    void deleteLoginDetailsInDatabase(loginDetails_entity loginDetailsEntity);
}
