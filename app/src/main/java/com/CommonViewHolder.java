package com;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean.Pic;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.List;


/**
 * 万能ViewHolder
 * @author Administrator
 *
 */
public class CommonViewHolder {

	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private Context mContext;
	private int mScreenWidth;
	
	CommonViewHolder(Context context, int layoutID, int position, View convertView, ViewGroup parent){
		this.mContext = context;
		this.mViews = new SparseArray<View>();
		this.mPosition = position;
		this.mConvertView = LayoutInflater.from(context).inflate(layoutID, parent, false);
		this.mConvertView.setTag(this);
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mScreenWidth = wm.getDefaultDisplay().getWidth();
	}
	
	public static CommonViewHolder getViewHolder(Context context, int layoutID, int position, 
			View convertView, ViewGroup parent){
		if(convertView==null) {
			CommonViewHolder viewHolder = new CommonViewHolder(context, layoutID, position, convertView, parent);
			return viewHolder;
		} else {
			CommonViewHolder viewHolder = (CommonViewHolder) convertView.getTag();
			viewHolder.mPosition = position;
			return viewHolder;
		}
	}

	public View getConvertView() {
		return this.mConvertView;
	}
	
	public <T extends View>T getView(int viewID){
		View view = mViews.get(viewID);
		
		if(view==null){
			view = getConvertView().findViewById(viewID);
			mViews.put(viewID, view);
		}
		
		return (T)view;
	}
	
	public CommonViewHolder setTextView(int viewID, String text){
		TextView tv = getView(viewID);
		tv.setText(text);
		return this;
	}
	
	/*//带删除线的TextView
	public CommonViewHolder setTextViewDel(int viewID, String text){
		TextView tv = getView(viewID);
		tv.setText(text);
		tv.getPaint().setFlags(TextPaint.STRIKE_THRU_TEXT_FLAG);//设置删除线
		tv.getPaint().setAntiAlias(true);//自动去掉锯齿
		return this;
	}
	
	//带中划线的TextView
	public CommonViewHolder setTextViewHaveStrikeLine(int viewID, String text){
		TextView tv = getView(viewID);
		tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
		tv.setText(text);
		return this;
	}*/
	
	public CommonViewHolder setImageResource(int viewID, int resourceID){
		ImageView iv = getView(viewID);
		iv.setImageResource(resourceID);
		return this;
	}
	
	//为商品格子动态设置图片的宽高,rate表示长宽比率,nums表示每行显示几列图片
	public CommonViewHolder setImageResource(int viewID, int resourceID, int rate, int nums){
		ImageView iv = getView(viewID);
		LayoutParams params = iv.getLayoutParams();
		params.width = mScreenWidth / nums;
		params.height = params.width * rate;
		iv.setLayoutParams(params);
		iv.setImageResource(resourceID);
		return this;
	}
	
	//根据图片的路径获取图片(主要用于显示本地所有图片),nums为每行显示几列图片
	public CommonViewHolder setImageResource(int viewID, String pathName, int nums){
		ImageView iv = getView(viewID);
		LayoutParams params = iv.getLayoutParams();
		params.width = mScreenWidth / nums;
		params.height = params.width * 1;
		/*params.width = 50;
		params.height = params.width;*/
		iv.setLayoutParams(params);
		//iv.setImageBitmap(BitmapFactory.decodeFile(pathName));//不能粗暴的使用该方法,会有OOM的危险
		ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(pathName), iv);//调用UIL组件获取图片显示在ImageView
		return this;
	}
	
	public CommonViewHolder setImageBitmap(int viewID, Bitmap bitmap){
		ImageView iv = getView(viewID);
		iv.setImageBitmap(bitmap);
		return this;
	}
	
	public CommonViewHolder setImageURI(int viewID, String uri, int nums){
		ImageView iv = getView(viewID);
		//采用第三方框架比如Google volley, assync-http, universal-image-loader
		//ImageLoader.getInstance().getImageBitmap(viewID, uri);
		LayoutParams params = iv.getLayoutParams();
		params.width = mScreenWidth / nums;
		params.height = params.width;
		iv.setLayoutParams(params);
		ImageLoader.getInstance().displayImage(uri, iv);
		return this;
	}
	
	public CommonViewHolder setImageURI(int viewID, String uri){
		ImageView iv = getView(viewID);
		ImageLoader.getInstance().displayImage(uri, iv);
		return this;
	}
	
	//设置水平滚动视图(画廊效果)
	public CommonViewHolder setHSVGallery(int viewID, List<Pic> list, List<Pic> PicBigList){
		MyHorizontalScrollView hsv = getView(viewID);
		MyHorizontalScrollView.HorizontalScrollViewAdapter adapter= hsv.new HorizontalScrollViewAdapter(mContext, list,PicBigList);
		hsv.initData(adapter);
		return this;
	}

	public int getPosition() {
		return mPosition;
	}
}