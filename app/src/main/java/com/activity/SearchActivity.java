package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.trade.R;


public class SearchActivity extends Activity {

    private EditText etSearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        etSearch=findViewById(R.id.etSearch);
        findViewById(R.id.ivbac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tvSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent();
                it.putExtra("content",etSearch.getText().toString());
                it.setClass(SearchActivity.this,SearchResuliActivity.class);
                startActivity(it);
            }
        });
    }
}
