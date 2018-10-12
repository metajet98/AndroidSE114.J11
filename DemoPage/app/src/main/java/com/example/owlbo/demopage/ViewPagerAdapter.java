package com.example.owlbo.demopage;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    public Integer [] images = {R.drawable.image_1,R.drawable.image_2,R.drawable.image_3,R.drawable.image_4};
    public String[]list_title={"Title 1","Title 2","Title 3","Title 4"};
    public String[] list_des = {"Desciption 1","Desciption 2","Desciption 3","Desciption 4"};
    public int[] list_background={
            Color.rgb(55,55,55),
            Color.rgb(239,85,85),
            Color.rgb(110,49,89),
            Color.rgb(1,188,212)
    };

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }
    public LinearLayout linearLayout;
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imgView);
        TextView txt_Title = (TextView) view.findViewById(R.id.txtTitle);
        TextView txt_Des = (TextView) view.findViewById(R.id.txtDescription);
        linearLayout = (LinearLayout)view.findViewById(R.id.custom);

        linearLayout.setBackgroundColor(list_background[position]);
        imageView.setImageResource(images[position]);
        txt_Title.setText(list_title[position]);
        txt_Des.setText(list_des[position]);


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
