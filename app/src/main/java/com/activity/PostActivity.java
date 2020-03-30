package com.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.CommonBaseAdapter;
import com.CommonViewHolder;
import com.bean.ImageBean;
import com.example.trade.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.MyApplication.initImageLoader;

public class PostActivity extends BaseActivity {

    List<ImageBean> mList=new ArrayList<>();//保存图片路劲
    List<ImageBean> mListSelect=new ArrayList<>();
    GridView mGridView;
    TextView testbutton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initImageLoader(getApplicationContext());
        super.onCreate(savedInstanceState);
        //判断登陆状态

        setContentView(R.layout.activity_post_1);;
        queryLocalImage();
        initUI();
        testbutton=findViewById(R.id.btSelectPic);
        testbutton.setOnClickListener(v -> {
            Intent it=new Intent();
            Bundle extras=new Bundle();
            ArrayList<String> value=new ArrayList<>();
            for (ImageBean bean:mListSelect){
                value.add(bean.getPicUri());
            }
            extras.putStringArrayList("data",value);
            it.putExtras(extras);
            it.setClass(PostActivity.this,Post2Activity.class);
            startActivity(it);
            finish();
        });
    }
    public void initUI(){
        mGridView=findViewById(R.id.postGridView);
        mGridView.setAdapter(new CommonBaseAdapter<ImageBean>(this,mList,R.layout.sub_post_gridview) {
            @Override
            protected void convert(CommonViewHolder viewHolder, ImageBean imageBean) {
                if (viewHolder.getPosition()==0){
                    viewHolder.setImageResource(R.id.ivPostPic,imageBean.getPicResID(),1,4);
                    viewHolder.getView(R.id.selectIcon).setVisibility(View.GONE);
                }
                else {
                    viewHolder.setImageResource(R.id.ivPostPic, imageBean.getPicUri(), 4);
                    viewHolder.getView(R.id.selectIcon).setOnClickListener(v -> {
                        ImageView icon = (ImageView) v;
                        if (!imageBean.isSelected()) {
                            imageBean.setSelected(true);
                            icon.setImageResource(R.mipmap.check_true);
                            System.out.println(viewHolder.getPosition()+imageBean.getPicUri());
                            mListSelect.add(imageBean);
                        } else {
                            imageBean.setSelected(false);
                            icon.setImageResource(R.mipmap.check_false);
                            mListSelect.remove(imageBean);
                        }
                    });
                }
            }
        });
    }

    private List<ImageBean> initData(){
        List<ImageBean> list=new ArrayList<>();
        list.add(new ImageBean(R.mipmap.photo_icon_camera));


        list.add(new ImageBean(R.drawable.pic5));
        list.add(new ImageBean(R.drawable.pic1));
        list.add(new ImageBean(R.drawable.pic2));
        list.add(new ImageBean(R.drawable.pic3));
        list.add(new ImageBean(R.drawable.pic4));
        list.add(new ImageBean(R.drawable.pic3));
        return list;
    }

    private void queryLocalImage(){
        Uri uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor=getContentResolver().query(uri,null, MediaStore.Images.Media.MIME_TYPE+"=? or "+MediaStore.Images.Media.MIME_TYPE+"=?",new String[]{"image/png","image/jpeg"},MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor==null){
            return;
        }
        while (cursor.moveToNext()){
            String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            File file=new File(path);
            if (file.exists()) {
                mList.add(new ImageBean(path));
            }
        }
        mList.add(0,new ImageBean(R.mipmap.photo_icon_camera));
    }



}
