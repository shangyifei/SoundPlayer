package com.ghost.playmanager.cache;

import android.content.Context;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.lifesense.logger.Logger;

import java.io.File;

/**
 * Create by qwerty
 * Create on 2019-12-24
 **/
public class CacheManager {

    private static CacheManager instance;

    /**
     * 缓存服务代理
     */
    private HttpProxyCacheServer proxy;

    public static CacheManager getInstance(Context context) {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private CacheManager(Context context) {
        this.proxy = new HttpProxyCacheServer(context);

    }

    public String getProxyUrl(String path, CacheListener cacheListener) {
        proxy.registerCacheListener(new CacheListener() {
            @Override
            public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
                Logger.e(url + "-cacheFile-" + cacheFile.toString() + "-percentsAvailable-" + percentsAvailable);
            }
        }, path);
        proxy.registerCacheListener(cacheListener,path);
        if ((path.startsWith("http") || path.startsWith("https"))) {
            return proxy.getProxyUrl(path);
        }
        return path;
    }

    public void cancel(){
//        if (proxy != null) {
//            proxy.isCached()
//        }
    }

}
