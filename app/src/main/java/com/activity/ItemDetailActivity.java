package com.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.APIInterface;
import com.CommonBaseAdapter;
import com.CommonViewHolder;
import com.HttpUtils;
import com.MyApplication;
import com.MyListView;
import com.bean.Comment;
import com.bean.Item;
import com.bean.Pic;
import com.example.trade.R;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ItemDetailActivity extends Activity implements View.OnClickListener {
    Date nowDate=new Date();
    MyListView mlv;
    Date mDate=new Date();
    private TextView tvPrice,tvUsername,tvContent,tvCollections,tvComments,tvCreated,tvHits,tvCity,tvTitle;
    private ViewPager viewPager;
    private View inputArea, bottomArea;
    private String toUsername;
    private EditText etInput;
    private TextView btBuy;
    TextView ivOperation;
    List<Comment> comments;
    private BaseAdapter adapter;
    String id,title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        id=getIntent().getStringExtra("id");
        initUi();
        sendRequest();

    }

    public void initUi(){
        findViewById(R.id.ItemBack).setOnClickListener(v -> finish());
        btBuy=findViewById(R.id.ItemBuy);
        btBuy.setOnClickListener(this);
        tvTitle=findViewById(R.id.tvTitle);
        tvHits=findViewById(R.id.tvHits);
        tvCollections=findViewById(R.id.tvCollections);
        tvComments=findViewById(R.id.tvComments);
        tvCity=findViewById(R.id.tvCity);
        tvContent=findViewById(R.id.tvContent);
        tvCreated=findViewById(R.id.tvCreated);
        tvPrice=findViewById(R.id.tvPrice);
        tvUsername=findViewById(R.id.tvUserName);
        viewPager=findViewById(R.id.viewPager);
        etInput=findViewById(R.id.etInput);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Guangzhou"));
        ivOperation=findViewById(R.id.tvItemDel);
        inputArea = findViewById(R.id.inputArea);
        bottomArea = findViewById(R.id.bottomArea);
        mlv = findViewById(R.id.liuyan);
        findViewById(R.id.btnPub).setOnClickListener(this);
        tvComments.setOnClickListener(this);
        findViewById(R.id.keyBoard).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ItemBuy:
                Intent it=new Intent();
                Bundle extras=new Bundle();
                extras.putString("id",id);
                extras.putString("title",title);
                extras.putString("poster",tvUsername.getText().toString());
                it.putExtras(extras);
                it.setClass(ItemDetailActivity.this,BuyActivity.class);
                startActivity(it);
                finish();
                break;
            case R.id.tvComments://需要显示输入留言的区域
                inputArea.setVisibility(View.VISIBLE);
                bottomArea.setVisibility(View.GONE);
                break;
            case R.id.keyBoard://需要显示输入留言的区域
                inputArea.setVisibility(View.GONE);
                bottomArea.setVisibility(View.VISIBLE);
                etInput.setHint("");
                break;
            case R.id.btnPub://点击了"发送"
                if(etInput.getText().toString().equals("")) {
                    Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();return;
                }
                RequestParams params = new RequestParams();
                params.put("content", etInput.getText().toString());
                params.put("username", MyApplication.getUser().getUsername());
                params.put("itemId", getIntent().getStringExtra("id"));
                params.put("toUsername", toUsername);
                HttpUtils.post(APIInterface.COMMENT_POST, params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Toast.makeText(ItemDetailActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                        //把刚才发表的评论加入到集合类，更新ListView评论列表
                        Comment comment = new Comment();
                        try {
                            comment.setId(response.getInt("data"));
                            comment.setUsername(MyApplication.getUser().getUsername());
                            comment.setContent(etInput.getText().toString());
                            comment.setItemId(getIntent().getStringExtra("id"));
                            comment.setToUsername(toUsername);
                            comment.setCreated(String.valueOf(System.currentTimeMillis()/1000));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        comments.add(0, comment);
                        adapter.notifyDataSetChanged();
                        etInput.setText("");
                        inputArea.setVisibility(View.GONE);
                        bottomArea.setVisibility(View.VISIBLE);
                        tvComments.setText(String.valueOf(Integer.parseInt(tvComments.getText().toString())+1));
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, 0, "删除");
        menu.add(0, 2, 0, "取消");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                //获取选中的列表项数据
                final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Comment comment = comments.get(info.position);
                HttpUtils.get(APIInterface.COMMENT_DEL+"?id="+comment.getId()+"&username="+MyApplication.getUser().getUsername(), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (response.getInt("code")==200) {
                                Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                comments.remove(info.position);
                                adapter.notifyDataSetChanged();
                                tvComments.setText(String.valueOf(Integer.parseInt(tvComments.getText().toString()) - 1));
                            }else Toast.makeText(getBaseContext(), "删除失败", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

            case 2:
                break;
        }
        return super.onContextItemSelected(item);
    }


    //显示商品数据
    private void sendRequest(){

        HttpUtils.get(APIInterface.ITEM_VIEW+"?id="+id,responseHandler);
    }

    JsonHttpResponseHandler responseHandler= new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Gson gson=new Gson();
            Item bean=gson.fromJson(response.toString(), Item.class);
            title=bean.getTitle();
            tvTitle.setText(title);
            tvPrice.setText("￥"+bean.getPrice());
            tvCollections.setText(bean.getCollections());
            tvComments.setText(bean.getComments());
            tvContent.setText(bean.getContent());
            tvUsername.setText(bean.getUsername());
            tvCreated.setText(bean.getCreated());
            tvHits.setText("浏览次数"+bean.getHits());
            switch (bean.getCity()){
                case "1":
                    tvCity.setText("发布于广东工业大学");
                    break;
                case "2":
                    tvCity.setText("发布于星海音乐学院");
                    break;
                case "3":
                    tvCity.setText("发布于广州大学");
                    break;
            }

            if (!bean.getUsername().equals(MyApplication.getUser().getUsername())){
                ivOperation.setVisibility(View.GONE);
            }else {
                ivOperation.setOnClickListener(v -> {
                    HttpUtils.get(APIInterface.DEL_ITEM+"?id="+getIntent().getStringExtra("id"),new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                if (response.getInt("code")==200){
                                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                });
            }

            comments=bean.getCommentList();
            mlv.setOnItemClickListener((parent, view, position, id) -> {
                Comment comment = comments.get(position);
                toUsername = comment.getUsername();
                etInput.setHint("@ "+toUsername);
            });

            //评论适配器

            adapter=new CommonBaseAdapter<Comment>(getBaseContext(),comments,R.layout.sub_comment) {
                @Override
                protected void convert(CommonViewHolder viewHolder, Comment comment) {
                    viewHolder.setTextView(R.id.tvUsername,comment.getUsername());
                    if (comment.getToUsername()==null){
                        viewHolder .setTextView(R.id.tvComment,comment.getContent());
                    }else {
                        String result="回复@"+comment.getToUsername()+":"+comment.getContent();
                        viewHolder .setTextView(R.id.tvComment,result);
                    }

                    if (!bean.getCreated().equals("")){
                        viewHolder.setTextView(R.id.tvCreated,comment.getCreated());
                    }
                }
            };
            mlv.setAdapter(adapter);
            registerForContextMenu(mlv);

            //设置图片
            List<Pic> picList=bean.getPicList();
            if (picList!=null&&picList.size()>0){
                viewPager.setAdapter(new PicViewPagerAdater(picList,getBaseContext()));
            }
            mlv.setFocusable(false);
        }
    };

    private  class PicViewPagerAdater extends PagerAdapter {

        List<Pic> mData;
        Context mContext;

        public PicViewPagerAdater(List<Pic> mData, Context mContext) {
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
            ImageLoader.getInstance().displayImage(mData.get(position).getUrl(),iv);
            container.addView(iv);
            return iv;
        }
    }
}
