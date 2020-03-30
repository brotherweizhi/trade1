package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.APIInterface;
import com.CommomList;
import com.CommonBaseAdapter;
import com.CommonViewHolder;
import com.DateUtils;
import com.HttpUtils;
import com.MyApplication;
import com.bean.Item;
import com.example.trade.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class SearchResuliActivity extends Activity {
    ListView listView;
    CommomList<List<Item>> mcommomList;
    private String content;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favor);
        initUI();
        sendRequest();
    }

    private void sendRequest(){
        HttpUtils.get(APIInterface.HOME+"?kw="+ content,responseHandler);
    }

    @Override
    public void onResume() {
        super.onResume();
        sendRequest();
    }

    JsonHttpResponseHandler responseHandler= new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            //利用gson把json转成bean
            Gson gson=new Gson();
            mcommomList = gson.fromJson(response.toString(), new TypeToken<CommomList<List<Item>>>() {}.getType());
            Date nowDate=new Date();
            listView.setAdapter(new CommonBaseAdapter<Item>(SearchResuliActivity.this,mcommomList.getList(),R.layout.sub_home_list) {
                @Override
                protected void convert(CommonViewHolder viewHolder, Item item) {
                    if (item.getCreated()!=null&&!item.getCreated().equals("")){
                        Date myDate= DateUtils.toDate(item.getCreated());
                        String created= DateUtils.subTime(nowDate,myDate);
                        viewHolder.setTextView(R.id.tvCreated,created);
                    }
                    viewHolder.setTextView(R.id.tvPrice,"￥"+item.getPrice())
                            .setTextView(R.id.tvContent,item.getContent())
                            .setTextView(R.id.tvCity,item.getCity())
                            .setTextView(R.id.tvComments, String.valueOf(item.getComments()))
                            .setTextView(R.id.tvCollections, String.valueOf(item.getCollections()))
                            .setTextView(R.id.tvUserName, item.getUsername());
                    if (item.getFlag()!=null&&item.getFlag().equals("1")){
                        ((TextView)viewHolder.getView(R.id.tvCollections)).setCompoundDrawablesWithIntrinsicBounds(R.mipmap.love_red,0,0,0);
                    }else ((TextView)viewHolder.getView(R.id.tvCollections)).setCompoundDrawablesWithIntrinsicBounds(R.mipmap.love_gray,0,0,0);

                    if (item.getPicList()!=null&&item.getPicList().size()>0){
                        viewHolder.setHSVGallery(R.id.hsv,item.getPicList(),item.getPicList());
                    }
                    //位列表项设置监听
                    viewHolder.getView(R.id.tvTest).setOnClickListener(v -> {
                        Intent intent=new Intent(SearchResuliActivity.this, ItemDetailActivity.class);
                        intent.putExtra("id",item.getId());
                        startActivity(intent);
                    });
                    //收藏监听器
                    viewHolder.getView(R.id.tvCollections).setOnClickListener(v -> {
                        if (MyApplication.getUser().getUid()!=0){//判断是否登录
                            HttpUtils.get(APIInterface.FAVOUR_ITEM+"?uid="+MyApplication.getUser().getUid()+"&itemId="+item.getId(),new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    TextView tv=(TextView)v;
                                    try {
                                        if (response.getInt("code")==200){
                                            if (response.getInt("data")==1){
                                                Toast.makeText(SearchResuliActivity.this,"收藏成功", Toast.LENGTH_SHORT).show();
                                                tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.love_red,0,0,0);
                                                tv.setText(String.valueOf(Integer.parseInt(tv.getText().toString())+1));
                                            }else {
                                                Toast.makeText(SearchResuliActivity.this,"取消收藏成功", Toast.LENGTH_SHORT).show();
                                                tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.love_gray,0,0,0);
                                                tv.setText(String.valueOf(Integer.parseInt(tv.getText().toString())-1));
                                            }
                                            super.onSuccess(statusCode, headers, response);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else {
                            startActivity(new Intent(SearchResuliActivity.this, LoginActivity.class));
                        }
                    });
                }
            });

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    };

    private void initUI(){
        listView=findViewById(R.id.ListView);
        listView.setSelector(R.drawable.bg_mysell);
        ((TextView)findViewById(R.id.tvMyBar)).setText("搜索结果");
        content=getIntent().getStringExtra("content");
    }

}
