package com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.Nullable;

import com.bean.ImageBean;
import com.example.trade.R;

import java.util.ArrayList;
import java.util.List;


public class test extends Activity {

    ImageBean imageBean;
    List<ImageBean> mList=new ArrayList<>();//保存图片路劲
    GridView gridView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /*mList=postActivity.mListSelect;*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_2);
        /*gridView=findViewById(R.id.testgrid);
        gridView.setAdapter(new CommonBaseAdapter<ImageBean>(this,imageBean.getmListSelect(),R.layout.sub_post_gridview) {
            @Override
            protected void convert(CommonViewHolder viewHolder, ImageBean imageBean) {
                viewHolder.setImageResource(R.id.ivPostPic,imageBean.getPicResID());
            }

        });*/
    }
}
