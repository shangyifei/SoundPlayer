package com.ghost.playmanager;

import android.content.Context;

import com.ghost.playmanager.playcontroler.PlayController;

/**
 * Create by qwerty
 * Create on 2019-12-23
 * 播放管理器
 **/
public class PlayManager implements IPlayControl {

    private IPlayControl playControl;

    private static PlayManager instance;

    public static PlayManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PlayManager.class) {
                if (instance == null) {
                    instance = new PlayManager(context);
                }
            }
        }
        return instance;
    }

    private PlayManager(Context context) {
        playControl = new PlayController(context.getApplicationContext(), true);
    }

    @Override
    public void play(String path, boolean looping) {
        playControl.play(path, looping);
    }

    @Override
    public void pause(String path) {
        playControl.pause(path);
    }

    @Override
    public void stop(String path) {

        playControl.stop(path);
    }

    @Override
    public void play(int resId, boolean looping) {
        playControl.play(resId, looping);
    }

    @Override
    public void pause(int resId) {
        playControl.pause(resId);
    }

    @Override
    public void stop(int resId) {
        playControl.stop(resId);
    }

    @Override
    public void stopAllChannel() {
        playControl.stopAllChannel();
    }

    @Override
    public void clearChannel() {
        playControl.clearChannel();
    }


    @Override
    public void releaseChannel(String path) {
        playControl.releaseChannel(path);
    }

    @Override
    public void setChannelVolume(String path, float leftVolume, float rightVolume) {
        playControl.setChannelVolume(path, leftVolume, rightVolume);
    }

    @Override
    public boolean haveChannel(String path) {
        return playControl.haveChannel(path);
    }

    @Override
    public boolean isChannelPlaying(String path) {
        return playControl.isChannelPlaying(path);
    }

    @Override
    public int channelIndexOfPool(String path) {
        return playControl.channelIndexOfPool(path);
    }


    @Override
    public boolean haveChannelPlaying() {
        return playControl.haveChannelPlaying();
    }

    @Override
    public int getChannelCount() {
        return playControl.getChannelCount();
    }

    @Override
    public void setPlayCallback(PlayCallback callback) {
        playControl.setPlayCallback(callback);

    }
}
