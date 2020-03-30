package com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.MyApplication;
import com.example.trade.R;
import com.fragment.CityFragment;
import com.fragment.HomeFragment;
import com.fragment.MessageFragment;
import com.fragment.MyFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    LinearLayout mTabBottom ;
    int[] arrayTabIcon={R.mipmap.ic_tabbar_home_nor,R.mipmap.ic_tabbar_community_nor,R.mipmap.comui_tab_post, R.mipmap.ic_tabbar_message_nor,R.mipmap.ic_tabbar_my_nor};
    String[] arrayTabText={"首页","校园","","消息","我的"};
    int[] arrayTabIconSel={R.mipmap.ic_tabbar_home_sel,R.mipmap.ic_tabbar_community_sel,0,R.mipmap.ic_tabbar_message_sel,R.mipmap.ic_tabbar_my_sel};
    boolean[] arrayTabFlag={false,false,true,false,false};
    int mSelected;

    FragmentManager mFragmentManager;
    LayoutInflater lf;
    LinearLayout.LayoutParams Params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0F);
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);
        lf=LayoutInflater.from(this);
        mTabBottom= findViewById(R.id.tab_bottom);
        mFragmentManager=getSupportFragmentManager();
        mFragmentTransaction =mFragmentManager.beginTransaction();

        for (int i=0;i<arrayTabIcon.length;i++){
            View child=getTabView(arrayTabIcon[i],arrayTabText[i],arrayTabFlag[i]);
            child.setId(i);
            child.setOnClickListener(this);
            mTabBottom.addView(child,Params);
        }

    }

    private View getTabView(int imageResID,String text,boolean onlyimage ){
        View view=null;
        /*View view=null;
        view= lf.inflate(R.layout.tab_home,null);
        ImageView iv=view.findViewById(R.id.iv_tabIcon);
        TextView tv=view.findViewById(R.id.tv_tabText);
        iv.setImageResource(imageResID);*/
        if (onlyimage) {
            view= lf.inflate(R.layout.tab_post,null);
            ImageView iv=view.findViewById(R.id.iv_tabIcon);
            iv.setImageResource(imageResID);
        }
        else{
            view= lf.inflate(R.layout.tab_home,null);
            ImageView iv=view.findViewById(R.id.iv_tabIcon);
            TextView tv=view.findViewById(R.id.tv_tabText);
            iv.setImageResource(imageResID);
            tv.setText(text);
            tv.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case 0:
                    resetTabIcon(0);
                    mFragmentTransaction=mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.fragmentContainer,new HomeFragment());
                    mFragmentTransaction.commit();
                    break;
                case 1:
                    resetTabIcon(1);
                    mFragmentTransaction=mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.fragmentContainer,new CityFragment());
                    mFragmentTransaction.commit();
                    break;
                case 2:
                    startActivity(new Intent(this,PostActivity.class));
                    break;
                case 3:
                    if (MyApplication.getUser().getUid()!=0){  /*登录状态*/
                        resetTabIcon(3);
                        mFragmentTransaction=mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.fragmentContainer,new MessageFragment());
                        mFragmentTransaction.commit();
                    }
                    else {
                        startActivity(new Intent(this,LoginActivity.class));
                    }
                    break;
                case 4:
                    resetTabIcon(4);
                    mFragmentTransaction=mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.fragmentContainer,new MyFragment());
                    mFragmentTransaction.commit();
                    break;
            }
    }

    //更改选中图标颜色
    private void resetTabIcon(int mSelected){
        View view=mTabBottom.getChildAt(mSelected);

        for (int i=0;i<mTabBottom.getChildCount();i++){
            View childview=mTabBottom.getChildAt(i);
            ((ImageView)childview.findViewById(R.id.iv_tabIcon)).setImageResource(arrayTabIcon[i]);
        }
        ImageView iv=view.findViewById(R.id.iv_tabIcon);
        iv.setImageResource(arrayTabIconSel[mSelected]);
    }
}
