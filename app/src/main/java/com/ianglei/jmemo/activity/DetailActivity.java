package com.ianglei.jmemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ianglei.jmemo.JMemoApplication;
import com.ianglei.jmemo.R;
import com.ianglei.jmemo.bean.Listening;
import com.ianglei.jmemo.service.PlayMediaService;
import com.ianglei.jmemo.service.PlayMediaService.*;
import com.ianglei.jmemo.utils.JUtils;
import com.ianglei.jmemo.utils.L;
import com.ianglei.jmemo.utils.StringUtils;
import com.ianglei.jmemo.utils.ToastUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by j00255628 on 2015/11/5.
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
{
    private static final String TAG = DetailActivity.class.getSimpleName();
    private PlayMediaService playMediaService;
    private PlayerServiceConnection playerServiceConnection;

    private String transcript;
    private TextView transcriptTextView;
    private LinearLayout loadingBar;
    public ProgressBar downloadBar;
    private RelativeLayout playerLayout;
    private Button playButton;
    private Button replayButton;
    private Button restoreButton;
    //	private ImageButton pauseBtn;
    //	private ImageButton stopBtn;
    public static String mp3PathToPlay;
    /* 定义进度条 */
    public static SeekBar seekBar = null;

    private boolean isPlay = false;  //是否在播放
    private TextView playTime;  //当前播放时间
    private TextView allTime;  //总的播放时间
    private static int nowBarSize;  //当前seekbar进度
    private static int seekBarSize;  //seekbar的总大小
    private Intent intent;
    private Listening item;
    private boolean isPlayed = false;
    private boolean isDragPlay = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        item = (Listening)intent.getParcelableExtra("currentpos");

        playerServiceConnection = new PlayerServiceConnection();

        Intent serviceIntent = new Intent(this, PlayMediaService.class);
        bindService(serviceIntent, playerServiceConnection, BIND_AUTO_CREATE);

        init(item);

        if(transcript == null)
        {
            TranscriptTask task = new TranscriptTask();
            task.execute(item);
        }
        else {
            loadingBar.setVisibility(View.GONE);
            transcriptTextView.setText(transcript);
        }

        boolean isExist = false;
        if(item.getMp3path() != null)
        {
            isExist = new File(item.getMp3path()).exists();
        }

        if(null == item.getMp3path() || item.getMp3path().startsWith("http") || !isExist)
        {
            L.d("开始下载Mp3: " + item.getMp3path());
            Mp3Task task = new Mp3Task();
            task.execute(item);
        }
        else {
            playerLayout.setVisibility(View.VISIBLE);
//            startPlay(item);
        }

//        listererTelephony(); //来电话监听
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {

    }

    class PlayerServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个MsgService对象
            L.d("onServiceConnected called");
            playMediaService = ((PlayerBinder)service).getPlayMediaService();
            playMediaService.setActivity(DetailActivity.this);
            listererTelephony();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playMediaService = null;
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void init(Listening item) {
        TextView titleTextView = (TextView)findViewById(R.id.ItemTitle);
        transcriptTextView = (TextView)findViewById(R.id.Transcript);
        ImageView coverImgView = (ImageView)findViewById(R.id.coverimg);
        loadingBar = (LinearLayout)findViewById(R.id.pb);
        downloadBar = (ProgressBar)findViewById(R.id.download_progress);

        playerLayout = (RelativeLayout)findViewById(R.id.player);
        playerLayout.setVisibility(View.GONE);
        playButton = (Button)findViewById(R.id.play);
        playButton.setOnClickListener(this);
        replayButton = (Button)findViewById(R.id.replay);
        replayButton.setOnClickListener(this);
        restoreButton = (Button)findViewById(R.id.restore);
        restoreButton.setOnClickListener(this);

        playTime = (TextView) findViewById(R.id.play_time);
        allTime = (TextView) findViewById(R.id.all_time);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(item.getTitle());
        //返回按钮
        toolbar.setNavigationIcon(R.drawable.bt_back_normal);
//        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //从本地加载图片
        Bitmap bitmap = BitmapFactory.decodeFile(item.getCoverpath());
        L.d("封面图片: " + item.getCoverpath());
        coverImgView.setImageBitmap(bitmap);

        titleTextView.setText(item.getTitle());

        //从数据库中查询脚本
        JMemoApplication.getDataSource().queryDetailById(item);
        transcript = item.getTranscript();
    }

    private void listererTelephony() {
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(new MobliePhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);
    }
    private class MobliePhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    playMediaService.play();
                    isPlay = true;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    playMediaService.pause();
                    isPlay = false;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    playMediaService.pause();
                    isPlay = false;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        L.d(keyCode + ", " + event);
        //放开事件处理，keyCode为按键的键值，event 为按键事件的对像，其中包含了按键的信息。
        switch(keyCode){
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS://耳机三个按键是的上键，注意并不是耳机上的三个按键的物理位置的上下。
            case KeyEvent.KEYCODE_VOLUME_UP:
                //按键功能定义的处理。
                backToPlay();
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE://耳机单按键的按键或三按键耳机的中间按键。
            case KeyEvent.KEYCODE_HEADSETHOOK://耳机单按键的按键或三按键耳机的中间按键。与上面的按键可能是相同的，具体得看驱动定义。
            case KeyEvent.KEYCODE_MEDIA_PLAY:
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                //按键功能定义的处理。一般与KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE、键的处理一样。
                play();
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT://耳机三个按键是的下键。
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                //按键功能定义的处理。
                restoreToPlay();
                break;
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                break;
        }
        return false;//为true,则其它后台按键处理再也无法处理到该按键，为false,则其它后台按键处理可以继续处理该按键事件。
    }

    private void backToPlay()
    {
        int now = seekBar.getProgress();
        L.d("now: " + now);
        int to = now - (10 * 1000);
        if(to < 0)
            to = 0;
        L.d("to: " + to);

        playMediaService.start();
        playMediaService.pause();
//        playMediaService.seekTo(item.getTitle(), item.getMp3path());
        playMediaService.seekTo(to);
        onProgressChanged(seekBar, to, false);
        playMediaService.play();
    }

    private void restoreToPlay()
    {

    }

//    private void startPlay(Listening item) {
//
//        playMediaService.stop();//播放之前，先停止播放，不管有没有在播放，都先停止
//        intent = new Intent();
//        intent.putExtra("name", item.getTitle());
//        intent.putExtra("music", item.getMp3path());
////        intent.setClass(DetailActivity.this, PlayMusicService.class); //启动service，播放音乐
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        startService(intent);
//    }

    public void updateSeekBar(int size) {   //更新seekbar的方法
        seekBar.setProgress(size);
    }
    public void backSeekBar(int size) {   //更新seekbar的方法
        seekBar.setProgress(size);
    }
    public void updateSeekCach(int size) { //更新seekbar第2进度条的方法
        seekBar.setSecondaryProgress(size);
    }

    public void setIsplay(boolean isPlay) {  //改变当前播放状态数据标志
        if (isPlay) {
            this.isPlay = true;
        } else {
            this.isPlay = false;
        }
    }
    public void refreshUI(int size) {     //音乐初始化 后，调用这里
        allTime.setText(StringUtils.generateTime(size));  //显示播放总时间
//        playButton.setBackgroundResource(R.drawable.pause);//设置播放按钮的背景
        seekBar.setMax(size); //设置seekbar的总大小
        seekBar.setEnabled(true); //可以拖动seekbar了
    }
    public void updateTime(int nowPlayTime) { //更新播放时间
        playTime.setText(StringUtils.generateTime(nowPlayTime));
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play) {     //播放按钮
            play();
        }
        else if(v.getId() == R.id.replay)
        {
            backToPlay();
        }
        else if(v.getId() == R.id.restore)
        {
            restoreToPlay();
        }
        //		else if (v.getId() == R.id.next) { //下一去
        //			PlayMusicService.playNextMusic();
        //			id += 1;
        //			if (id >= beans.size()) {
        //				id = 0;
        //			}
        //			setName(id);
        //		} else if (v.getId() == R.id.font) {//上一曲
        //			id -= 1;
        //			PlayMusicService.playFontMusic();
        //			if (id < 0) {
        //				id = beans.size() - 1;
        //			}
        //			setName(id);

    }

    private void play() {
        if(!isPlayed) {//只有首次
            playMediaService.play(item.getTitle(), item.getMp3path());
            isPlayed = true;
            playButton.setText("暂停");
        }else
        {
            if(isPlay)
            {
                playMediaService.pause();
                playButton.setText("播放");
            }
            else
            {
                playMediaService.play();
                playButton.setText("暂停");
            }
        }

    }

//    public void setPlayButton(boolean isPlay)
//    {
//        if (isPlay) {
//            playButton.setText("暂停");
//        } else {
//            playButton.setText("播放");
//        }
//    }

    public void hideBar() { //隐藏圈圈
        loadingBar.setVisibility(View.GONE);
    }
    public void showBar() {//显示圈圈
        loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        playMediaService.release();
        unbindService(playerServiceConnection);
//        if (!isPlay && intent != null) {
//            stopService(intent);
//        }
//        if (playMediaService.isPause) {
//            playMediaService.stop();
//        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {   //拖动seekbar时候，调用这里
        L.d("seekbar progress: " + progress);
        if (fromUser) {
//            playMediaService.start();
            if(isDragPlay)
            {
                playMediaService.play();
            }
            else
            {
                playMediaService.pause();
            }
            playMediaService.seekTo(progress);  //播放跳转到拖动位置
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { //开始拖动seekbar
        isDragPlay = isPlay;
        playMediaService.pause();
        nowBarSize = seekBar.getProgress();
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { //拖动seekbar结束
        seekBarSize = seekBar.getProgress();
        if (nowBarSize >= seekBarSize) {
            //PlayMusicService.setFlag();
        }
    }

    class TranscriptTask extends AsyncTask<Listening, Integer, String>
    {
        Listening item;

        @Override
        protected String doInBackground(Listening... params)
        {
            item = params[0];
            Document doc;
            try
            {
                doc = Jsoup.connect(item.getLink()).timeout(45000).get();
                int pcount = doc.select("div.text p, div.text br").size();

                StringBuilder builder = new StringBuilder(2048);

                for(int i = 0; i < pcount; i++)
                {
                    Element dateElement = doc.select("div.text p, div.text br").get(i);
                    String dateText = dateElement.text();
                    builder.append(dateText);
                    builder.append("\n");
                }

                transcript = builder.toString();
                return transcript;
            } catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            loadingBar.setVisibility(View.GONE);
            playerLayout.setVisibility(View.VISIBLE);
            transcriptTextView.setText(result);
            item.setTranscript(result);
            JMemoApplication.getDataSource().updateTranscript(item);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
        }
    }


class Mp3Task extends AsyncTask<Listening, Integer, String>
{
    Listening item;

    @Override
    protected String doInBackground(Listening... params) {
        Document doc;
        String localPath = null;
        String localMp3Path = null;

        try {
            item = params[0];
            doc = Jsoup.connect(params[0].getLink()).timeout(45000).get();

            //MP3 and pdf has no fixed order
            Element aElement1 = doc.select("a.download").get(0);
            Element aElement2 = doc.select("a.download").get(1);

            String[] urls = new String[2];

            urls[0] = aElement1.attr("href");
            urls[1] = aElement2.attr("href");

            for (String s : urls) {
                localPath = JUtils.getResourcePath(s);
                if (localPath.endsWith("mp3")) {
                    localMp3Path = localPath;
                }

                try {
                    URL url = new URL(s);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    //urlConnection.setDoOutput(true);
                    urlConnection.connect();

                    BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(localPath));

                    InputStream inputStream = urlConnection.getInputStream();

                    int totalSize = urlConnection.getContentLength();

                    //variable to store total downloaded bytes
                    int downloadedSize = 0;

                    //create a buffer...
                    byte[] buffer = new byte[1024000];
                    int bufferLength = 0; //used to store a temporary size of the buffer

                    //now, read through the input buffer and write the contents to the file
                    while ((bufferLength = inputStream.read(buffer)) != -1) {
                        //add the data in the buffer to the file in the file output stream (the file on the sd card
                        fileOutput.write(buffer, 0, bufferLength);
                        //add up the size so we know how much is downloaded
                        downloadedSize += bufferLength;
                        //Log.i(TAG, "download " + downloadedSize + " of " + totalSize);

                        publishProgress(Integer.valueOf(100 * downloadedSize / totalSize));

                    }
                    Log.i(TAG, "download " + downloadedSize + " of " + totalSize);
                    //close the output stream when done
                    fileOutput.close();
                    urlConnection.disconnect();

                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                    L.e(e1.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return localMp3Path;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            downloadBar.setProgress(values[0]);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            downloadBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            downloadBar.setVisibility(View.GONE);
            playerLayout.setVisibility(View.VISIBLE);

            item.setMp3path(result);
            JMemoApplication.getDataSource().updateMp3Path(item);
            ToastUtil.showShort(getBaseContext(), "Mp3 downloaded.");
        }
    }

//    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            String msg = "";
//            switch (menuItem.getItemId()) {
//                case R.id.action_settings:
//                    msg += "Click setting";
//                    break;
//            }
//
//            if(!msg.equals("")) {
//                Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_SHORT).show();
//            }
//            return true;
//        }
//    };

}
