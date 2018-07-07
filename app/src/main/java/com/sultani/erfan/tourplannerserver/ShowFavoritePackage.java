package com.sultani.erfan.tourplannerserver;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sultani.erfan.tourplannerserver.Comman.Comman;
import com.sultani.erfan.tourplannerserver.Interface.ItemClickListener;
import com.sultani.erfan.tourplannerserver.Model.GeneratePack;
import com.sultani.erfan.tourplannerserver.Model.Request;
import com.sultani.erfan.tourplannerserver.ViewHolder.FavoriteViewHolder;
import com.sultani.erfan.tourplannerserver.ViewHolder.OrderViewHolder;

public class ShowFavoritePackage extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<GeneratePack,FavoriteViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference favPackageShow;

    MaterialSpinner spinner;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_favorite_package);


        database = FirebaseDatabase.getInstance();
        favPackageShow = database.getReference("Generate");

        recyclerView = (RecyclerView) findViewById(R.id.listFavPackage);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Favorite Packages");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        loadAllFavPackages();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void loadAllFavPackages() {

        adapter = new FirebaseRecyclerAdapter<GeneratePack, FavoriteViewHolder>(
                GeneratePack.class,
                R.layout.layout_favorite_payment,
                FavoriteViewHolder.class,
                favPackageShow

        ) {
            @Override
            protected void populateViewHolder(FavoriteViewHolder viewHolder, GeneratePack model, int position) {

                viewHolder.txtFavName.setText(model.getName());
                viewHolder.txtFavPaymnet.setText(Comman.convertCodeStatus(model.getStatus()));
                viewHolder.txtFavPhone.setText(model.getPhone());
                viewHolder.txtFavToken.setText(adapter.getRef(position).getKey());
                viewHolder.txtFavTotal.setText(model.getPrice());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });


            }
        };

             adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Comman.UPDATE))
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));

        else  if (item.getTitle().equals(Comman.DELETE))
            deleteOrder(adapter.getRef(item.getOrder()).getKey());



        return super.onContextItemSelected(item);
    }


    //delete favorite pack
    private void deleteOrder(String key) {
        favPackageShow.child(key).removeValue();
    }

    //method that update status
    private void showUpdateDialog(String key, final GeneratePack item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowFavoritePackage.this);
        alertDialog.setTitle("Update Favorite Payment");
        alertDialog.setMessage("Please Choose status");
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_favorite_layout,null);
        spinner = (MaterialSpinner)view.findViewById(R.id.favSpinner);
        spinner.setItems("Progress","Paid","Not Paid");
        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));


                favPackageShow.child(localKey).setValue(item);


            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();





    }
}
