package com.infinityco.notebookcam.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinityco.notebookcam.Object.FolderList;
import com.infinityco.notebookcam.R;

import java.util.List;

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.DataObjectHolder>{

    private FolderList folders;
    private Context context;

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static FoldersAdapter.MyClickListener myClickListener;
    private Typeface tittle_type;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView folderName;
        ImageView folderColorBar,folderColor;

        public DataObjectHolder(View itemView, Typeface typeface) {
            super(itemView);

            folderName = (TextView) itemView.findViewById(R.id.tvFolderName);
            folderColorBar = (ImageView) itemView.findViewById(R.id.ivFolderBarColor);
            folderColor = (ImageView) itemView.findViewById(R.id.ivFolderColor);
            folderName.setTypeface(typeface);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    myClickListener.onLongClick(getAdapterPosition(), v);
                    return false;
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(FoldersAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public FoldersAdapter(Context context, FolderList folders, Typeface tittle_type) {

        this.context = context;
        this.folders = folders;

        this.tittle_type = tittle_type;
    }



    @Override
    public FoldersAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_folder, parent, false);

        FoldersAdapter.DataObjectHolder dataObjectHolder = new FoldersAdapter.DataObjectHolder(view,tittle_type);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(FoldersAdapter.DataObjectHolder holder, final int position) {

        switch (folders.get(position).getColor()){
            case 1:
                holder.folderColorBar.setBackgroundColor(context.getResources().getColor(R.color.folderColor1));
                holder.folderColor.setBackgroundColor(context.getResources().getColor(R.color.folderColor1));
                break;
            case 2:
                holder.folderColorBar.setBackgroundColor(context.getResources().getColor(R.color.folderColor2));
                holder.folderColor.setBackgroundColor(context.getResources().getColor(R.color.folderColor2));
                break;
            case 3:
                holder.folderColorBar.setBackgroundColor(context.getResources().getColor(R.color.folderColor3));
                holder.folderColor.setBackgroundColor(context.getResources().getColor(R.color.folderColor3));
                break;
            case 4:
                holder.folderColorBar.setBackgroundColor(context.getResources().getColor(R.color.folderColor4));
                holder.folderColor.setBackgroundColor(context.getResources().getColor(R.color.folderColor4));
                break;
            case 5:
                holder.folderColorBar.setBackgroundColor(context.getResources().getColor(R.color.folderColor5));
                holder.folderColor.setBackgroundColor(context.getResources().getColor(R.color.folderColor5));
                break;
            case 6:
                holder.folderColorBar.setBackgroundColor(context.getResources().getColor(R.color.folderColor6));
                holder.folderColor.setBackgroundColor(context.getResources().getColor(R.color.folderColor6));
                break;
        }

        holder.folderName.setText(folders.get(position).getFolderName());

    }

    public void UpDateViews(){
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return folders.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
        public void onLongClick(int position, View v);
    }
}

