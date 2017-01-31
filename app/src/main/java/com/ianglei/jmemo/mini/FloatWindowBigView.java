package com.ianglei.jmemo.mini;


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

import com.ianglei.jmemo.R;
import com.ianglei.jmemo.bean.Phrase;
import com.ianglei.jmemo.db.PhraseHelper;
import com.ianglei.jmemo.db.VocabularyHelper;
import com.ianglei.jmemo.utils.ToastUtil;

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

	private EditText saText;

	private EditText syText;
	
	private int category;

	public FloatWindowBigView(final Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
		View view = findViewById(R.id.big_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		Button save = (Button) findViewById(R.id.save);
		Button back = (Button) findViewById(R.id.back);
		ImageView close = (ImageView) findViewById(R.id.close_btn);
		enText = (EditText)findViewById(R.id.phrase);
		cnText = (EditText)findViewById(R.id.translation);
		syText = (EditText)findViewById(R.id.symbol);
		saText = (EditText)findViewById(R.id.sample);
		RadioGroup group = (RadioGroup)this.findViewById(R.id.radioGroup);
		
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//new En2CnService(context).save(enText.getText().toString().trim(), cnText.getText().toString().trim());
				PhraseHelper.insertPhrase(enText.getText().toString().trim(), category, syText.getText().toString().trim(), cnText.getText().toString().trim(), saText.getText().toString().trim());
                ToastUtil.showShort(context, "Saved!");
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

