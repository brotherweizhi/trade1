package com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.trade.R;

public class ChatActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        findViewById(R.id.ChatBack).setOnClickListener(this);
        //findViewById(R.id.ChatDetail).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ChatBack:
                finish();
                break;
            /*case  R.id.ChatDetail:
                Intent it=new Intent();
                it.setClass(this,ItemDetailActivity.class);
                startActivity(it);
                break;*/
        }
    }
}
