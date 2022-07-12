package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DataContainerClasses;

import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DAoFiles.massege_Dao;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.MainDatabaseClass;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.loginDetails_entity;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.userIdEntityForApp;

public class userIdEntityHolder {
    userIdEntityForApp data;

    public userIdEntityHolder( MainDatabaseClass db){

        massege_Dao MassegeDao = db.MassegeDao();

        userIdEntityForApp dataFromDatabase = MassegeDao.getUserIdFromDatabase();
        data = dataFromDatabase;
    }

    public userIdEntityForApp getData(){
        return data;
    }
}
