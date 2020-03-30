package com.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.trade.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ItemPicPreview extends Activity {

    ViewPager mViewPager;
    TextView mtvCurrent,mtvTotal;
    int mCurrentPosition;
    //List<ImageView> mData;
    ArrayList<String> mPics;  //图片url集合

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_pic_preview);

        Intent it=getIntent();
        mCurrentPosition=it.getIntExtra("position",0);
        mPics =it.getStringArrayListExtra("pic");
        //mData=initData(mPics);
        initUI();
    }

    private void initUI(){
        mViewPager=findViewById(R.id.viewPager);
        mtvCurrent=findViewById(R.id.tvCurrent);
        mtvCurrent.setText(String.valueOf(mCurrentPosition+1));
        mtvTotal=findViewById(R.id.tvTotalCount);
        mtvTotal.setText(String.valueOf(mPics.size()));

        mViewPager.setAdapter(new PicViewPagerAdater(mPics ,this));

        mViewPager.setCurrentItem(mCurrentPosition); //自己加的

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition=position;
                mtvCurrent.setText(String.valueOf(position+1));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private  class PicViewPagerAdater extends PagerAdapter{

        List<String> mData;
        Context mContext;

        public PicViewPagerAdater(List<String> mData, Context mContext) {
            this.mData = mData;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeViewAt(position);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView iv=new ImageView(mContext);
            ImageLoader.getInstance().displayImage(mData.get(position),iv);
            container.addView(iv);
            return iv;
        }
    }

    private List<ImageView> initData(ArrayList<Integer> mPics){
        List<ImageView> data=new ArrayList<>();
        /*Resources res=getResources();
        data.add(toImageView(R.mipmap.pic1));
        data.add(toImageView(R.mipmap.pic2));
        data.add(toImageView(R.mipmap.pic3));
        data.add(toImageView(R.mipmap.pic4));
        data.add(toImageView(R.mipmap.pic5));*/

        for (Integer resID:mPics){
            data.add(toImageView(resID));
        }
        return  data;
    }
    private ImageView toImageView(int resID){
        ImageView iv=new ImageView(this);
        iv.setImageResource(resID);
        return iv;
    }
}
