package com.example.socialcompass.Friendview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcompass.R;
import com.example.socialcompass.Friendmodel.FriendListItem;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private List<FriendListItem> friendListItems = Collections.emptyList();
    private static Consumer<FriendListItem> onDeleteClicked;

    public void setFriendListItems(List<FriendListItem> newFriendListItems){
        this.friendListItems.clear();
        this.friendListItems = newFriendListItems;
        notifyDataSetChanged();
    }

    public void setOnDeleteClickedHandler(Consumer<FriendListItem> onDeleteClicked){
        this.onDeleteClicked = onDeleteClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.friend_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setTodoItem(friendListItems.get(position));
    }

    @Override
    public int getItemCount() {
        return friendListItems.size();
    }

    public long getItemId(int position){
        return friendListItems.get(position).id;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView label;
        private final TextView publicCode;

        private FriendListItem friendListItem;
        private final TextView deleteView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.label = itemView.findViewById(R.id.friend_name);
            this.publicCode = itemView.findViewById(R.id.friend_public_code);
            this.deleteView = itemView.findViewById(R.id.delete_btn);
            this.deleteView.setOnClickListener(view -> {
                if(onDeleteClicked == null) return;
                onDeleteClicked.accept(friendListItem);
            });



        }
        public FriendListItem getTodoItem(){return friendListItem;}

        public void setTodoItem(FriendListItem friendListItem){
            this.friendListItem = friendListItem;
            this.label.setText(friendListItem.label);
            this.publicCode.setText(friendListItem.public_code);
        }




    }
}
