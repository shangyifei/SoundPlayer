package com.ghost.playmanager.channel;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.PowerManager;


import com.danikula.videocache.CacheListener;
import com.ghost.playmanager.cache.CacheManager;
import com.lifesense.logger.Logger;

import java.io.File;
import java.io.IOException;



/**
 * Create by qwerty
 * Create on 2019-12-20
 * 播放通道
 **/
public class PlayChannel implements IPlayChannel, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final String TAG = PlayChannel.class.getSimpleName();

    private MediaPlayer mediaPlayer;

    private boolean isPrepared;

    private boolean isPreparing;

    private boolean isPause;

    private String path;

    private Context context;

    private WifiManager.WifiLock wifiLock;

    private CacheManager cacheManager;

    private PlayCallback playCallback;


    public PlayChannel(Context context) {
        this.context = context;
        this.cacheManager = CacheManager.getInstance(context);
    }

    @Override
    public void initChannel(String path, boolean looping, boolean cache) {
        if (path == null) return;
        this.path = path;
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(cache ? cacheManager.getProxyUrl(path, new CacheListener() {
                @Override
                public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
                    if (playCallback != null) {
                        playCallback.onDownloadProgress(percentsAvailable);
                    }
                }
            }) : path);
            mediaPlayer.setLooping(looping);
            mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnPreparedListener(this);
            wifiLock = ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
            Logger.d(this.path + "：初始化通道");
            wifiLock.acquire();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initChannel(int resId, boolean looping) {
        this.path = resId + "";
        mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.setLooping(looping);
        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        wifiLock = ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        Logger.d(this.path + "：初始化通道");
        wifiLock.acquire();
    }

    @Override
    public void preparedAndPlay() {
        isPreparing = true;
        mediaPlayer.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPreparing = false;
        isPrepared = true;
        isPause = false;
        Logger.d(this.path + "：通道已经准备");
        play();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        isPreparing = false;
        isPause = false;
        Logger.d(this.path + "：通道错误");
        if (playCallback != null) {
            playCallback.onError();
        }
        return true;
    }

    @Override
    public void play() {
        if (isPrepared()) {
            mediaPlayer.start();
            if (playCallback != null) {
                playCallback.onPlay();
            }

            Logger.d(this.path + "：通道开始播放");
        } else if (!isPreparing) {
            preparedAndPlay();
        }
    }

    @Override
    public void pause() {
        if (isPrepared() && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
            Logger.d(this.path + "：通道已经暂停");
        }
    }

    @Override
    public void stop() {
        if (isPrepared()) {
            mediaPlayer.stop();
            isPrepared = false;
            Logger.d(this.path + "：通道已经停止");
        }
    }

    @Override
    public boolean isPlaying() {
        if (isPrepared) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public boolean isPause() {
        return isPause;
    }

    public void seekTo(int progress) {
        if (isPrepared()) {
            mediaPlayer.seekTo(progress);
        }
    }

    public boolean isPrepared() {
        if (!isPrepared) {
            Logger.e(TAG, "播放器没有准备好");
        }
        return isPrepared;
    }

    public boolean matchChannel(String path) {
        return (path != null && path.equals(this.path));
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public void release() {
        if (isPrepared) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        if (wifiLock.isHeld()) {
            wifiLock.release();
        }
        Logger.d(this.path + "：通道已经释放");
        isPrepared = false;
        path = null;
    }

    @Override
    public void setPlayCallback(PlayCallback playCallback) {
        this.playCallback = playCallback;
    }

    @Override
    public void setLooping(boolean looping) {
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(looping);
        }
    }
}
