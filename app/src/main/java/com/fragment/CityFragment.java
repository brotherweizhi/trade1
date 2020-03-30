package com.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.APIInterface;
import com.CommomList;
import com.CommonBaseAdapter;
import com.CommonViewHolder;
import com.HttpUtils;
import com.activity.CateActivity;
import com.activity.ItemDetailActivity;
import com.bean.Item;
import com.example.trade.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityFragment extends Fragment  {


    View tabChooseCity;
    PopupWindow mpopupWindow;
    TextView mTextview;
    LayoutInflater mLayoutInflater;
    GridView mitemGridView;
    CommomList<List<Item>> mCommomList;
    RelativeLayout mobile,book,shose,other;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_city,container,false);
        this.mLayoutInflater=inflater;
        mitemGridView=rootview.findViewById(R.id.itemGridView);

        //分类监听器
        rootview.findViewById(R.id.cate_mobile).setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), CateActivity.class);
            intent.putExtra("cate","1");
            getActivity().startActivity(intent);
        });
        rootview.findViewById(R.id.cate_book).setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), CateActivity.class);
            intent.putExtra("cate","3");
            getActivity().startActivity(intent);
        });
        rootview.findViewById(R.id.cate_shoose).setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), CateActivity.class);
            intent.putExtra("cate","2");
            getActivity().startActivity(intent);
        });
        rootview.findViewById(R.id.cate_other).setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), CateActivity.class);
            intent.putExtra("cate","4");
            getActivity().startActivity(intent);
        });
        initTab(rootview);
        initItemGridView();
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        initItemGridView();
    }

    private void initItemGridView(){
        HttpUtils.get(APIInterface.HOME+"?city=",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Gson gson=new Gson();
                mCommomList=gson.fromJson(response.toString(),new TypeToken<CommomList<List<Item>>>() {}.getType());
                mitemGridView.setAdapter(new CommonBaseAdapter<Item>(getActivity(),mCommomList.getList(),R.layout.sub_city_grid) {
                    @Override
                    protected void convert(CommonViewHolder viewHolder, Item item) {
                        viewHolder.setTextView(R.id.tvTitle,item.getTitle())
                                .setTextView(R.id.tvPrice,"￥"+item.getPrice())
                                .setImageURI(R.id.ivPic,item.getPic(),2);
                        switch (item.getCity()){
                            case "1":
                                viewHolder.setTextView(R.id.tvCity,"广东工业大学");
                                break;
                            case "2":
                                viewHolder.setTextView(R.id.tvCity,"星海音乐学院");
                                break;
                            case "3":
                                viewHolder.setTextView(R.id.tvCity,"广州大学");
                                break;
                        }

                    }
                });
            }
        });
        mitemGridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent it=new Intent(getActivity(), ItemDetailActivity.class);
            it.putExtra("id",mCommomList.getList().get(position).getId());
            startActivity(it);
        });
        /*mitemGridView.setAdapter(new CommonBaseAdapter<Item>(getActivity(),initItemData(),R.layout.sub_city_grid) {
            @Override
            protected void convert(CommonViewHolder viewHolder, Item item) {
                viewHolder.setImageResource(R.id.ivPic,item.getPic(),1,2)
                        .setTextView(R.id.tvTitle,item.getTitle())
                        .setTextView(R.id.tvPrice,"￥"+item.getPrice())
                        .setTextView(R.id.tvDistrict,item.getCity());
            }
        });*/
    }

   /* private List<Item> initItemData(){
        List<Item> list=new ArrayList<Item>();
        Item item=new Item(1,"120","标题标题","广东工业大学",R.mipmap.pic2);
        Item item1=new Item(1,"10","标题标题","广东工业大学",R.mipmap.pic1);
        Item item2=new Item(1,"620","标题标题","广东工业大学",R.mipmap.pic3);
        Item item3=new Item(1,"620","标题标题","广东工业大学",R.mipmap.pic4);
        Item item4=new Item(1,"620","标题标题","广东工业大学",R.mipmap.pic5);
        Item item5=new Item(1,"620","标题标题","广东工业大学",R.drawable.pic6);
        Item item6=new Item(1,"620","标题标题","广东工业大学",R.drawable.pic4);
        Item item7=new Item(1,"620","标题标题","广东工业大学",R.drawable.pic5);
        Item item8=new Item(1,"620","标题标题","广东工业大学",R.drawable.pic6);
        list.add(item);list.add(item2);list.add(item1);list.add(item3);list.add(item4);list.add(item5);list.add(item6);list.add(item7);list.add(item8);
        return list;
    }*/

    private void initTab(View rootview){
        tabChooseCity=rootview.findViewById(R.id.tabChooseCity);
        TabOnClickListen tabOnClickListen=new TabOnClickListen();
        tabChooseCity.setOnClickListener(tabOnClickListen);
        mTextview=rootview.findViewById(R.id.tabChooseCity);
        mTextview.setText("全部");
    }


    //Tab监听器
    private class TabOnClickListen implements View.OnClickListener{

        int screenWidth;
        public TabOnClickListen(){
            DisplayMetrics dm=new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenWidth=dm.widthPixels;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tabChooseCity:
                    initWindow(v);

                    break;
            }
        }
        private void initWindow(View view){
            ArrayList<HashMap<String,String>> data= new ArrayList<>();
            HashMap<String, String> map0=new HashMap<>();
            HashMap<String, String> map1=new HashMap<>();
            HashMap<String, String> map2=new HashMap<>();
            HashMap<String, String> map3=new HashMap<>();
            map0.put("name","全部");
            map0.put("id","0");
            map1.put("name","广东工业大学");
            map1.put("id","1");
            map2.put("name","星海音乐学院");
            map2.put("id","2");
            map3.put("name","广州大学");
            map3.put("id","3");
            data.add(map0);
            data.add(map1);
            data.add(map2); data.add(map3);

            View contentView=LayoutInflater.from(getActivity()).inflate(R.layout.popup_listview,null);
            contentView.setOnClickListener(v -> {
                if (mpopupWindow.isShowing())
                    mpopupWindow.dismiss();
            });
            ListView listView=contentView.findViewById(R.id.ListView);

            //点击弹出窗
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                Toast.makeText(getActivity(),String.valueOf(data.get(position).get("id")),Toast.LENGTH_SHORT).show();
                mTextview.setText(data.get(position).get("name"));
                if (mpopupWindow.isShowing()) {
                    mpopupWindow.dismiss();
                }
                HttpUtils.get(APIInterface.HOME+"?city="+data.get(position).get("id"),new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Gson gson=new Gson();
                        mCommomList=gson.fromJson(response.toString(),new TypeToken<CommomList<List<Item>>>() {}.getType());
                        mitemGridView.setAdapter(new CommonBaseAdapter<Item>(getActivity(),mCommomList.getList(),R.layout.sub_city_grid) {
                            @Override
                            protected void convert(CommonViewHolder viewHolder, Item item) {
                                viewHolder.setTextView(R.id.tvTitle,item.getTitle())
                                        .setTextView(R.id.tvPrice,"￥"+item.getPrice())
                                        .setImageURI(R.id.ivPic,item.getPic(),2)
                                        .setTextView(R.id.tvCity,item.getCity());
                            }
                        });
                    }
                });
                mitemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent it=new Intent(getActivity(), ItemDetailActivity.class);
                        it.putExtra("id",mCommomList.getList().get(position).getId());
                        startActivity(it);
                    }
                });
            });

            listView.setAdapter(new SimpleAdapter(getActivity(),data,R.layout.sub_tab_sort,new String[]{"name"},new int[]{R.id.name}));
            mpopupWindow =new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mpopupWindow.setOutsideTouchable(true);
            mpopupWindow.setAnimationStyle(0);
            mpopupWindow.setFocusable(true);
            mpopupWindow.showAsDropDown(tabChooseCity);

        }

    }
}
