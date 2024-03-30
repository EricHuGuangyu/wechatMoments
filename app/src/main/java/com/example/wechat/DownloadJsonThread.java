package com.example.wechat;

import android.os.Handler;
import android.util.Log;

import com.example.wechat.gson.TweetBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class DownloadJsonThread extends Thread {

    private String text;
    private final static String TAG = "DownloadJsonThread";
    private List<TweetBean> mList;
    private HashMap<String,String> mProfileMap = new HashMap<>();

    //图片链接地址
    private final static int DATA_TYPE_PROFILE = 2;
    private final static int DATA_TYPE_TWEETS = 1;
    //UI线程中传递的参数用于向UI线程传递消息和数据
    private int mDataType;
    private Handler mHandler;
    //获取成功massage
    private final static int SUCCESS_MSG_TWEETS = 1;

    private final static int SUCCESS_MSG_PROFILE = 2;

    //获取失败返回massage
    private final static int FAILURE_MSG = 0;

    /*
     * 带参构造方法
     * */
    public DownloadJsonThread(int type, Handler mHandler) {
        this.mDataType = type;
        this.mHandler = mHandler;
    }

    /*
     * 继承重写Thread的方法
     * 实现异步请求图片
     * */
    @Override
    public void run() {
        try {
            //你的URL
            Log.i(TAG,"mDataType:" + mDataType);
            String url_s = (mDataType == DATA_TYPE_PROFILE)?
                    "http://thoughtworks-ios.herokuapp.com/user/jsmith"
                    :"http://thoughtworks-ios.herokuapp.com/user/jsmith/tweets";

            URL url = new URL(url_s);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置连接属性。不喜欢的话直接默认也阔以
            conn.setConnectTimeout(5000);//设置超时
            conn.setUseCaches(false);//数据不多不用缓存了

            //这里连接了
            conn.connect();
            //这里才真正获取到了数据
            InputStream inputStream = conn.getInputStream();
            InputStreamReader input = new InputStreamReader(inputStream);
            BufferedReader buffer = new BufferedReader(input);
            if(conn.getResponseCode() == 200){
                String inputLine;
                StringBuffer resultData  = new StringBuffer();
                while((inputLine = buffer.readLine())!= null){
                    resultData.append(inputLine);
                }
                text = resultData.toString();
                Log.i(TAG,"text from web:" + text);
                if(mDataType == DATA_TYPE_PROFILE) {
                    JSONObject jsonObject = new JSONObject(text);
                    mProfileMap.put("profile-image",jsonObject.getString("profile-image"));
                    mProfileMap.put("avatar",jsonObject.getString("avatar"));
                    mProfileMap.put("nick",jsonObject.getString("nick"));
                    mProfileMap.put("username",jsonObject.getString("username"));
                    mHandler.obtainMessage(SUCCESS_MSG_PROFILE, mProfileMap).sendToTarget();
                }else {
                    Type type = new TypeToken<List<TweetBean>>() {}.getType();
                    mList = new Gson().fromJson(text, type);
                    if(mList != null && mList.size() > 0) {
                        Log.i(TAG, ""+ mList.size());
                        for (int i = 0; i < mList.size(); i++) {
                            Log.i(TAG, mList.get(i).toString());
                        }
                    }else {
                        Log.i(TAG,"list is null !");
                    }

                    mHandler.obtainMessage(SUCCESS_MSG_TWEETS, mList).sendToTarget();
                }
            }else {
                //获取图片失败返回失败massage
                mHandler.obtainMessage(FAILURE_MSG).sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
