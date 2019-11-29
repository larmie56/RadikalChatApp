package com.radikalhub.radikalchatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.radikalhub.radikalchatapp.RoomDB.MessageInfo;

import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<MessageInfo> mMessageInfos;

    public ChatRecyclerAdapter(Context context, List<MessageInfo> messageInfos) {
        mContext = context;
        mMessageInfos = messageInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //TODO: implement displaying username
        MessageInfo messageInfo = mMessageInfos.get(position);
        holder.mTextViewLastMessage.setText(messageInfo.message);

    }

    @Override
    public int getItemCount() {
        return mMessageInfos == null ? 0 : mMessageInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextViewChatName;
        private final TextView mTextViewLastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewChatName = itemView.findViewById(R.id.textView_chat_name);
            mTextViewLastMessage = itemView.findViewById(R.id.textView_last_message);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }




    }
}
