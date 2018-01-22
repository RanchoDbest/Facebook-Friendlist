package com.example.rachanasolanki.facebookdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rachana.solanki on 1/17/2018.
 */

public class FriendListHolder extends RecyclerView.ViewHolder {

    public ImageView profile_pic;
    public TextView name;

    public FriendListHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.name);
        profile_pic = (ImageView) itemView.findViewById(R.id.profile_pic);

    }
}
