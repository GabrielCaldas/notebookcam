package com.infinityco.notebookcam.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infinityco.notebookcam.R;
import com.squareup.picasso.Picasso;

import net.alhazmy13.imagefilter.ImageFilter;

import java.util.ArrayList;

public class rvAdapterFilters extends RecyclerView.Adapter<rvAdapterFilters.DataObjectHolder>{

    private Context context;
    private ArrayList<Bitmap> filtersImg = new ArrayList<>();
    private ArrayList<String> filtersName = new ArrayList<>();
    private Bitmap original;

    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView filterImg;
        TextView filterName;


        public DataObjectHolder(final View itemView) {
            super(itemView);

            filterImg = (ImageView) itemView.findViewById(R.id.ivFilterImg);
            filterName = (TextView) itemView.findViewById(R.id.tvFilterName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public rvAdapterFilters(Context context, Bitmap bmp) {
        this.context = context;
        this.original = Bitmap.createBitmap(bmp);
        arraysInit(original);
    }
    private void arraysInit(Bitmap bmp){
        Bitmap baseBitmap = resize(bmp, 100, 100);

        for(int i=0;i<20;i++){
            if(i==0){
                filtersImg.add(Bitmap.createBitmap(baseBitmap));
                filtersName.add(context.getString(R.string.PhotoEditorFilterNone));
            }
            else if(i==1){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.GRAY));
                filtersName.add(context.getString(R.string.PhotoEditorFilterGRAY));
            }
            else if(i==2){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.RELIEF));
                filtersName.add(context.getString(R.string.PhotoEditorFilterRELIEF));
            }
            else if(i==3){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.AVERAGE_BLUR));
                filtersName.add(context.getString(R.string.PhotoEditorFilterAVERAGE_BLUR));
            }
            else if(i==4){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.NEON));
                filtersName.add(context.getString(R.string.PhotoEditorFilterNEON));
            }
            else if(i==5){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.PIXELATE));
                filtersName.add(context.getString(R.string.PhotoEditorFilterPIXELATE));
            }
            else if(i==6){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.TV));
                filtersName.add(context.getString(R.string.PhotoEditorFilterTV));
            }
            else if(i==7){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.INVERT));
                filtersName.add(context.getString(R.string.PhotoEditorFilterINVERT));
            }
            else  if(i==8){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.BLOCK));
                filtersName.add(context.getString(R.string.PhotoEditorFilterBLOCK));
            }
            else if(i==9){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.OLD));
                filtersName.add(context.getString(R.string.PhotoEditorFilterOLD));
            }
            else if(i==10){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.LIGHT));
                filtersName.add(context.getString(R.string.PhotoEditorFilterLIGHT));
            }
            else if(i==11){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.LOMO));
                filtersName.add(context.getString(R.string.PhotoEditorFilterLOMO));
            }
            else if(i==12){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.HDR));
                filtersName.add(context.getString(R.string.PhotoEditorFilterHDR));
            }
            else if(i==13){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.SOFT_GLOW));
                filtersName.add(context.getString(R.string.PhotoEditorFilterSOFT_GLOW));
            }
            else if(i==14){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.SKETCH));
                filtersName.add(context.getString(R.string.PhotoEditorFilterSKETCH));
            }
            else if(i==15){
               filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.GOTHAM));
                filtersName.add(context.getString(R.string.PhotoEditorFilterGOTHAM));
            }
            else if(i==16){
                filtersImg.add(ImageFilter.applyFilter(baseBitmap, ImageFilter.Filter.GAUSSIAN_BLUR));
                filtersName.add(context.getString(R.string.PhotoEditorFilterGAUSSIANBLUR));
            }
        }
    }

    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, false);
            return image;
        } else {
            return image;
        }
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_filter, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }


    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {

        holder.filterImg.setImageBitmap(filtersImg.get(position));
        holder.filterName.setText(filtersName.get(position));

    }

    @Override
    public int getItemCount() {

        return filtersImg.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}

