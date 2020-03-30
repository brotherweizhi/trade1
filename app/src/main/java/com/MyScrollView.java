package com;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	//获取Y轴滚动距离
	int lastY;
	OnMyScrollListener onMyScrollListener;
	
	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setOnMyScrollListener(OnMyScrollListener onMyScrollListener) {
		this.onMyScrollListener = onMyScrollListener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(onMyScrollListener!=null) {
			onMyScrollListener.onScroll(lastY=getScrollY());
		}
				
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP://判断当手指离开屏幕时
			handler.sendMessageDelayed(handler.obtainMessage(), 5);
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int scrollY = getScrollY();
			
			if(scrollY!=lastY){//当ScrollView停止滚动时，我们才不会发送消息检测。否则，一直发消息检测
				lastY=scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 5);
			}
			
			if(onMyScrollListener!=null){
				onMyScrollListener.onScroll(scrollY);
			}
		};
	};
	
	//定义一个回调接口,把数据传递给接口实现者
	public interface OnMyScrollListener {
		public void onScroll(int scrollY);
	}
}
