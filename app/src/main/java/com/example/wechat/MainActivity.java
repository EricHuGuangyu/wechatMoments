package com.example.wechat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wechat.adapter.RecyclerViewWithHeadAdapter;
import com.example.wechat.gson.TweetBean;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //获取成功massage
    private final static int SUCCESS_MSG_PROFILE = 2;
    private final static int SUCCESS_MSG_TWEET = 1;
    private final static int SWIPE_REFRESH = 3;
    //获取失败返回massage
    private final static int FAILURE_MSG = 0;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerViewWithHeadAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DownloadJsonThread mThread1;
    private DownloadJsonThread mThread2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewWithHeadAdapter(this);
        initImageLoader();
        initData();
        initSwipeFresh();
    }

    private void initData() {
        mThread1 = new DownloadJsonThread(SUCCESS_MSG_TWEET, mHandler);
        mThread1.start();

        mThread2 = new DownloadJsonThread(SUCCESS_MSG_PROFILE, mHandler);
        mThread2.start();
    }

    private void initView(List<TweetBean> list) {
        if(mAdapter != null) {
            mAdapter.setList(list);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initProfileView(HashMap<String,String> map) {
        if(mAdapter != null) {
            mAdapter.setMap(map);
        }
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .imageDownloader(new BaseImageDownloader(this,5000,30000)).build();

//        HttpParams params = new BasicHttpParams();
//        // Turn off stale checking. Our connections break all the time anyway,
//        // and it's not worth it to pay the penalty of checking every time.
//        HttpConnectionParams.setStaleCheckingEnabled(params, false);
//        // Default connection and socket timeout of 10 seconds. Tweak to taste.
//        HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
//        HttpConnectionParams.setSoTimeout(params, 10 * 1000);
//        HttpConnectionParams.setSocketBufferSize(params, 8192);
//
//        // Don't handle redirects -- return them to the caller. Our code
//        // often wants to re-POST after a redirect, which we must do ourselves.
//        HttpClientParams.setRedirecting(params, false);
//        // Set the specified user agent and register standard protocols.
//        HttpProtocolParams.setUserAgent(params, "some_randome_user_agent");
//        SchemeRegistry schemeRegistry = new SchemeRegistry();
//        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
//
//        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);
//
//
//        ImageLoaderConfiguration configuration =
//                new ImageLoaderConfiguration
//                        .Builder(this)
//                        .threadPoolSize(1)
//                        .memoryCache(new WeakMemoryCache())
//                        .imageDownloader(new HttpClientImageDownloader(new DefaultHttpClient(manager, params)))
//                        .build();

        ImageLoader.getInstance().init(configuration);

    }

    private void initSwipeFresh() {
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeFreshView);
        // 设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        mySwipeRefreshLayout.setProgressViewOffset(true, 50, 200);

        // 设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        mySwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);

        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mySwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // 通过 setEnabled(false) 禁用下拉刷新
        mySwipeRefreshLayout.setEnabled(true);

        // 设定下拉圆圈的背景
        //mSwipeLayout.setProgressBackgroundColor(R.color.red);

        /*
         * 设置手势下拉刷新的监听
         */
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(!mHandler.hasMessages(SWIPE_REFRESH)) {
                            mHandler.sendEmptyMessageDelayed(SWIPE_REFRESH,2000);
                        }
                    }
                }
        );
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS_MSG_TWEET:
                    Log.i("DownloadJsonThread","get tweets success");
                    initView((List<TweetBean>)msg.obj);
                    break;
                case SUCCESS_MSG_PROFILE:
                    Log.i("DownloadJsonThread","get profile success");
                    initProfileView((HashMap<String,String>)msg.obj);
                    break;
                case FAILURE_MSG:
                    Toast.makeText(MainActivity.this,"get from web failed",Toast.LENGTH_SHORT).show();
                    Log.i("DownloadJsonThread","get from web failed");
                    break;
                case SWIPE_REFRESH:
                    Log.i("DownloadJsonThread","get tweets success");
                    Log.i("DownloadJsonThread","onRefresh");
                    initData();
                    mySwipeRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                    break;
            }

        }
    };
}
