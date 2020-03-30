package com;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.client.CookieStore;


public class HttpUtils {

    static AsyncHttpClient client=new AsyncHttpClient();

    public static void setCookieStore(CookieStore cookieStore){
        client.setCookieStore(cookieStore);
    }

    public static void get(String url, JsonHttpResponseHandler responseHandler){
        client.get(getUrl(url),responseHandler);
    }

    public static void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler){
        client.get(getUrl(url),params,responseHandler);
    }

    public static void post(String url, JsonHttpResponseHandler responseHandler){
        client.post(getUrl(url),responseHandler);
    }

    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler){
        client.post(getUrl(url),params,responseHandler);
    }

    public static String getUrl(String url){
        return APIInterface.API_HOST+url;
    }
}
