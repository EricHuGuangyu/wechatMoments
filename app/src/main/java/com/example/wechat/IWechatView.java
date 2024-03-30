package com.example.wechat;

public interface IWechatView {
    /**
     * 根据数据设置view
     * @param result
     */
    void setView(String result);

    /**
     * 根据数据设置view
     * @param drag
     */
    void freshView(boolean drag);
}
