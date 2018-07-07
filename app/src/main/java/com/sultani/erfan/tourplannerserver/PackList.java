package com.sultani.erfan.tourplannerserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.sultani.erfan.tourplannerserver.Comman.Comman;
import com.sultani.erfan.tourplannerserver.Interface.ItemClickListener;
import com.sultani.erfan.tourplannerserver.Model.Category;
import com.sultani.erfan.tourplannerserver.Model.Pack;
import com.sultani.erfan.tourplannerserver.ViewHolder.PackViewHolder;

import java.util.UUID;

import info.hoang8f.widget.FButton;

public class PackList extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;

    FloatingActionButton fab;

    String categoryId = "";

    FirebaseDatabase db;
    DatabaseReference packList;
    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseRecyclerAdapter<Pack,PackViewHolder> adapter;

    MaterialEditText editName,editDiscription,editDiscount,editPrice,editPlaceStay,editTranports,editFood,editDays,editTourpoint;

    FButton btnSelect,btnUpload;

    Pack newPack;

    Uri saveUri;

    ScrollView scrollView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack_list);

        db = FirebaseDatabase.getInstance();
        packList = db.getReference("Tour");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_pack);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Packages");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);


        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddPackDialoge();

            }
        });

        if (getIntent() != null)
           categoryId = getIntent().getStringExtra("CategoryId");
            if (!categoryId.isEmpty())
                loadListPack(categoryId);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void loadListPack(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Pack, PackViewHolder>(
                Pack.class,
                R.layout.pack_item,
                PackViewHolder.class,
                packList.orderByChild("menuId").equalTo(categoryId)

        ) {
            @Override
            protected void populateViewHolder(PackViewHolder viewHolder, Pack model, int position) {
                viewHolder.pack_name.setText(model.getName());
                Picasso.with(getBaseContext())
                        .load(model.getImage())
                        .into(viewHolder.pack_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                     //
                    }
                });


            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void showAddPackDialoge() {

        AlertDialog.Builder alertdialoge = new AlertDialog.Builder(PackList.this);
        alertdialoge.setTitle("ADD NEW PACKAGE");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_pack_pack,null);

        editName =add_menu_layout.findViewById(R.id.edtName);
        editDays = add_menu_layout.findViewById(R.id.edtDays);
        editDiscount = add_menu_layout.findViewById(R.id.edtDiscount);
        editDiscription = add_menu_layout.findViewById(R.id.edtDiscription);
        editPrice = add_menu_layout.findViewById(R.id.edtPrice);
        editFood = add_menu_layout.findViewById(R.id.edtFood);
        editPlaceStay = add_menu_layout.findViewById(R.id.edtPlaceStay);
        editTranports = add_menu_layout.findViewById(R.id.edtTransport);
        editTourpoint = add_menu_layout.findViewById(R.id.edtTourPoint);


        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload =add_menu_layout.findViewById(R.id.btnUpload);

        scrollView = add_menu_layout.findViewById(R.id.sc_top_down);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();//user select image from gallery
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        alertdialoge.setView(add_menu_layout);
        alertdialoge.setIcon(R.drawable.ic_packages);

        alertdialoge.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // in here we create a new categoty

                if (newPack != null){

                    packList.push().setValue(newPack);
                    Snackbar.make(rootLayout,"New Package"+" "+newPack.getName()+" "+"was add",Snackbar.LENGTH_SHORT).show();
                }


            }
        });
        alertdialoge.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();
            }
        });

        alertdialoge.show();

    }


    private void uploadImage() {

        if (saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("image/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mDialog.dismiss();
                    Toast.makeText(PackList.this,"Uploaded!!!",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            newPack = new Pack();
                            newPack.setName(editName.getText().toString());
                            newPack.setDays(editDays.getText().toString());
                            newPack.setDiscount(editDiscount.getText().toString());
                            newPack.setDescription(editDiscription.getText().toString());
                            newPack.setPlace(editPlaceStay.getText().toString());
                            newPack.setFoods(editFood.getText().toString());
                            newPack.setTransport(editTranports.getText().toString());
                            newPack.setPrice(editPrice.getText().toString());
                            newPack.setTourpoints(editTourpoint.getText().toString());
                            newPack.setMenuId(categoryId);
                            newPack.setImage(uri.toString());

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(PackList.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                    mDialog.setMessage("Uploaded"+progress+"%");
                }
            });



        }

    }

    private void chooseImage() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Comman.PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Comman.PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Image Selected");



        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Comman.UPDATE))
        {
            showUpdatePackDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));

        }
        else  if (item.getTitle().equals(Comman.DELETE))
        {

                deletePack(adapter.getRef(item.getOrder()).getKey());

        }

        return super.onContextItemSelected(item);
    }


    //delete item method
    private void deletePack(String key) {


        packList.child(key).removeValue();
    }

    private void showUpdatePackDialog(final String key, final Pack item) {
        AlertDialog.Builder alertdialoge = new AlertDialog.Builder(PackList.this);
        alertdialoge.setTitle("UPDATE PACKAGE");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_pack_pack,null);

        editName =add_menu_layout.findViewById(R.id.edtName);
        editDays = add_menu_layout.findViewById(R.id.edtDays);
        editDiscount = add_menu_layout.findViewById(R.id.edtDiscount);
        editDiscription = add_menu_layout.findViewById(R.id.edtDiscription);
        editPrice = add_menu_layout.findViewById(R.id.edtPrice);
        editFood = add_menu_layout.findViewById(R.id.edtFood);
        editPlaceStay = add_menu_layout.findViewById(R.id.edtPlaceStay);
        editTranports = add_menu_layout.findViewById(R.id.edtTransport);
        editTourpoint = add_menu_layout.findViewById(R.id.edtTourPoint);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload =add_menu_layout.findViewById(R.id.btnUpload);
        scrollView = add_menu_layout.findViewById(R.id.sc_top_down);

        //set the default informations
        editName.setText(item.getName());
        editDays.setText(item.getDays());
        editDiscription.setText(item.getDescription());
        editPrice.setText(item.getPrice());
        editFood.setText(item.getFoods());
        editPlaceStay.setText(item.getPlace());
        editTranports.setText(item.getTransport());
        editTourpoint.setText(item.getTourpoints());
        editDiscount.setText(item.getDiscount());

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();//user select image from gallery
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alertdialoge.setView(add_menu_layout);
        alertdialoge.setIcon(R.drawable.ic_packages);

        alertdialoge.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // in here we create a new package

                    item.setName(editName.getText().toString());
                    item.setDays(editDays.getText().toString());
                    item.setDescription(editDiscription.getText().toString());
                    item.setPrice(editPrice.getText().toString());
                    item.setFoods(editFood.getText().toString());
                    item.setPlace(editPlaceStay.getText().toString());
                    item.setTransport(editTranports.getText().toString());
                    item.setTourpoints(editTourpoint.getText().toString());
                    item.setDiscount(editDiscount.getText().toString());

                    packList.child(key).setValue(item);
                    Snackbar.make(rootLayout,"Updated Package"+" "+item.getName()+" "+"was updated",Snackbar.LENGTH_SHORT).show();



            }
        });
        alertdialoge.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();
            }
        });

        alertdialoge.show();




    }

  // change image
    private void changeImage(final Pack item) {

        if (saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("image/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mDialog.dismiss();
                    Toast.makeText(PackList.this,"Uploaded!!!",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            item.setImage(uri.toString());

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(PackList.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded"+progress+"%");
                }
            });



        }





    }
}
