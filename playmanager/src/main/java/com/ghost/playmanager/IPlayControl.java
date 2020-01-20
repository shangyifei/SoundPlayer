package com.ghost.playmanager;


import com.ghost.playmanager.playcontroler.PlayController;

/**
 * Create by qwerty
 * Create on 2019-12-23
 **/
public interface IPlayControl {

    /**
     * 根据路径播放音乐
     *
     * @param path 路径
     */
    void play(String path, boolean looping);

    /**
     * 根据路径暂停音乐
     *
     * @param path 路径
     */
    void pause(String path);

    /**
     * 根据路径停止音乐
     *
     * @param path 路径
     */
    void stop(String path);

    /**
     * 根据资源ID播放音乐
     *
     * @param resId 资源id
     */
    void play(int resId, boolean looping);

    /**
     * 根据资源ID暂停音乐
     *
     * @param resId 资源id
     */
    void pause(int resId);

    /**
     * 根据资源ID停止音乐
     *
     * @param resId 资源id
     */
    void stop(int resId);

    boolean haveChannel(String path);

    boolean isChannelPlaying(String path);
    /**
     * 释放通道
     *
     * @param path
     */
    void releaseChannel(String path);

    /**
     * 停止所有通道播放音乐
     */
    void stopAllChannel();

    /**
     * 设置通道音量
     *
     * @param leftVolume  通道左声道音量，通道右声道音量
     * @param rightVolume
     */
    void setChannelVolume(String path, float leftVolume, float rightVolume);

    /**
     * 清除所有通道
     */
    void clearChannel();

    int channelIndexOfPool(String path);

    boolean haveChannelPlaying();

    int getChannelCount();

    void setPlayCallback(PlayController.PlayCallback callback);
    interface PlayCallback{
        void onDownloadProgress(String path, int progress);
        void onPlay(String path);
        void onError(String path);
    }

}
