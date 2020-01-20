package com.ghost.playmanager.playcontroler;


import android.content.Context;

import com.ghost.playmanager.IPlayControl;
import com.ghost.playmanager.channel.IPlayChannel;
import com.ghost.playmanager.channel.PlayChannel;

import java.util.ArrayList;
import java.util.List;


/**
 * Create by qwerty
 * Create on 2019-12-20
 * 播放服务
 **/
public class PlayController  implements IPlayControl {


    private List<PlayChannel> playChannelPool = new ArrayList<>();

    private boolean cache;

    private Context context;

    private PlayCallback playCallback;


    public PlayController(Context context, boolean cache) {
        this.context = context;
        this.cache = cache;
    }

    @Override
    public void play(String path, boolean looping) {
        PlayChannel curPlayChannel = selectPlayChannel(path);
        if (curPlayChannel != null) {
            curPlayChannel.setLooping(looping);
            curPlayChannel.play();
        } else {
            playNewChannelAndEnqueue(path, looping);
        }
    }

    /**
     * 查找播放通道
     *
     * @param path
     * @return
     */
    private PlayChannel selectPlayChannel(String path) {
        for (PlayChannel playChannel : playChannelPool) {
            if (playChannel.matchChannel(path)) {
                return playChannel;
            }
        }
        return null;
    }

    private void playNewChannelAndEnqueue(final String path, boolean looping) {
        PlayChannel newPlayChannel = new PlayChannel(context);
        newPlayChannel.setPlayCallback(new IPlayChannel.PlayCallback() {
            @Override
            public void onDownloadProgress(int progress) {
                callDownloadProgressCallback(path ,progress);
            }

            @Override
            public void onPlay() {
                callPlayCallback(path);
            }

            @Override
            public void onError() {
                callErrorCallback(path);
            }
        });
        newPlayChannel.initChannel(path, looping, cache);
        newPlayChannel.preparedAndPlay();
        playChannelPool.add(newPlayChannel);

    }

    @Override
    public void pause(String path) {
        PlayChannel playChannel = selectPlayChannel(path);
        if (playChannel != null) {
            playChannel.pause();
        }
    }

    @Override
    public void stop(String path) {
        PlayChannel playChannel = selectPlayChannel(path);
        if (playChannel != null) {
            playChannel.stop();
        }
    }

    @Override
    public void play(int resId, boolean looping) {
        PlayChannel curPlayChannel = selectPlayChannel(resId + "");
        if (curPlayChannel != null) {
            curPlayChannel.setLooping(looping);
            curPlayChannel.play();
        } else {
            playNewChannelAndEnqueue(resId, looping);
        }
    }

    private void playNewChannelAndEnqueue(final int resId, boolean looping) {
        PlayChannel newPlayChannel = new PlayChannel(context);
        newPlayChannel.setPlayCallback(new IPlayChannel.PlayCallback() {
            @Override
            public void onDownloadProgress(int progress) {
                callDownloadProgressCallback(String.valueOf(resId) ,progress);
            }

            @Override
            public void onPlay() {
                callPlayCallback(String.valueOf(resId));
            }

            @Override
            public void onError() {
                callErrorCallback(String.valueOf(resId));
            }
        });
        newPlayChannel.initChannel(resId, looping);
        newPlayChannel.preparedAndPlay();
        playChannelPool.add(newPlayChannel);
    }

    @Override
    public void pause(int resId) {
        PlayChannel playChannel = selectPlayChannel(resId + "");
        if (playChannel != null) {
            playChannel.pause();
        }
    }

    @Override
    public void stop(int resId) {
        PlayChannel playChannel = selectPlayChannel(resId + "");
        if (playChannel != null) {
            playChannel.stop();
        }
    }

    @Override
    public void stopAllChannel() {
        for (PlayChannel playChannel : playChannelPool) {
            if (playChannel != null) {
                playChannel.stop();
            }
        }
    }

    @Override
    public void clearChannel() {
        for (PlayChannel playChannel : playChannelPool) {
            if (playChannel != null) {
                playChannel.setPlayCallback(null);
                playChannel.release();
            }
        }
        playChannelPool.clear();
    }

    @Override
    public boolean haveChannel(String path) {
        return selectPlayChannel(path) != null;
    }

    @Override
    public boolean isChannelPlaying(String path) {
        PlayChannel playChannel = selectPlayChannel(path);
        if (playChannel != null) {
            return playChannel.isPlaying();
        }
        return false;
    }


    @Override
    public void releaseChannel(String path) {
        PlayChannel playChannel = selectPlayChannel(path);
        if (playChannel != null) {
            playChannel.setPlayCallback(null);
            playChannel.release();
            playChannelPool.remove(playChannel);
        }
    }

    @Override
    public void setChannelVolume(String path, float leftVolume, float rightVolume) {
        PlayChannel playChannel = selectPlayChannel(path);
        if (playChannel != null) {
            playChannel.setVolume(leftVolume, rightVolume);
        }
    }

    @Override
    public boolean haveChannelPlaying() {
        for (PlayChannel playChannel : playChannelPool) {
            if (playChannel.isPlaying()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int channelIndexOfPool(String path) {
        for (int i = 0; i < playChannelPool.size(); i++) {
            PlayChannel playChannel = playChannelPool.get(i);
            if (playChannel.matchChannel(path)) {
                return i;
            }
        }
        return -1;
    }

    public int getChannelCount() {
        return playChannelPool.size();
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    @Override
    public void setPlayCallback(PlayCallback callback) {
        playCallback = callback;
    }

    private void callDownloadProgressCallback(String path, int progress){
        if (playCallback != null) {
            playCallback.onDownloadProgress(path,progress);
        }
    }

    private void callPlayCallback(String path){
        if (playCallback != null) {
            playCallback.onPlay(path);
        }
    }

    private void callErrorCallback(String path){
        if (playCallback != null) {
            playCallback.onError(path);
        }
    }
}
