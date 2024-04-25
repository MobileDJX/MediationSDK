package com.djax.sdkaddemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    ArrayList<Datavalue> list;
    Context context;

    public ListAdapter(Context context,ArrayList<Datavalue> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {

        Datavalue datavalue=list.get(position);
        holder.title.setText(datavalue.getName());
        holder.des.setText(datavalue.getDes());
        Picasso.with(context)
                .load(datavalue.getImg())
                .into(holder.iconImg);
        if(datavalue.getType().equalsIgnoreCase("true"))
        {
            Picasso.with(context)
                    .load(datavalue.getNativeImg())
                    .into(holder.nativeImg);
            holder.nativeImg.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.nativeImg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView des;
        public ImageView iconImg;
        public ImageView nativeImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt_title);
            des = itemView.findViewById(R.id.txt_des);
            iconImg = itemView.findViewById(R.id.icon_img);
            nativeImg = itemView.findViewById(R.id.native_img);

        }
    }
}
