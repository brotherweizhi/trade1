package com;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.bean.User;
import com.example.trade.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class MyApplication extends Application {

    public static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MyApplication.user = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
    }

    public static void initImageLoader(Context context) {

        File caahe= StorageUtils.getOwnCacheDirectory(context,"bradway/cache");//缓存目录
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.memoryCache(new LruMemoryCache(7*1024*1024));
        config.diskCache(new UnlimitedDiskCache(caahe));
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.diskCacheFileCount(500);
        config.writeDebugLogs(); // Remove for release app

        //图片显示选项
        DisplayImageOptions options=new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).showImageOnLoading(R.mipmap.photo_icon_camera).bitmapConfig(Bitmap.Config.RGB_565).build();
        config.defaultDisplayImageOptions(options);
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
    public static final String SP_USER="userinfo";   //存取登陆后用户信息的sp文件名

    public static User getUserInfo(Activity activity){
        User user=new User();
        SharedPreferences sp=activity.getSharedPreferences(MyApplication.SP_USER,0);
        user.setAccount(sp.getString("account",""));
        user.setIsOnline(sp.getInt("isOnLine",0));
        return user;
    }



}
