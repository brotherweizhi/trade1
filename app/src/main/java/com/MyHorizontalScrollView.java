package com;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.activity.ItemPicPreview;
import com.bean.Pic;
import com.example.trade.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的水平滚动视图,以Gallery的形式显示多张水平图片
 * @author Administrator
 *
 */
public class MyHorizontalScrollView extends HorizontalScrollView{

    //屏幕的宽高度,图片的宽度,每屏显示几张图,适配器,线性布局,当前可视区域内第一张和最后一张图片的下标
    int mScreenWidth, mScreenHeight;
    int mImageWidth, mImageHeight;
    int mMaxCount;
    HorizontalScrollViewAdapter mAdapter;
    LinearLayout mContainer;
    int mFirstPos, mLastPos;
    public static final String TAG = "自定义的滚动视图";

    //首先应该获取屏幕的宽高度，以及线性布局的实例化
    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
    }

    //初始化适配器的函数，并且计算每屏可以显示几张图
    public void initData(HorizontalScrollViewAdapter adapter){
        this.mAdapter = adapter;
        View view = adapter.getView(0, null, null);
        mContainer = (LinearLayout)getChildAt(0);
        mContainer.addView(view);
        mMaxCount = 5;

        /*if(mImageWidth==0 && mImageHeight==0){
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            mImageWidth = view.getMeasuredWidth();
            mImageHeight = view.getMeasuredHeight();
            mMaxCount = 4;//+1为多出一个位置以让滚动视图能得到scrollX

            Log.i(TAG, "mImageWidth:"+mImageWidth+"-"+mScreenWidth+"-"+"mMaxCount"+mMaxCount);
        }*/

        //初始化第一屏的前mMaxCount张图片
        if(adapter.getCount()>1) {
            mContainer.removeAllViews();
            for (int i = 0; i < mMaxCount; i++) {
                if(i<adapter.getCount()){
                    View v = adapter.getView(i, null, null);
                    mContainer.addView(v);
                    mLastPos = i;
                }
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int scrollX = getScrollX();//获取水平滚动的位置,参照原先view的原点
                //Log.i(TAG, "scrollX="+scrollX);
                if(scrollX>=mImageWidth) {
                    //加载下一张图
                    nextImage();
                }
                if(scrollX==0) {
                    //加载上一张图
                    previousImage();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    //加载下一张图
    private void nextImage(){
        if(mLastPos==mAdapter.getCount()-1) {
            return;
        }
        //先移除可视区域内第一张图片，然后把替补队员加到线性布局来
        scrollTo(0, 0);
        mContainer.removeViewAt(0);
        mContainer.addView(mAdapter.getView(mLastPos+1, null, null));
        mFirstPos++;//原先首位置和末位置的图片下标++
        mLastPos++;

    }

    //加载上一张图
    private void previousImage(){
        if(mFirstPos==0){
            return;
        }
        //先移除最末尾一张，然后把替补队员加到线性布局最前面
        mContainer.removeViewAt(mContainer.getChildCount()-1);
        mContainer.addView(mAdapter.getView(mFirstPos-1, null, null), 0);
        scrollTo(mImageWidth, 0);
        mFirstPos--;
        mLastPos--;
    }

    /**
     * 水平滚动视图的适配器
     * @author Administrator
     *
     */
    public class HorizontalScrollViewAdapter {

        private List<Pic> mData;
        private Context mContext;
        private LayoutInflater mLayoutInflator;
        private ArrayList<String> listBig;

        public HorizontalScrollViewAdapter(Context context, List<Pic> data , List<Pic> picBigList){
            this.mContext = context;
            this.mData = data;
            listBig=new ArrayList<>();
            for (Pic pic:picBigList){
                listBig.add(pic.getUrlBig());
            }

            /*//获取所有大图URL
            for(Pic pic:dataBig){
                picBigList.add(pic.getUrlBig());
            }*/
            this.mLayoutInflator = LayoutInflater.from(mContext);
        }

        public List<Pic> getData() {
            return mData;
        }

        public int getCount() {
            return mData.size();
        }


        public Object getItem(int arg0) {
            return null;
        }


        public long getItemId(int arg0) {
            return 0;
        }


        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = mLayoutInflator.inflate(R.layout.sub_hsv_item, null);

                viewHolder.ivPic = convertView.findViewById(R.id.ivPic);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //viewHolder.ivPic.setImageResource(mData.get(position));

            /*LayoutParams params = new LayoutParams(720 / 4, 720 / 4);
            viewHolder.ivPic.setLayoutParams(params);*/
            ImageLoader.getInstance().displayImage(mData.get(position).getUrl(), viewHolder.ivPic);
            viewHolder.ivPic.setOnClickListener(arg0 -> {
                //把当前你点击的图片位置传递到下一个界面
                /*Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_SHORT).show();*/
                Intent it = new Intent(mContext, ItemPicPreview.class);
                Bundle extras = new Bundle();
                extras.putInt("position", position);//传递位置，还有大图的数据集合
                extras.putStringArrayList("pic", listBig);
                it.putExtras(extras);
                mContext.startActivity(it);
            });
            return convertView;
        }

        class ViewHolder{
            ImageView ivPic;
        }
    }
}