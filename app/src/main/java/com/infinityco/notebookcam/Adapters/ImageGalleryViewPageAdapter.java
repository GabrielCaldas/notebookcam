package com.infinityco.notebookcam.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.infinityco.notebookcam.Helpers.PhotoView;
import com.infinityco.notebookcam.Helpers.PhotoViewAttacher;
import com.infinityco.notebookcam.Object.PhotoList;
import com.infinityco.notebookcam.R;

/**
 * Created by gabrielcaldas on 06/10/16.
 */

public class ImageGalleryViewPageAdapter extends PagerAdapter{

    Context mContext;
    LayoutInflater mLayoutInflater;
    RelativeLayout infoLayout,buttons;
    private PhotoList photoList;


    private PhotoView Image;
    public ImageGalleryViewPageAdapter(Context context, PhotoList photoList,RelativeLayout infoLayout,RelativeLayout buttons) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.photoList = photoList;
        this.buttons = buttons;
        this.infoLayout = infoLayout;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.image_gallery_item, container, false);

        Image = (PhotoView) view.findViewById(R.id.ivImage);
        Glide.with(mContext).load(photoList.get(position).getPath()).into(Image);
        //Image.setImageBitmap(BitmapFactory.decodeFile(photoList.get(position).getPath()));
        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttons.getVisibility()==View.VISIBLE) {
                    buttons.setVisibility(View.GONE);
                    infoLayout.setVisibility(View.GONE);
                }
                else{
                    buttons.setVisibility(View.VISIBLE);
                    infoLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}