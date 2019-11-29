package com.radikalhub.radikalchatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.radikalhub.radikalchatapp.RoomDB.MessageDatabase;
import com.radikalhub.radikalchatapp.RoomDB.MessageInfo;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "message.db";
    private static final String LOG = "ChatActivity";

    private EditText mEditTextSender;
    private boolean right = true;
    private MessageDatabase mMessageDatabase;
    private int mostRecentId = 0;
    private MessageInfo[] mMessageInfos;
    private ScrollView mScrollView;
    List<MessageInfo> mMessageInfoList;
    Handler mHandlerGet;
    private Thread mGetMessagesThread;
    private Thread mWriteMessagesThread;
    Handler mHandlerWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mMessageDatabase = Room.databaseBuilder(getApplicationContext(),
                MessageDatabase.class, DATABASE_NAME).build();

        mScrollView = findViewById(R.id.scroll_view);

        mHandlerGet = new Handler(getMainLooper());
        mHandlerWrite = new Handler(getMainLooper());

        if (savedInstanceState == null)
            initializeDisplayContent();
        mEditTextSender = findViewById(R.id.editText_message_to_send);
    }


    private void initializeDisplayContent() {
        mGetMessagesThread = new Thread() {
            @Override
            public void run() {
                Log.d(LOG, "Thread running on: " + currentThread().getId());
                mMessageInfoList = mMessageDatabase.messageDao().getAllMessages();

                mHandlerGet.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mMessageInfoList != null) {
                            mostRecentId = mMessageInfoList.size();
                            int arraySize = 50;
                            mMessageInfos = new MessageInfo[arraySize];
                            int index = 0;
                            for (MessageInfo messageInfo: mMessageInfoList) {
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
                                setInTextView(messageInfo.message, right);
                                right = !right;
                            }
                        }
                    }
                });
            }
        };
        mGetMessagesThread.start();
    }

    public void sendMessage(View view) {
        String messageToSend = mEditTextSender.getText().toString();
        setInTextView(messageToSend, right);
        writeToDatabase(messageToSend);
        right = !right;
        mEditTextSender.setText("");
        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    private void writeToDatabase(String messageToSend) {
        final MessageInfo messageInfo = new MessageInfo();
        messageInfo.messageId = generateMessageId();
        messageInfo.sender = "A";
        messageInfo.message = messageToSend;

        mWriteMessagesThread = new Thread() {
            @Override
            public void run() {
                mMessageDatabase.messageDao().insertMessage(messageInfo);
            }
        };
        mWriteMessagesThread.start();
    }

    private int generateMessageId() {
        mostRecentId = mostRecentId + 1;
        return mostRecentId;
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void setInTextView(String messageToSend, boolean right) {
        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        if (!right) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setText(messageToSend);
            textView.setBackgroundColor(getResources().getColor(R.color.backgroundMessageA));
            textView.setGravity(Gravity.START);
            textView.setTextSize(24.0f);
            textView.setPadding(8, 8, 8, 8);
            linearLayout.addView(textView);
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
        else {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.END;
            layoutParams.setMargins(8, 8, 8, 8);
            textView.setLayoutParams(layoutParams);
            textView.setText(messageToSend);
            textView.setBackgroundColor(getResources().getColor(R.color.backgroundMessageB));
            textView.setGravity(Gravity.END);
            textView.setTextSize(24.0f);
            textView.setPadding(8, 8, 8, 8);
            linearLayout.addView(textView);
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}
