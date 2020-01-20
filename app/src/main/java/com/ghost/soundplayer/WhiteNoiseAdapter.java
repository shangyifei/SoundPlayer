package com.ghost.soundplayer;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ghost.playmanager.PlayManager;

import java.util.List;

/**
 * Create by qwerty
 * Create on 2019-12-26
 **/
public class WhiteNoiseAdapter extends RecyclerView.Adapter<WhiteNoiseAdapter.WhiteNoiseViewHolder> {


    public static class PlayState {
        String path;
        boolean play;
        String title;
        int themeColor;

        public PlayState() {
        }

        public PlayState(String path, boolean play, String title, int themeColor) {
            this.path = path;
            this.play = play;
            this.title = title;
            this.themeColor = themeColor;
        }
    }

    private PlayManager playManager;

    private List<PlayState> dataList;


    public WhiteNoiseAdapter(PlayManager playManager) {
        this(null, playManager);
    }

    public WhiteNoiseAdapter(List<PlayState> dataList, PlayManager playManager) {
        this.dataList = dataList;
        this.playManager = playManager;
    }

    @NonNull
    @Override
    public WhiteNoiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WhiteNoiseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_white_noise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final WhiteNoiseViewHolder holder, int position) {
        final PlayState playState = getData(position);
        if (playState != null) {
            holder.tvWhiteNoise.setText(playState.title);
            holder.vWhiteNoiseIcon.setBackgroundDrawable(getDrawable(playState));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playManager.isChannelPlaying(playState.path)) {
                    playManager.pause(playState.path);
                    notifyDataSetChanged();
                } else {
                    playManager.play(playState.path, true);
                }

            }
        });

    }

    public GradientDrawable getDrawable(PlayState playState) {
        if (playManager.haveChannel(playState.path)) {
            if (playManager.isChannelPlaying(playState.path)) {
                return getPlayingDrawable(playState.themeColor);
            } else {
                return getPauseDrawable(playState.themeColor);
            }
        } else {
            return getUnSelectDrawable(playState.themeColor);
        }
    }

    private GradientDrawable getUnSelectDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setSize(120, 120);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setAlpha(64);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    private GradientDrawable getPlayingDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setSize(120, 120);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setAlpha(255);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    private GradientDrawable getPauseDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setSize(120, 120);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setAlpha(128);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public PlayState getData(int position) {
        if (dataList == null || position < 0 || position > dataList.size() - 1) {
            return null;
        }
        return dataList.get(position);
    }

    public void setDataList(List<PlayState> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public List<PlayState> getDataList() {
        return dataList;
    }

    class WhiteNoiseViewHolder extends RecyclerView.ViewHolder {
        private TextView tvWhiteNoise;
        private View vWhiteNoiseIcon;

        public WhiteNoiseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWhiteNoise = itemView.findViewById(R.id.tv_white_noise_title);
            vWhiteNoiseIcon = itemView.findViewById(R.id.v_white_noise_icon);
        }
    }

}
