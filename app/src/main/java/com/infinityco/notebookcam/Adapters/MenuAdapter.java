package com.infinityco.notebookcam.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinityco.notebookcam.Object.FolderList;
import com.infinityco.notebookcam.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.DataObjectHolder>{



    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static MenuAdapter.MyClickListener myClickListener;
    private Typeface tittle_type;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView menuName;
        ImageView menuIcon;

        public DataObjectHolder(View itemView, Typeface typeface) {
            super(itemView);

            menuName = (TextView) itemView.findViewById(R.id.tvMenuName);
            menuIcon= (ImageView) itemView.findViewById(R.id.ivMenuIcon);
            menuName.setTypeface(typeface);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MenuAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MenuAdapter(Typeface tittle_type) {

        this.tittle_type = tittle_type;
    }


    @Override
    public MenuAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_menu, parent, false);

        MenuAdapter.DataObjectHolder dataObjectHolder = new MenuAdapter.DataObjectHolder(view,tittle_type);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(MenuAdapter.DataObjectHolder holder, final int position) {

        switch (position){
            case 0:
                holder.menuIcon.setBackgroundResource(R.drawable.settings);
                holder.menuName.setText(R.string.Settings);
                break;
            case 1:
                holder.menuIcon.setBackgroundResource(R.drawable.tutorial);
                holder.menuName.setText(R.string.Tutorial);
                break;
            case 2:
                holder.menuIcon.setBackgroundResource(R.drawable.discovery);
                holder.menuName.setText(R.string.Descovery);
                break;
        }


    }

    public void UpDateViews(){
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return 3;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}

