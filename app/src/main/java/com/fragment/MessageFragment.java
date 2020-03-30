package com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.APIInterface;
import com.CommonBaseAdapter;
import com.CommonViewHolder;
import com.HttpUtils;
import com.MyApplication;
import com.activity.ItemDetailActivity;
import com.activity.SysMessageActivity;
import com.bean.Comment;
import com.example.trade.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MessageFragment extends Fragment {
    ListView listView;
    RelativeLayout systemMessage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_message,container,false);
        initUI(rootview);
        return rootview;
    }
    private void initUI(View rootview){
        systemMessage=rootview.findViewById(R.id.systemMessage);
        listView=rootview.findViewById(R.id.ListView);
        HttpUtils.get(APIInterface.COMMENT_MY+"?username="+ MyApplication.getUser().getUsername(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code")==200){
                        Gson gson=new Gson();
                        List<Comment> mlist =gson.fromJson(response.getJSONArray("data").toString(),new TypeToken<List<Comment>>(){}.getType());
                        listView.setAdapter(new CommonBaseAdapter<Comment>(getActivity(),mlist,R.layout.sub_message) {

                            @Override
                            protected void convert(CommonViewHolder viewHolder, Comment message) {
                                viewHolder.setImageURI(R.id.ivPic,message.getPic())
                                            .setTextView(R.id.tvUsername,message.getUsername())
                                            .setTextView(R.id.tvContent,message.getContent());
                                if (message.getReaded()==0){
                                    viewHolder.getView(R.id.ivRead).setVisibility(View.VISIBLE);
                                }
                                else{
                                    viewHolder.getView(R.id.ivRead).setVisibility(View.GONE);
                                }
                                viewHolder.getConvertView().setOnClickListener(v -> {
                                    HttpUtils.get(APIInterface.COMMENT_READ+"?id="+message.getId(),new JsonHttpResponseHandler());
                                    Intent it=new Intent(getActivity(), ItemDetailActivity.class);
                                    it.putExtra("id",message.getItemId());
                                    startActivity(it);
                                    viewHolder.getView(R.id.ivRead).setVisibility(View.GONE);
                                });
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        systemMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent();
                it.setClass(getActivity(), SysMessageActivity.class);
                startActivity(it);
            }
        });
    }
  /*  private List<Message> getData(){
        List<Message> list=new ArrayList<Message>();
        Message messag4e=new Message(R.mipmap.picre2,1,"系统消息","mo","4");
        Message messag1e=new Message(R.mipmap.picre2,1,"mianmo","mo","1");
        Message message2=new Message(R.mipmap.picre1,1,"xie","hai","2");
        Message message3=new Message(R.mipmap.picre3,1,"yu","nmsl","3");
        list.add(messag4e);list.add(messag1e);list.add(message2);list.add(message3);
        return list;
    }*/
}
