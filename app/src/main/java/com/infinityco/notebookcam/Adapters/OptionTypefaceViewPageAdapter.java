package com.infinityco.notebookcam.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.infinityco.notebookcam.Activity.MainActivity;
import com.infinityco.notebookcam.Object.Settings_values;
import com.infinityco.notebookcam.R;


public class OptionTypefaceViewPageAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;


    private RecyclerView rv;

    public OptionTypefaceViewPageAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.appcolor_vpfragment, container, false);

        rv = (RecyclerView) view.findViewById(R.id.rvAppColors);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(new TypafaceAdapter(mContext));


        ((TypafaceAdapter) rv.getAdapter()).setOnItemClickListener(new TypafaceAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                new Settings_values(mContext).setTypeFace(position +1+"");
                rv.getAdapter().notifyDataSetChanged();
                MainActivity.reloadUI = true;
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