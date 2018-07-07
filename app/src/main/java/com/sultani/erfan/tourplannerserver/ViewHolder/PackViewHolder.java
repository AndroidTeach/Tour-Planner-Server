package com.sultani.erfan.tourplannerserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sultani.erfan.tourplannerserver.Comman.Comman;
import com.sultani.erfan.tourplannerserver.Interface.ItemClickListener;

import com.sultani.erfan.tourplannerserver.R;

/**
 * Created by Sultani on 10/17/2017.
 */

public class PackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener

{

    public TextView pack_name;
    public ImageView pack_image;
    private ItemClickListener itemClickListener;

    public PackViewHolder(View itemView) {
        super(itemView);

        pack_name =(TextView)itemView.findViewById(R.id.pack_name);
        pack_image = (ImageView)itemView.findViewById(R.id.pack_image);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        contextMenu.setHeaderTitle("select action");

        contextMenu.add(0,0,getAdapterPosition(),Comman.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(),Comman.DELETE);

    }
}
