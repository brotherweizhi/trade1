package com;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 万能适配器
 * @author Administrator
 * @param <T>
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> mData;
	protected LayoutInflater mLayoutInflater;
	protected int mLayoutID;
	
	public CommonBaseAdapter(Context context, List<T> data, int layoutID) {
		this.context = context;
		this.mData = data;
		this.mLayoutID = layoutID;
		this.mLayoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(context, mLayoutID, position, convertView, parent);
		
		convert(viewHolder, mData.get(position));
		
		return viewHolder.getConvertView();
	}

	//ViewHolder, T t
	protected abstract void convert(CommonViewHolder viewHolder, T t);
}
