package com.sultani.erfan.tourplannerserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.sultani.erfan.tourplannerserver.Comman.Comman;
import com.sultani.erfan.tourplannerserver.Interface.ItemClickListener;
import com.sultani.erfan.tourplannerserver.R;

/**
 * Created by Erfan on 12/17/2017.
 */

public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnCreateContextMenuListener {

    public TextView txtFavName,txtFavPaymnet,txtFavPhone,txtFavToken,txtFavTotal;

    private ItemClickListener itemClickListener;

    public FavoriteViewHolder(View itemView) {
        super(itemView);

        txtFavName = (TextView)itemView.findViewById(R.id.pack_name_fav);
        txtFavPaymnet = (TextView)itemView.findViewById(R.id.fav_status);
        txtFavPhone = (TextView)itemView.findViewById(R.id.fav_phone);
        txtFavToken = (TextView)itemView.findViewById(R.id.fav_credit);
        txtFavTotal = (TextView)itemView.findViewById(R.id.total_amount_fav);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);



    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select Action");
        menu.add(0,0,getAdapterPosition(), Comman.UPDATE);
        menu.add(0,1,getAdapterPosition(),Comman.DELETE);

    }
}
