package com.radikalhub.radikalchatapp.RoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insertMessage(MessageInfo messageInfo);

    @Query("SELECT * FROM MessageInfo")
    List<MessageInfo> getAllMessages();

    @Delete
    void delete(MessageInfo messageInfo);
}
