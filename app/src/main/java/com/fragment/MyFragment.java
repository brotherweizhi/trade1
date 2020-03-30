package com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.MyApplication;
import com.activity.FavorActivity;
import com.activity.LoginActivity;
import com.activity.MyItemActivity;
import com.activity.SettingActivity;
import com.bean.User;
import com.example.trade.R;

public class MyFragment extends Fragment implements View.OnClickListener {
    View noLogin,Logined;
    TextView tvLogined;


    private void initui(View rootView){
        noLogin=rootView.findViewById(R.id.mynologin);
        Logined=rootView.findViewById(R.id.MyLogined);
        tvLogined=rootView.findViewById(R.id.tvLogined);
        rootView.findViewById(R.id.LoginButton).setOnClickListener(this);
        rootView.findViewById(R.id.setting).setOnClickListener(this);
        rootView.findViewById(R.id.tvMyFavor).setOnClickListener(v -> {
            Intent it=new Intent(getActivity(), FavorActivity.class);
            startActivity(it);
        });
        rootView.findViewById(R.id.tvOnSell).setOnClickListener(v -> {
            Intent it=new Intent(getActivity(), MyItemActivity.class);
            startActivity(it);
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_my,container,false);
        initui(rootview);
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        User u=MyApplication.getUser();
        if (u.getUid()!=0){  /*登录状态*/
            noLogin.setVisibility(View.GONE);
            Logined.setVisibility(View.VISIBLE);
            tvLogined.setText(u.getUsername());
        }
        else {
            noLogin.setVisibility(View.VISIBLE);
            Logined.setVisibility(View.GONE);
        }
    }

   /* public User getUserInfo(){    *//* 获取用户信息*//*
        User user=new User();
        SharedPreferences sp=getActivity().getSharedPreferences(MyApplication.SP_USER,0);
        user.setAccount(sp.getString("account",""));
        user.setIsOnline(sp.getInt("isOnLine",0));
        return user;
    }*/

    @Override
    public void onClick(View v) {
        Intent it=new Intent();
        switch (v.getId()){
            case R.id.LoginButton:
                it.setClass(getActivity(), LoginActivity.class);
                startActivity(it);
                break;
            case R.id.setting:
                it.setClass(getActivity(), SettingActivity.class);
                startActivity(it);
                break;
        }
    }
}
