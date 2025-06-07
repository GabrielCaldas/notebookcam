package com.infinityco.notebookcam.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infinityco.notebookcam.R;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.DataObjectHolder>{



    private Context context;
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static OptionAdapter.MyClickListener myClickListener;
    private Typeface tittle_type;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView optionName;
        ViewPager optionVP;

        public DataObjectHolder(View itemView, Typeface typeface) {
            super(itemView);

            optionName = (TextView) itemView.findViewById(R.id.tvOptinoName);
            optionVP= (ViewPager) itemView.findViewById(R.id.vpOption);
            optionName.setTypeface(typeface);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(OptionAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public OptionAdapter(Context context,Typeface tittle_type) {
        this.context = context;
        this.tittle_type = tittle_type;
    }


    @Override
    public OptionAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_option, parent, false);

        OptionAdapter.DataObjectHolder dataObjectHolder = new OptionAdapter.DataObjectHolder(view,tittle_type);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(OptionAdapter.DataObjectHolder holder, final int position) {

        switch (position){
            case 0:
                holder.optionName.setText(R.string.Font);
                holder.optionVP.setAdapter(new OptionTypefaceViewPageAdapter(context));
                break;
            case 1:
                holder.optionName.setText(R.string.AppColor);
                holder.optionVP.setAdapter(new OptionColorViewPageAdapter(context));
                break;
        }


    }

    public void UpDateViews(){
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return 2;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}

