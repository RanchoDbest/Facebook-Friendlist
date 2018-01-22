package com.example.rachanasolanki.facebookdemo;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rachana.solanki on 1/17/2018.
 */

public class FriendListAdapter  extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {

    private List<FriendsModel> friendsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);

        }
    }


    public FriendListAdapter(List<FriendsModel> friendsList) {
        this.friendsList = friendsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_friend, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FriendsModel friendsModel = friendsList.get(position);
        holder.name.setText(friendsModel.getName());

    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}