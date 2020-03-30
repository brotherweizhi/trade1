package com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.APIInterface;
import com.CommomList;
import com.CommonBaseAdapter;
import com.CommonViewHolder;
import com.DateUtils;
import com.HttpUtils;
import com.MyApplication;
import com.activity.ItemDetailActivity;
import com.activity.LoginActivity;
import com.activity.SearchActivity;
import com.bean.Item;
import com.example.trade.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {
    ListView listView;
    View mivSearch;
    CommomList<List<Item>> mcommomList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_home,container,false);
        initUI(rootview);
        sendRequest();
        return rootview;
    }

    private void sendRequest(){
        HttpUtils.get(APIInterface.HOME+"?uid="+MyApplication.getUser().getUid(),responseHandler);
    }

    @Override
    public void onResume() {
        super.onResume();
        sendRequest();
    }

    //获取商品数据Handeler
    JsonHttpResponseHandler responseHandler= new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            //利用gson把json转成bean
            Gson gson=new Gson();
            mcommomList=gson.fromJson(response.toString(),new TypeToken<CommomList<List<Item>>>(){}.getType());

            Date nowDate=new Date();

            listView.setAdapter(new CommonBaseAdapter<Item>(getActivity(),mcommomList.getList(),R.layout.sub_home_list) {
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
                            .setTextView(R.id.tvTitle,item.getTitle())
                            .setTextView(R.id.tvUserName, item.getUsername());
                    if (item.getFlag()!=null&&item.getFlag().equals("1")){
                        ((TextView)viewHolder.getView(R.id.tvCollections)).setCompoundDrawablesWithIntrinsicBounds(R.mipmap.love_red,0,0,0);
                    }else ((TextView)viewHolder.getView(R.id.tvCollections)).setCompoundDrawablesWithIntrinsicBounds(R.mipmap.love_gray,0,0,0);
                    
                    if (item.getPicList()!=null&&item.getPicList().size()>0){
                        viewHolder.setHSVGallery(R.id.hsv,item.getPicList(),item.getPicList());
                    }
                    //位列表项设置监听
                    viewHolder.getView(R.id.tvTest).setOnClickListener(v -> {
                        Intent intent=new Intent(getActivity(), ItemDetailActivity.class);
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
                                                Toast.makeText(getActivity(),"收藏成功", Toast.LENGTH_SHORT).show();
                                                tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.love_red,0,0,0);
                                                tv.setText(String.valueOf(Integer.parseInt(tv.getText().toString())+1));
                                            }else {
                                                Toast.makeText(getActivity(),"取消收藏成功", Toast.LENGTH_SHORT).show();
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
                            startActivity(new Intent(getActivity(), LoginActivity.class));
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

    private void initUI(View rootview){
        mivSearch=rootview.findViewById(R.id.ivSearch);
        mivSearch.setOnClickListener(this);

        listView=rootview.findViewById(R.id.ListView);
        listView.setSelector(R.drawable.bg_mysell);
        rootview.findViewById(R.id.tvRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }


    private List<Integer> getHSVData(){
        List<Integer> data=new ArrayList<Integer>();
        data.add(R.mipmap.picre1);
        data.add(R.mipmap.picre2);
        data.add(R.drawable.pic3);
        data.add(R.drawable.pic4);
        data.add(R.drawable.pic5);
        data.add(R.drawable.pic6);
        data.add(R.drawable.pic5);
        return  data;
    }

    private ArrayList<Integer> getHSVDataBig(){
        ArrayList<Integer> data=new ArrayList<Integer>();
        data.add(R.mipmap.pic1);
        data.add(R.mipmap.pic2);
        data.add(R.mipmap.pic3);
        data.add(R.mipmap.pic4);
        return  data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivSearch:
                Intent it=new Intent();
                it.setClass(getActivity(), SearchActivity.class);
                startActivity(it);
                break;
        }
    }
}
