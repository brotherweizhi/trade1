package com.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.APIInterface;
import com.HttpUtils;
import com.MyApplication;
import com.bean.User;
import com.example.trade.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener {

    TextView mTvReg;
    EditText inputaccount,inputpw;
    Button Login;
    View back;
    ProgressDialog dialog;
    public static String TAG="LoginActivity:";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
    }
    private void initUI(){
        back=findViewById(R.id.LoginBack);
        back.setOnClickListener(this);
        mTvReg=findViewById(R.id.tvReg);
        mTvReg.setOnClickListener(this);
        inputaccount=findViewById(R.id.inputaccount);
        inputpw=findViewById(R.id.inputpassword);
        Login=findViewById(R.id.loginbutton);
        Login.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.tvReg:
                intent.setClass(this,RegActivity.class);
                startActivity(intent);
                break;
            case R.id.LoginBack:
                finish();
                break;
            case R.id.loginbutton:
                dialog=ProgressDialog.show(this,"","登陆中");
                dialog.show();
                validate();
                break;
          default:
              break;
        }
    }
    /*校验登录信息*/
    private void  validate(){
        String account,pw;
        account=inputaccount.getText().toString();
        pw=inputpw.getText().toString();
        if (account.equals("")) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        if (pw.equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        RequestParams params=new RequestParams();
        params.add("account",account);
        params.add("password",pw);
        //持久化保存cookie到客户端
        HttpUtils.setCookieStore(new PersistentCookieStore(getApplicationContext()));
        HttpUtils.post(APIInterface.LOGIN,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code")==200){
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
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e(TAG,"异常");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("fail"+throwable.getMessage());
            }
        });

        /*if (account.equals("123") && pw.equals("123")) {
            SharedPreferences sp=getSharedPreferences(MyApplication.SP_USER,0);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("account",account);
            editor.putInt("isOnLine",1);
            editor.commit();
            dialog.dismiss();
            finish();
        }else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }*/

    }
}
