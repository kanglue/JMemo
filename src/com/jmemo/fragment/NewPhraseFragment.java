package com.jmemo.fragment;



import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jmemo.R;
import com.jmemo.db.Phrase;
import com.jmemo.db.VocabularyHelper;

public class NewPhraseFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View contextView = inflater.inflate(R.layout.new_phrase_fragment, container,
				false);
		ListView listView = (ListView) contextView
				.findViewById(R.id.phrase_list);
		
		NewPhraseAdapter listAdapter = new NewPhraseAdapter(getActivity(), listView, mHandler);
		listView.setAdapter(listAdapter);
		
		ArrayList<Phrase> list = VocabularyHelper.getPhraseList();

		listAdapter.addItem(list);
		
		return contextView;
	}
	
	
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            if (msg.what == 1 && msg.obj != null)
            {
                //fullScreenPlay((Channel)msg.obj);
//                Intent intent = new Intent(getActivity(), DetailActivity.class);
//                intent.putExtra("currentpos", (Parcelable)msg.obj);
//                getActivity().startActivity(intent);
            }
        }
    };
}
