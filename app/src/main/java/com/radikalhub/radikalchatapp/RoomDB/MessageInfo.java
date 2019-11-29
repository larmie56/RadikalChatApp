package com.radikalhub.radikalchatapp.RoomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MessageInfo {

    @PrimaryKey
    public int messageId;

    @ColumnInfo(name = "sender")
    public String sender;
    @ColumnInfo (name = "message")
    public String message;

}
