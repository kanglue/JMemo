package com.jmemo.menu;





import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jmemo.R;


public class MenuFragment extends Fragment implements View.OnClickListener{

	private LinearLayout mLinearFirst;
	private LinearLayout mLinearSecond;
	private LinearLayout mLinearThird;
	private LinearLayout mLinearFourth;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_frame_menu, null);
		mLinearFirst = (LinearLayout) view.findViewById(R.id.linear_first);
		mLinearSecond = (LinearLayout) view.findViewById(R.id.linear_second);
		mLinearThird = (LinearLayout) view.findViewById(R.id.linear_third);
		mLinearFourth = (LinearLayout) view.findViewById(R.id.linear_fourth);
		return view; 
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mLinearFirst.setOnClickListener(this);
		mLinearSecond.setOnClickListener(this);
		mLinearThird.setOnClickListener(this);
		mLinearFourth.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		mLinearFirst.setSelected(false);
		mLinearSecond.setSelected(false);
		mLinearThird.setSelected(false);
		mLinearFourth.setSelected(false);
		v.setSelected(true);
		onMenuItemClickListener.onChange(v.getId());
	}
	
	private OnMenuItemClickListener onMenuItemClickListener;
	
	
	public OnMenuItemClickListener getOnMenuItemClickListener() {
		return onMenuItemClickListener;
	}

	public void setOnMenuItemClickListener(
			OnMenuItemClickListener onMenuItemClickListener) {
		this.onMenuItemClickListener = onMenuItemClickListener;
	}
}
