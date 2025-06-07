package com.infinityco.notebookcam.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infinityco.notebookcam.Object.Settings_values;
import com.infinityco.notebookcam.R;

public class TypafaceAdapter extends RecyclerView.Adapter<TypafaceAdapter.DataObjectHolder>{

    private Context context;
    private static int selected;

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static TypafaceAdapter.MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView CardView;
        TextView tvPreview;
        Vibrator vibrator;
        public DataObjectHolder(View itemView,Context context) {
            super(itemView);

            CardView = (CardView) itemView.findViewById(R.id.cvColor_back);
            tvPreview = (TextView) itemView.findViewById(R.id.tv_typaface_preview);
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

    public void setOnItemClickListener(TypafaceAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public int getColor(){
        return selected;
    }
    public TypafaceAdapter(Context context) {

        this.context = context;
        try {
            selected = new Settings_values(context).getTypeFace()-1;
        }
        catch (Exception e){
            selected = 0;
        }
    }

    @Override
    public TypafaceAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_typaface_option, parent, false);

        TypafaceAdapter.DataObjectHolder dataObjectHolder = new TypafaceAdapter.DataObjectHolder(view,context);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(TypafaceAdapter.DataObjectHolder holder, final int position) {

        if(position==selected)
            holder.CardView.setVisibility(View.VISIBLE);
        else
            holder.CardView.setVisibility(View.GONE);

        switch (position) {
            case 0:
                holder.tvPreview.setTypeface(Typeface.createFromAsset(context.getAssets(), "tittle_font.ttf"));
                break;
            case 1:
                holder.tvPreview.setTypeface(Typeface.createFromAsset(context.getAssets(), "DroidSans.ttf"));
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

