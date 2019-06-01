package com.example.shashanksinha.chatappytv1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shashanksinha.chatappytv1.Fragments.UsersFragment;
import com.example.shashanksinha.chatappytv1.MessageActivity;
import com.example.shashanksinha.chatappytv1.Model.Chat;
import com.example.shashanksinha.chatappytv1.Model.User;
import com.example.shashanksinha.chatappytv1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private Boolean isChat;
    private String theLastMessage;

    public UserAdapter(Context mContext, List<User> mUsers, Boolean isChat){
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int pos) {

        final User user = mUsers.get(pos);

        Log.d("xxx",user.getUsername());
        holder.username.setText(user.getUsername());

        if(user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.drawable.def_pp);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }

        if(isChat){
            lastMessage(user.getId(), holder.last_msg);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                UsersFragment.search_users.setText("");
                mContext.startActivity(intent);
            }
        });

        if(isChat){
            if(user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public CircleImageView profile_image;
        private CircleImageView img_on;
        private CircleImageView img_off;

        private TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user_item_username_tv);
            profile_image = itemView.findViewById(R.id.user_item_image_civ);

            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);

        }
    }

    private void lastMessage(final String userid, final TextView last_msg_tv){

        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String imageUri = "null";

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);


                    if (firebaseUser != null && userid != null) {

                        Log.d("xnxx_s",chat.getReceiver());
                        Log.d("xnxx_r",chat.getSender());
                        Log.d("xnxx_sid",firebaseUser.getUid());
                        Log.d("xnxx_rid",userid);

                            if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                    chat.getReceiver().equals(userid)
                                            && chat.getSender().equals(firebaseUser.getUid())) {
                                theLastMessage = chat.getMessage();

                                imageUri = chat.getImageURL();

                            }

                    }
                }

                Log.d("xmxx",userid+" "+theLastMessage+" "+imageUri);

                switch (theLastMessage){
                    case "default":
                        last_msg_tv.setText("No Last Message");
                        break;


                    case "null":
                        if(!imageUri.equals("null")){
                            last_msg_tv.setText("An Image has been send/recieved...");
                            break;
                        }

                     default:
                         last_msg_tv.setText(theLastMessage);
                         break;
                }

                theLastMessage = "default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
