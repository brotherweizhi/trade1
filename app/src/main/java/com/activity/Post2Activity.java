package com.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.APIInterface;
import com.CommonBaseAdapter;
import com.CommonViewHolder;
import com.HttpUtils;
import com.MyApplication;
import com.bean.ImageBean;
import com.example.trade.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.MyApplication.initImageLoader;

public class Post2Activity extends Activity {

    List<ImageBean> mListSelect=new ArrayList<>();
    GridView mGridViewSelected;
    CommonBaseAdapter mSelectAdaptor;
    List<String> urls;
    PopupWindow mpopupCityWindow,mpopupCateWindow;
    TextView mCityTextview,mCateTextview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initImageLoader(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_2);

        //多张图片上传
        ArrayList<String> data=getIntent().getExtras().getStringArrayList("data");
        File[] files=new File[data.size()];
        for (int i=0;i<files.length;i++){
            mListSelect.add(new ImageBean(data.get(i)));
            files[i]=new File(data.get(i));
        }
        RequestParams params=new RequestParams();
        try {
            params.put("pic[]",files);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HttpUtils.post(APIInterface.UPLOAD,params,handler);
        initUI();
    }

    //activity获得焦点（加载完毕）后才可加载popupwindow
    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            initWindow();
        }
    }*/

    private void initCityWindow(){
        ArrayList<HashMap<String,String>> data= new ArrayList<>();
        HashMap<String, String> map1=new HashMap<>();
        HashMap<String, String> map2=new HashMap<>();
        HashMap<String, String> map3=new HashMap<>();
        map1.put("name","广东工业大学");
        map1.put("id","1");
        map2.put("name","星海音乐学院");
        map2.put("id","2");
        map3.put("name","广州大学");
        map3.put("id","3");
        data.add(map1);
        data.add(map2); data.add(map3);

        View contentView= LayoutInflater.from(this).inflate(R.layout.popup_listview,null);
        contentView.setOnClickListener(v -> {
            if (mpopupCityWindow.isShowing())
                mpopupCityWindow.dismiss();
        });
        ListView listView=contentView.findViewById(R.id.ListView);

        //点击弹出窗
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            mCityTextview.setText(data.get(position).get("name"));
            if (mpopupCityWindow.isShowing()) {
                mpopupCityWindow.dismiss();
            }

        });

        listView.setAdapter(new SimpleAdapter(this,data,R.layout.sub_tab_sort,new String[]{"name"},new int[]{R.id.name}));
        mpopupCityWindow =new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mpopupCityWindow.setBackgroundDrawable(new BitmapDrawable());
        mpopupCityWindow.setOutsideTouchable(true);
        mpopupCityWindow.setAnimationStyle(0);
        mpopupCityWindow.setFocusable(true);
        mpopupCityWindow.showAsDropDown(mCityTextview);

    }
    private void initCateWindow(){
        ArrayList<HashMap<String,String>> data= new ArrayList<>();
        HashMap<String, String> map1=new HashMap<>();
        HashMap<String, String> map2=new HashMap<>();
        HashMap<String, String> map3=new HashMap<>();
        HashMap<String, String> map4=new HashMap<>();
        map1.put("name","手机");
        map1.put("id","1");
        map2.put("name","服装");
        map2.put("id","2");
        map3.put("name","书籍");
        map3.put("id","3");
        map4.put("name","其他");
        map4.put("id","4");
        data.add(map1);
        data.add(map2); data.add(map3);data.add(map4);

        View contentView= LayoutInflater.from(this).inflate(R.layout.popup_listview,null);
        contentView.setOnClickListener(v -> {
            if (mpopupCateWindow.isShowing())
                mpopupCateWindow.dismiss();
        });
        ListView listView=contentView.findViewById(R.id.ListView);

        //点击弹出窗
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            mCateTextview.setText(data.get(position).get("name"));
            if (mpopupCateWindow.isShowing()) {
                mpopupCateWindow.dismiss();
            }

        });

        listView.setAdapter(new SimpleAdapter(this,data,R.layout.sub_tab_sort,new String[]{"name"},new int[]{R.id.name}));
        mpopupCateWindow =new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mpopupCateWindow.setBackgroundDrawable(new BitmapDrawable());
        mpopupCateWindow.setOutsideTouchable(true);
        mpopupCateWindow.setAnimationStyle(0);
        mpopupCateWindow.setFocusable(true);
        mpopupCateWindow.showAsDropDown(mCateTextview);

    }



    String strhead=APIInterface.IMAGE_DIR;
    private JsonHttpResponseHandler handler=new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            String str = null;
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getInt("code")==200){
                    String fileName=response.getString("data");
                    urls=new ArrayList<>();
                    String[] arr=fileName.split(",");
                    for (String string:arr)
                        urls.add(strhead+string);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            System.out.println("fail"+throwable.getMessage());
        }
    };
    public void initUI(){
        mGridViewSelected=findViewById(R.id.gridViewSelect);
        mCityTextview=findViewById(R.id.sendCity);
        mCateTextview=findViewById(R.id.tvPostCate);
        mCityTextview.setOnClickListener(v -> initCityWindow());
        mCateTextview.setOnClickListener(v -> initCateWindow());
        mSelectAdaptor=new CommonBaseAdapter<ImageBean>(this,mListSelect,R.layout.sub_post_gridview) {
            @Override
            protected void convert(CommonViewHolder viewHolder, ImageBean imageBean) {
                    viewHolder.setImageResource(R.id.ivPostPic,imageBean.getPicUri(),4);
                    viewHolder.getView(R.id.selectIcon).setOnClickListener(v -> {
                        mListSelect.remove(imageBean);
                        mSelectAdaptor.notifyDataSetChanged();
                    });
            }
        };
        mGridViewSelected.setAdapter(mSelectAdaptor);
        findViewById(R.id.btnPub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params=new RequestParams();
                params.put("title",((EditText)(findViewById(R.id.etTitle))).getText());
                params.put("content",((EditText)(findViewById(R.id.etContent))).getText());
                params.put("price",((EditText)(findViewById(R.id.etCurrentPrice))).getText());
                params.put("uid", MyApplication.getUser().getUid());
                params.put("username",MyApplication.getUser().getUsername());
                switch (mCateTextview.getText().toString()){
                    case "手机":
                        params.put("cateId1",1);
                        break;
                    case "书籍":
                        params.put("cateId1",3);
                        break;
                    case "服装":
                        params.put("cateId1",2);
                        break;
                    case "其他":
                        params.put("cateId1",4);
                        break;
                }
                //params.put("cateId2",6);
                params.put("urls",urls);
                switch (mCityTextview.getText().toString()){
                    case "广东工业大学":
                        params.put("city",1);
                        break;
                    case "星海音乐学院":
                        params.put("city",2);
                        break;
                    case "广州大学":
                        params.put("city",3);
                        break;
                }
                if (urls.size()>0)
                    params.put("pic",urls.get(0));
                HttpUtils.post(APIInterface.ITEM_POST,params,posthandler);
            }
        });

    }
    //发布商品handelr
    private JsonHttpResponseHandler posthandler=new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            System.out.println("fail"+throwable.getMessage());
        }
    };
}
