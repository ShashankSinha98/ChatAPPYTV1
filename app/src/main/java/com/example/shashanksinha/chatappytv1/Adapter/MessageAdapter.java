package com.example.shashanksinha.chatappytv1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shashanksinha.chatappytv1.MessageActivity;
import com.example.shashanksinha.chatappytv1.Model.Chat;
import com.example.shashanksinha.chatappytv1.Model.User;
import com.example.shashanksinha.chatappytv1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int IMG_MSG_TYPE_RIGHT = 2;
    public static final int IMG_MSG_TYPE_LEFT = 3;
    private Context mContext;
    private List<Chat> mChat;
    private String imageURL;

    FirebaseUser currUser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageURL){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d("xlr8_m", String.valueOf(viewType));
        if(viewType == MSG_TYPE_RIGHT){
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent, false);
        return new MessageAdapter.ViewHolder(view); } else if(viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else if (viewType == IMG_MSG_TYPE_LEFT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_image_left,parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_image_right,parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int pos) {

        Chat chat = mChat.get(pos);

        Log.d("xlr8_m",chat.getMessage()+" "+chat.getImageURL());

        if(chat.getImageURL().equals("null")) {
            Log.d("xlr8","A");
            holder.show_msg.setText(chat.getMessage());

            if (imageURL.equals("default")) {
                holder.profile_image.setImageResource(R.drawable.def_pp);
            } else {
                Glide.with(mContext).load(imageURL).into(holder.profile_image);
            }


            if (pos == mChat.size() - 1) {
                if (chat.getIsseen()) {
                    holder.text_seen.setText("Seen");
                } else {
                    holder.text_seen.setText("Delivered");
                }
            } else {
                holder.text_seen.setVisibility(View.GONE);
            }
        }

        else {

            if (imageURL.equals("default")) {
                holder.profile_image.setImageResource(R.drawable.def_pp);
            } else {
                Glide.with(mContext).load(imageURL).into(holder.profile_image);
            }

            Log.d("xlr8_m",chat.getImageURL());
            if(!chat.getImageURL().equals("null"))
            Glide.with(mContext).load(chat.getImageURL()).into(holder.img_msg);

            if (pos == mChat.size() - 1) {
                if (chat.getIsseen()) {
                    holder.text_seen.setText("Seen");
                } else {
                    holder.text_seen.setText("Delivered");
                }
            } else {
                holder.text_seen.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_msg;
        public CircleImageView profile_image;
        public ImageView img_msg;

        public TextView text_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_msg = itemView.findViewById(R.id.show_msg);
            profile_image = itemView.findViewById(R.id.profile_img_show_msg);
            text_seen = itemView.findViewById(R.id.text_seen);
            img_msg = itemView.findViewById(R.id.show_img_msg);

        }
    }

    @Override
    public int getItemViewType(int position) {
        currUser = FirebaseAuth.getInstance().getCurrentUser();


        if(mChat.get(position).getSender().equals(currUser.getUid()) && mChat.get(position).getImageURL().equals("null")){
            // If curr user is the Sender,
            return  MSG_TYPE_RIGHT;
        } else if(mChat.get(position).getReceiver().equals(currUser.getUid()) && mChat.get(position).getImageURL().equals("null")) {
            return MSG_TYPE_LEFT;
        } else if(mChat.get(position).getSender().equals(currUser.getUid()) && !mChat.get(position).getImageURL().equals("null")){
           return IMG_MSG_TYPE_RIGHT;
        } else {
            return IMG_MSG_TYPE_LEFT;
        }
    }
}

