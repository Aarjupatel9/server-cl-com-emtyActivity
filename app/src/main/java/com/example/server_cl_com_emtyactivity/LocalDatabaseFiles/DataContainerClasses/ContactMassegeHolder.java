package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DataContainerClasses;

import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DAoFiles.massege_Dao;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.MainDatabaseClass;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.massege_entity;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.userIdEntityForApp;

import java.util.List;

public class ContactMassegeHolder {
    List<massege_entity>  data;

    public ContactMassegeHolder( MainDatabaseClass db , int C_ID){

        massege_Dao MassegeDao = db.MassegeDao();
        data = MassegeDao.GetChat(C_ID);
    }

    public  List<massege_entity> getMassegeList(){
        return data;
    }
}
