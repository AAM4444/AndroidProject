package com.example.myapplication2.adapter;

//import
import android.content.Context;
import android.graphics.RenderNode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication2.R;
import com.example.myapplication2.OnItemClickInterface;
import com.example.myapplication2.UserInfo;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder> {

    private List<UserInfo> dataUser;
    private OnItemClickInterface listener;
    private Context mContext;

    public RecyclerViewAdapter(List<UserInfo> dataUser, Context mContext) {
        this.dataUser = dataUser;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater
                .from (viewGroup.getContext ())
                .inflate (R.layout.list_item, viewGroup, false);
        return new ViewHolder (v);
    }

    @Override
    public void onBindViewHolder (@NonNull final ViewHolder viewHolder, final int i) {
        final UserInfo datum = dataUser.get(i);

        viewHolder.tvName.setText(datum.firstName);
        viewHolder.tvLastName.setText (datum.lastName);
        viewHolder.tvEmail.setText (datum.email);
        Glide.with(mContext).load(datum.avatar).into(viewHolder.imgAvatar);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(datum.firstName,
                                datum.lastName,
                                datum.email,
                                datum.avatar,
                                datum.remoteId);
                    }
                }
            });
        }

    @Override
    public int getItemCount () {
        return dataUser.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imgAvatar;
            private TextView tvName;
            private TextView tvLastName;
            private TextView tvEmail;

        public ViewHolder (@NonNull View itemView) {
            super (itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLastName = itemView.findViewById(R.id.tv_last_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            imgAvatar.setClipToOutline(true);
        }
    }

    public void setOnItemClickListener (OnItemClickInterface listener) {
        this.listener = listener;
    }

}

