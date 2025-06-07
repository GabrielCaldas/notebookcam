package com.infinityco.notebookcam.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinityco.notebookcam.Object.SubjectList;
import com.infinityco.notebookcam.R;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.DataObjectHolder>{

    private SubjectList subjects;
    private Context context;
    private int folderColor;

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static SubjectAdapter.MyClickListener myClickListener;
    private Typeface tittle_type;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView subjectName;
        ImageView folderBarColor;

        public DataObjectHolder(View itemView, Typeface typeface,int folderColor) {
            super(itemView);

            folderBarColor = (ImageView) itemView.findViewById(R.id.ivFolderBarColor);
            subjectName = (TextView) itemView.findViewById(R.id.tvSubjectName);
            subjectName.setTypeface(typeface);

            folderBarColor.setBackgroundColor(folderColor);
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

    public void setOnItemClickListener(SubjectAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public SubjectAdapter(Context context, SubjectList subjects, int color, Typeface tittle_type) {

        this.context = context;
        this.subjects = subjects;
        this.folderColor = color;

        this.tittle_type = tittle_type;
    }

    @Override
    public SubjectAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_subject, parent, false);

        SubjectAdapter.DataObjectHolder dataObjectHolder = new SubjectAdapter.DataObjectHolder(view,tittle_type,folderColor);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(SubjectAdapter.DataObjectHolder holder, final int position) {

        holder.subjectName.setText(subjects.get(position).getSubjectName());

    }

    public void UpDateViews(){
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
        public void onLongClick(int position, View v);
    }
}

