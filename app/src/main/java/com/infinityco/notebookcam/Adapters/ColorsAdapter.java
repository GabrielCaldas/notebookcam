package com.infinityco.notebookcam.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinityco.notebookcam.Object.FolderList;
import com.infinityco.notebookcam.R;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.DataObjectHolder>{

    private Context context;
    private static int selected;

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static ColorsAdapter.MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cvColor,CardView;
        Vibrator vibrator;
        public DataObjectHolder(View itemView,Context context) {
            super(itemView);

            CardView = (CardView) itemView.findViewById(R.id.cvColor_back);
            cvColor = (CardView) itemView.findViewById(R.id.cvColor);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            selected = getAdapterPosition();
            myClickListener.onItemClick(getAdapterPosition(), v);
            vibrator.vibrate(35);
        }
    }

    public void setOnItemClickListener(ColorsAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public int getColor(){
        return selected;
    }
    public ColorsAdapter(Context context) {

        this.context = context;
        selected= 0;
    }

    public ColorsAdapter(Context context,int color) {

        this.context = context;
        this.selected = color;
    }


    @Override
    public ColorsAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_folder_colors, parent, false);

        ColorsAdapter.DataObjectHolder dataObjectHolder = new ColorsAdapter.DataObjectHolder(view,context);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(ColorsAdapter.DataObjectHolder holder, final int position) {

        if(position==selected)
            holder.CardView.setVisibility(View.VISIBLE);
        else
            holder.CardView.setVisibility(View.GONE);

        switch (position){
            case 0:
                holder.cvColor.setCardBackgroundColor(context.getResources().getColor(R.color.folderColor1));
                break;
            case 1:
                holder.cvColor.setCardBackgroundColor(context.getResources().getColor(R.color.folderColor2));
                break;
            case 2:
                holder.cvColor.setCardBackgroundColor(context.getResources().getColor(R.color.folderColor3));
                break;
            case 3:
                holder.cvColor.setCardBackgroundColor(context.getResources().getColor(R.color.folderColor4));
                break;
            case 4:
                holder.cvColor.setCardBackgroundColor(context.getResources().getColor(R.color.folderColor5));
                break;
            case 5:
                holder.cvColor.setCardBackgroundColor(context.getResources().getColor(R.color.folderColor6));
                break;
        }
    }

    public void UpDateViews(){
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return 6;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}

