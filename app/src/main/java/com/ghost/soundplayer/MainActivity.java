package com.ghost.soundplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ghost.playmanager.IPlayControl;
import com.ghost.playmanager.PlayManager;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private PlayManager playManager;


    private RecyclerView rvWhiteNoise;
    private WhiteNoiseAdapter whiteNoiseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playManager = PlayManager.getInstance(this);
        playManager.setPlayCallback(new IPlayControl.PlayCallback() {
            @Override
            public void onDownloadProgress(String path, int progress) {

            }

            @Override
            public void onPlay(String path) {
                if (whiteNoiseAdapter != null) {
                    whiteNoiseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String path) {

            }
        });

        rvWhiteNoise = findViewById(R.id.rv_white_noise);
        rvWhiteNoise.setLayoutManager(new GridLayoutManager(this, 4));
        List<WhiteNoiseAdapter.PlayState> playStateList = new ArrayList<>();
        playStateList.add(new WhiteNoiseAdapter.PlayState("http://music.163.com/song/media/outer/url?id=1376577855.mp3", false, "测试1", getResources().getColor(R.color.colorAccent)));
        playStateList.add(new WhiteNoiseAdapter.PlayState("http://music.163.com/song/media/outer/url?id=29785409.mp3", false, "测试2", getResources().getColor(R.color.colorPrimary)));
        playStateList.add(new WhiteNoiseAdapter.PlayState("http://music.163.com/song/media/outer/url?id=611923.mp3", false, "测试3", getResources().getColor(R.color.colorPrimaryDark)));
        playStateList.add(new WhiteNoiseAdapter.PlayState("http://music.163.com/song/media/outer/url?id=1317043661.mp3", false, "测试4", getResources().getColor(R.color.default_blue_light)));


        whiteNoiseAdapter = new WhiteNoiseAdapter(playStateList, playManager);
        rvWhiteNoise.setAdapter(whiteNoiseAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
