package com.djax.sdkaddemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.sdk.adserver.NativeVideo;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapterNativeVideo extends RecyclerView.Adapter<ListAdapterNativeVideo.ViewHolder> {

    ArrayList<NativeVideoDatavalue> list;
    Context context;

    public ListAdapterNativeVideo(Context context, ArrayList<NativeVideoDatavalue> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public ListAdapterNativeVideo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_layout_native_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterNativeVideo.ViewHolder holder, int position) {

        NativeVideoDatavalue datavalue=list.get(position);
        holder.title.setText(datavalue.getName());
        holder.des.setText(datavalue.getDes());
        Picasso.with(context)
                .load(datavalue.getImg())
                .into(holder.iconImg);

        new NativeVideo().loadNativeVideo(holder.playerView,context);
        /*if(datavalue.getType().equalsIgnoreCase("true"))
        {
            Picasso.with(context)
                    .load(datavalue.getNativeImg())
                    .into(holder.nativeImg);
            holder.nativeImg.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.nativeImg.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView des;
        public ImageView iconImg;
        public StyledPlayerView playerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt_title);
            des = itemView.findViewById(R.id.txt_des);
            iconImg = itemView.findViewById(R.id.icon_img);
            playerView = itemView.findViewById(R.id.player_view);

        }
    }
}
