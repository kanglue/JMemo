package com.jmemo.mini;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jmemo.R;
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
	
	private int category;

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
		RadioGroup group = (RadioGroup)this.findViewById(R.id.radioGroup);
		
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//new En2CnService(context).save(enText.getText().toString().trim(), cnText.getText().toString().trim());
				VocabularyHelper.insertPhrase(new Phrase(enText.getText().toString().trim(), category, cnText.getText().toString().trim()));
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
		//绑定一个匿名监听器
		         group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		             
		             @Override
		             public void onCheckedChanged(RadioGroup arg0, int arg1) {
		                 //获取变更后的选中项的ID
		                 int radioButtonId = arg0.getCheckedRadioButtonId();
		                 //根据ID获取RadioButton的实例
		                 RadioButton rb = (RadioButton)findViewById(radioButtonId);
		                 String s = rb.getText().toString();
		                 if(s.equals("Word"))
		                 {
		                	 category = 0;
		                 }else if(s.equals("Phrase"))
		                 {
		                	 category = 1;
		                 }
		             }
		         });
	}
}

