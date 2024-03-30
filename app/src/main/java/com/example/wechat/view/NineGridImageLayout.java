package com.example.wechat.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.wechat.R;
import com.example.wechat.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import javax.sql.DataSource;

public class NineGridImageLayout extends NineGridLayout {
    protected static final int MAX_W_H_RATIO = 3;
    private final static String TAG = "DownloadJsonThread";

    public NineGridImageLayout(Context context) {
        super(context);
    }

    public NineGridImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private RequestListener mRequestListener = new RequestListener() {
        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            Log.i(TAG,"Glide Exception:"+e);
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }

    };

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth) {
        Log.i(TAG,"displayImage url2:"+url);
        Glide.with(mContext).load(url).placeholder(R.drawable.ic_launcher_foreground).listener(mRequestListener).into(imageView);
        //这里是只显示一张图片的情况，显示图片的宽高可以根据实际图片大小自由定制，parentWidth 为该layout的宽度
//        ImageLoader.getInstance().displayImage(url, imageView, ImageLoaderUtil.getPhotoImageOption(), new ImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
//                        int w = bitmap.getWidth();
//                        int h = bitmap.getHeight();
//
//                        int newW;
//                        int newH;
//                        if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
//                            newW = parentWidth / 2;
//                            newH = newW * 5 / 3;
//                        } else if (h < w) {//h:w = 2:3
//                            newW = parentWidth * 2 / 3;
//                            newH = newW * 2 / 3;
//                        } else {//newH:h = newW :w
//                            newW = parentWidth / 2;
//                            newH = h * newW / w;
//                        }
//                        setOneImageLayoutParams(imageView, newW, newH);
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String imageUri, View view) {
//
//                    }
//                });
        return false;// true 代表按照九宫格默认大小显示(此时不要调用setOneImageLayoutParams)；false 代表按照自定义宽高显示。
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        Log.i(TAG,"displayImage url1:"+url);
        //ImageLoaderUtil.getImageLoader(mContext).displayImage(url, imageView, ImageLoaderUtil.getPhotoImageOption());
        Glide.with(mContext).load(url).placeholder(R.drawable.ic_launcher_foreground).listener(mRequestListener).into(imageView);
    }


    @Override
    protected void onClickImage(int i, String url, List<String> urlList) {
        Toast.makeText(mContext, "点击了图片" + url, Toast.LENGTH_SHORT).show();
    }

}
