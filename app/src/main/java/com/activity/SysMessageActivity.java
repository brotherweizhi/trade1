package com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.APIInterface;
import com.CommonBaseAdapter;
import com.CommonViewHolder;
import com.HttpUtils;
import com.MyApplication;
import com.bean.Comment;
import com.example.trade.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SysMessageActivity extends Activity {
    TextView bar;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bar=findViewById(R.id.tvMyBar);
        listView=findViewById(R.id.ListView);
        bar.setText("系统消息");
        HttpUtils.get(APIInterface.SYS_MSG+"?username="+ MyApplication.getUser().getUsername(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code")==200){
                        Gson gson=new Gson();
                        List<Comment> mlist =gson.fromJson(response.getJSONArray("data").toString(),new TypeToken<List<Comment>>(){}.getType());
                        listView.setAdapter(new CommonBaseAdapter<Comment>(SysMessageActivity.this,mlist,R.layout.sub_chat) {
                            @Override
                            protected void convert(CommonViewHolder viewHolder, Comment message) {
                                viewHolder.setTextView(R.id.subChat,message.getContent())
                                .setTextView(R.id.tvChatTime,message.getCreated());
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
