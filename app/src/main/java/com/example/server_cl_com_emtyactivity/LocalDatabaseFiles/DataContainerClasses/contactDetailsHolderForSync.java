package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DataContainerClasses;

import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DAoFiles.massege_Dao;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.MainDatabaseClass;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.ContactWithMassenger_entity;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.loginDetails_entity;

import java.util.List;

public class contactDetailsHolderForSync {
    List<ContactWithMassenger_entity> data;

    public contactDetailsHolderForSync( MainDatabaseClass db){

        massege_Dao MassegeDao = db.MassegeDao();

        List<ContactWithMassenger_entity> dataFromDatabase = MassegeDao.getContactDetailsFromDatabase();
        data = dataFromDatabase;
    }

    public List<ContactWithMassenger_entity> getData(){
        return data;
    }
}
