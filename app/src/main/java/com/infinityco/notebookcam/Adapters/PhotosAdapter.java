package com.infinityco.notebookcam.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infinityco.notebookcam.Activity.MainActivity;
import com.infinityco.notebookcam.Object.Photo;
import com.infinityco.notebookcam.Object.PhotoList;
import com.infinityco.notebookcam.R;

import java.io.File;
import java.util.ArrayList;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.DataObjectHolder>{

    private PhotoList fotos;
    private Context context;

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static PhotosAdapter.MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView fotoName;
        ImageView foto;


        public DataObjectHolder(final View itemView, Typeface tf) {
            super(itemView);

            fotoName = (TextView) itemView.findViewById(R.id.tvFotoInfo);
            foto = (ImageView) itemView.findViewById(R.id.ivFoto);
            itemView.setOnClickListener(this);
            fotoName.setTypeface(tf);
            /*try {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        myClickListener.onLongClick(getAdapterPosition(), itemView);
                        return true;
                    }
                });
            }
            catch (Exception e){}*/
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(PhotosAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public PhotosAdapter(Context context, PhotoList fotos) {
        this.context = context;
        this.fotos = fotos;

    }

    public PhotoList getPhotos(){
        return fotos;
    }


    @Override
    public PhotosAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_foto, parent, false);

        PhotosAdapter.DataObjectHolder dataObjectHolder = new PhotosAdapter.DataObjectHolder(view, MainActivity.getTyperFace());
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(PhotosAdapter.DataObjectHolder holder, final int position) {


        //Uri imageUri = Uri.fromFile(new File(fotos.get(position).getPath()));

        Glide.with(context).load(fotos.get(position).getPath()).into(holder.foto);

        //holder.foto.setImageBitmap(BitmapFactory.decodeFile(fotos.get(position).getPath()));
        holder.fotoName.setText(position+1+"");

    }

    public void UpdateViews(PhotoList photoList){
        fotos = photoList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
       // public void onLongClick(int position,View v);
    }
}

