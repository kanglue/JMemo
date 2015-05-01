package com.jmemo.mini;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jmemo.R;
import com.jmemo.R.id;
import com.jmemo.R.layout;
import com.jmemo.db.Phrase;
import com.jmemo.db.VocabularyHelper;

public class FloatWindowBigView extends LinearLayout {

	/**
	 * 记录大悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录大悬浮窗的高度
	 */
	public static int viewHeight;
	
	private EditText enText;
	
	private EditText cnText;

	public FloatWindowBigView(final Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
		View view = findViewById(R.id.big_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		Button save = (Button) findViewById(R.id.save);
		Button back = (Button) findViewById(R.id.back);
		ImageView close = (ImageView) findViewById(R.id.x);
		enText = (EditText)findViewById(R.id.english);
		cnText = (EditText)findViewById(R.id.chinese);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//new En2CnService(context).save(enText.getText().toString().trim(), cnText.getText().toString().trim());
				VocabularyHelper.insertPhrase(new Phrase(enText.getText().toString().trim(), 0, cnText.getText().toString().trim()));
			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击返回的时候，移除大悬浮窗，创建小悬浮窗
				MyWindowManager.removeBigWindow(context);
				MyWindowManager.createSmallWindow(context);
			}
		});
		close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                MyWindowManager.removeBigWindow(context);
                MyWindowManager.removeSmallWindow(context);
                Intent intent = new Intent(getContext(), FloatWindowService.class);
                context.stopService(intent);
            }
        });
	}
}

