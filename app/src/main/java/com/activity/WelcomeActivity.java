package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.MyApplication;
import com.bean.User;
import com.example.trade.R;
import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.cookie.Cookie;

import java.util.List;

public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        PersistentCookieStore cookieStore=new PersistentCookieStore(getApplicationContext());
        List<Cookie> cookies=cookieStore.getCookies();
        User user=new User();
        for (Cookie cookie:cookies){
            System.out.println(cookie.getName()+cookie.getValue());
            if (cookie.getName().equals("uid")){
                user.setUid(Integer.parseInt(cookie.getValue()));
            }else if (cookie.getName().equals("username")){
                user.setUsername(cookie.getValue());
            }
            else if (cookie.getName().equals("tel")){
                user.setTel(cookie.getValue());
            }
        }
        MyApplication.setUser(user);

        Handler handler=new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        },1000);
    }
}
