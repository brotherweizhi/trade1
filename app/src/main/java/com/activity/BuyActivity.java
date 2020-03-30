package com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.APIInterface;
import com.HttpUtils;
import com.example.trade.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class BuyActivity extends Activity {

    String id,title,poster;
    Button btEn;
    EditText etTel,etAdress,etMore;
    TextView test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        id=getIntent().getStringExtra("id");
        title=getIntent().getStringExtra("title");
        poster=getIntent().getStringExtra("poster");
        etAdress=findViewById(R.id.etAdress);
        etMore=findViewById(R.id.etMore);
        etTel=findViewById(R.id.etTel);
        btEn=findViewById(R.id.btnEnsure);
        btEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTel.getText().toString().equals("")||etAdress.getText().toString().equals("")) {
                    Toast.makeText(BuyActivity.this, "请完整输入收货信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestParams params = new RequestParams();
                params.put("content", "您发布的商品“"+title+"”已经被购买，发货信息为：电话："+etTel.getText().toString()+"，地址："+etAdress.getText().toString()+"请尽快发货");
                params.put("toUsername",poster);
                params.put("username","admin");
                HttpUtils.post(APIInterface.COMMENT_POST, params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //删除商品
                        HttpUtils.get(APIInterface.DEL_ITEM+"?id="+id,new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    if (response.getInt("code")==200){
                                        Toast.makeText(BuyActivity.this, "购买成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }

        });

    }
});
    }
}
