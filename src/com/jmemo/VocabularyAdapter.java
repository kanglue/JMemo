package com.jmemo;

import java.util.ArrayList;

import com.jmemo.db.Word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VocabularyAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	
	private ArrayList<Word> lists = new ArrayList<Word>();

    public VocabularyAdapter(Context context){ 
        this.mInflater = LayoutInflater.from(context); 
    } 

    @Override 
    public int getCount() { 
        return lists.size(); 
    } 

    @Override 
    public Object getItem(int arg0) { 
        return null; 
    } 

    @Override 
    public long getItemId(int arg0) { 
        return 0; 
    } 

    @Override 

    public View getView(int position, View convertView, ViewGroup parent) { 

        ViewHolder holder = null; 
        View row = null;

        if (convertView == null) { 
            holder=new ViewHolder();   
            convertView = mInflater.inflate(R.layout.activity_jmemo, null); 
            holder.english = (TextView)convertView.findViewById(R.id.english);
            holder.chinese = (TextView)convertView.findViewById(R.id.chinese); 
            //holder.viewBtn = (ImageView)convertView.findViewById(R.id.view_btn); 
            //row = convertView.findViewById(R.id.list_item);
            convertView.setTag(holder); 
        } else { 
            holder = (ViewHolder)convertView.getTag(); 
        } 

//        holder.img.setBackgroundColor(Color.BLUE);
//        //holder.img.setBackgroundResource((Integer)mData.get(position).get("img")); 
//        holder.title.setText((String)lists.get(position).get("title")); 
//        holder.info.setText((String)lists.get(position).get("info")); 
//
//        holder.viewBtn.setOnClickListener(new View.OnClickListener() { 
//            @Override 
//            public void onClick(View v) { 
//                //showInfo();
//            } 
//        });

        return convertView; 
    } 
    
    /** 
     * listview中点击按键弹出对话框 
     */ 
//    public void showInfo(){ 
//        new AlertDialog.Builder(this) 
//        .setTitle("我的listview") 
//        .setMessage("介绍...") 
//        .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
//            @Override 
//            public void onClick(DialogInterface dialog, int which) { 
//            } 
//        }) 
//        .show(); 
//    } 
    
    class ViewHolder{ 
        public TextView english; 
        public TextView chinese; 
    } 
}
