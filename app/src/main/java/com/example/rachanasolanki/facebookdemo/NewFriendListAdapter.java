package com.example.rachanasolanki.facebookdemo;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rachana.solanki on 1/17/2018.
 */

public class NewFriendListAdapter extends RecyclerView.Adapter {
        ArrayList<FriendsModel> item_list;
        Context context;

public NewFriendListAdapter(Context context, ArrayList<FriendsModel> item_list) {
        this.context = context;
        this.item_list = item_list;
        }

public void addData(List<FriendsModel> item_list) {
        this.item_list.addAll(item_list);
        notifyDataSetChanged();
        }

public FriendsModel getItemAtPosition(int position) {
        return item_list.get(position);
        }

public void addLoadingView() {
        //add loading item
        new Handler().post(new Runnable() {
@Override
public void run() {
        item_list.add(null);
        notifyItemInserted(item_list.size() - 1);
        }
        });
        }

        public void removeLoadingView() {
                //Remove loading item
                item_list.remove(item_list.size() - 1);
                notifyItemRemoved(item_list.size());
        }
                @Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.VIEW_TYPE_ITEM) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friend, parent, false);
        return new FriendListHolder(view);
        } else if (viewType == Constant.VIEW_TYPE_LOADING) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_loading, parent, false);
        return new LoadingHolder(view);
        }
        return null;
        }

@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FriendListHolder) {

            FriendListHolder userViewHolder = (FriendListHolder) holder;

             userViewHolder.name.setText(item_list.get(position).getName());

             Glide.with(context).load(item_list.get(position).getProfile_pic()).into(userViewHolder.profile_pic);



        } else if (holder instanceof LoadingHolder) {

        }

        }

@Override
public int getItemCount() {
        return item_list == null ? 0 : item_list.size();
        }

@Override
public int getItemViewType(int position) {
        return item_list.get(position) == null ? Constant.VIEW_TYPE_LOADING : Constant.VIEW_TYPE_ITEM;
        }


}
