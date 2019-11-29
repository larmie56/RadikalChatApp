package com.radikalhub.radikalchatapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.radikalhub.radikalchatapp.RoomDB.MessageDatabase;
import com.radikalhub.radikalchatapp.RoomDB.MessageInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String DATABASE_NAME = "message.db";
    private MessageDatabase mMesageDatabase;
    private MessageInfo[] mMessageInfos;
    private List<MessageInfo> mListMessageInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMesageDatabase = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, DATABASE_NAME).build();
        setUpDisplayContent();
        RecyclerView recyclerView = findViewById(R.id.recyclerView_chat_item);
        ChatRecyclerAdapter chatRecyclerAdapter = new ChatRecyclerAdapter(this, mListMessageInfos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(chatRecyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpDisplayContent() {
        AsyncTask<String, Void, List<MessageInfo>> task = new AsyncTask<String, Void, List<MessageInfo>>() {
            @Override
            protected List<MessageInfo> doInBackground(String... strings) {
                return mMesageDatabase.messageDao().getAllMessages();
            }

            @Override
            protected void onPostExecute(List<MessageInfo> messageInfos) {
                super.onPostExecute(messageInfos);
                if (messageInfos != null) {
                    mListMessageInfos = messageInfos;
                    int arraySize = 15;
                    mMessageInfos = new MessageInfo[arraySize];
                    int index = 0;
                    for (MessageInfo messageInfo: messageInfos) {
                        if (index > arraySize * 0.75) {
                            arraySize = arraySize * 2;
                            MessageInfo[] newArray = new MessageInfo[arraySize];
                            int newIndex = 0;
                            for (MessageInfo messageInfo1: mMessageInfos) {
                                newArray[newIndex] = messageInfo1;
                                newIndex++;
                            }
                            mMessageInfos = new MessageInfo[arraySize];
                            System.arraycopy(newArray, 0, mMessageInfos, 0, newIndex - 1);
                        }
                        mMessageInfos[index] = messageInfo;
                        index++;
                        setInTextView(/*user, */messageInfo.message);
                    }
                }
            }
        };

        task.execute("");
    }

    private void setInTextView(/*String user, */String message) {
        TextView textViewUser = findViewById(R.id.textView_chat_name);
        TextView textViewMessage = findViewById(R.id.textView_last_message);

        textViewUser.setText("TODO: display_username");
        textViewMessage.setText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
