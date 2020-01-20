package com.ghost.playmanager.channel;

/**
 * Create by qwerty
 * Create on 2019-12-23
 * 播放通道
 **/
public interface IPlayChannel {
    interface PlayCallback{
        void onDownloadProgress(int progress);
        void onPlay();
        void onError();
    }
    /**
     * @param path
     * @param looping
     * @param cache
     */
    void initChannel(String path, boolean looping, boolean cache);

    /**
     * @param resId
     * @param looping
     */
    void initChannel(int resId, boolean looping);

    /**
     * 准备并播放
     */
    void preparedAndPlay();

    /**
     * 播放音乐
     */
    void play();

    /**
     * 暂停音乐
     */
    void pause();

    /**
     * 停止音乐
     */
    void stop();

    /**
     * 释放通道
     */
    void release();

    /**
     * @return 是否播放
     */
    boolean isPlaying();

    void setVolume(float leftVolume, float rightVolume);

    void setPlayCallback(PlayCallback playCallback);

    void setLooping(boolean looping);

}
