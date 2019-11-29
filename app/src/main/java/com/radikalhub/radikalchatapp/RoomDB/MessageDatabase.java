package com.radikalhub.radikalchatapp.RoomDB;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MessageInfo.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {

    public abstract MessageDao messageDao();
}
