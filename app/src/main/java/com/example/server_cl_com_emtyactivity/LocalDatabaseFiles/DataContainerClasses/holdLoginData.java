package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DataContainerClasses;

import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DAoFiles.massege_Dao;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.MainDatabaseClass;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.loginDetails_entity;

public class holdLoginData {

    loginDetails_entity data;

    public holdLoginData( MainDatabaseClass db){

        massege_Dao MassegeDao = db.MassegeDao();

        loginDetails_entity dataFromDatabase = MassegeDao.getLoginDetailsFromDatabase();
        data = dataFromDatabase;
    }

    public loginDetails_entity getData(){
        return data;
    }
}
