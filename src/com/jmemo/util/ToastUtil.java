package com.jmemo.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	private Toast toast;
	public Toast showToast(Context context){
		toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		return toast;
	}
	public void setText(String text){
		toast.setText(text);
	}
}
