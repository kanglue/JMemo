package com.ianglei.jmemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ianglei.jmemo.R;
import com.ianglei.jmemo.bean.Word;
import com.ianglei.jmemo.service.PlayMediaService;
import com.ianglei.jmemo.utils.L;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ianglei on 2017/12/2.
 */

public class PopupWord extends Activity implements View.OnClickListener {

    @BindView(R.id.word)
    TextView wordText;
    @BindView(R.id.pronounce)
    TextView pronounce;
    @BindView(R.id.translation)
    TextView translation;
    @BindView(R.id.tense)
    TextView tense;
    @BindView(R.id.sentence1)
    TextView sentence1;
    @BindView(R.id.sentence2)
    TextView sentence2;
    @BindView(R.id.sentence3)
    TextView sentence3;
    @BindView(R.id.sentence4)
    TextView sentence4;
    @BindView(R.id.sentence5)
    TextView sentence5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_word);
        ButterKnife.bind(this);

        wordText.setOnClickListener(this);
        sentence1.setOnClickListener(this);
        sentence2.setOnClickListener(this);
        sentence3.setOnClickListener(this);
        sentence4.setOnClickListener(this);
        sentence5.setOnClickListener(this);

        Intent intent = getIntent();
        Word word = intent.getParcelableExtra("word");
        L.d(word.getWord());

        wordText.setText(word.getWord());
        pronounce.setText(word.getPronounce());
        translation.setText(word.getTranslation());
        tense.setText(word.getTense());
        if(null != word.getSample(0)) {
            sentence1.setText(word.getSample(0).getSentence());
        }else if(null != word.getSample(1)){
            sentence2.setText(word.getSample(1).getSentence());
        }else if(null != word.getSample(2)){
            sentence3.setText(word.getSample(2).getSentence());
        }else if(null != word.getSample(3)){
            sentence4.setText(word.getSample(3).getSentence());
        }else if(null != word.getSample(4)){
            sentence5.setText(word.getSample(4).getSentence());
        }
    }

    public static void launchActivity(Context context, Word word)
    {
        Intent intent = new Intent(context, PopupWord.class);
        intent.putExtra("word", word);
        context.startActivity(intent);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.word:
                break;
            case R.id.sentence1:
                break;
            case R.id.sentence2:
                break;
            case R.id.sentence3:
                break;
            case R.id.sentence4:
                break;
            case R.id.sentence5:
                break;
            default:
                break;
        }
        //finish();
    }
}
