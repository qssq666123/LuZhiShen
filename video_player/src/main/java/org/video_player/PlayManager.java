package org.video_player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by wuyr on 12/3/16 5:53 PM.
 */

public class PlayManager {

    private volatile static PlayManager mInstance;
    private MediaPlayer mPlayer;
    private PlayListener mListener, mFullScreenListener;
    private PlayStatus mStatus = PlayStatus.NORMAL;
    private VideoPlayer.Status mPlayerData;
    private VideoPlayer mVideoPlayer;

    private PlayManager() {
        mPlayer = new MediaPlayer();
    }

    public static PlayManager getInstance() {
        LogUtil.print("get play manager instance");
        if (mInstance == null)
            synchronized (PlayManager.class) {
                if (mInstance == null)
                    mInstance = new PlayManager();
            }
        return mInstance;
    }

    void prepareAsync(final Context context, final String url) {
        LogUtil.print("prepareAsync");
        if (TextUtils.isEmpty(url)) {
            if (VideoPlayer.isFullScreenNow()) {
                if (mFullScreenListener != null)
                    mFullScreenListener.onError(0, 0);
            } else if (mListener != null)
                mListener.onError(0, 0);
            return;
        }
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(context, Uri.parse(url));
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer iMediaPlayer) {
                    if (VideoPlayer.isFullScreenNow()) {
                        if (mFullScreenListener != null)
                            mFullScreenListener.onPrepared();
                    } else if (mListener != null)
                        mListener.onPrepared();
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer iMediaPlayer) {
                    if (VideoPlayer.isFullScreenNow()) {
                        if (mFullScreenListener != null)
                            mFullScreenListener.onCompletion();
                    } else if (mListener != null)
                        mListener.onCompletion();
                }
            });
            mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer iMediaPlayer, int i) {
                    if (VideoPlayer.isFullScreenNow()) {
                        if (mFullScreenListener != null)
                            mFullScreenListener.onBufferingUpdate(i);
                    } else if (mListener != null)
                        mListener.onBufferingUpdate(i);
                }
            });
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer iMediaPlayer, int i, int i1) {
                    if (VideoPlayer.isFullScreenNow()) {
                        if (mFullScreenListener != null)
                            mFullScreenListener.onError(i, i1);
                    } else if (mListener != null)
                        mListener.onError(i, i1);
                    return true;
                }
            });
            mPlayer.prepareAsync();
            setPlayerStatus(PlayStatus.PREPARING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int getVideoWidth() {
        return mPlayer.getVideoWidth();
    }

    int getVideoHeight() {
        return mPlayer.getVideoHeight();
    }

    void play() {
        try {
            if (mPlayer == null) return;
            if (!mPlayer.isPlaying())
                mPlayer.start();
            mStatus = PlayStatus.PLAYING;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void pause() {
        try{
            if (mPlayer == null) return;
            if (mPlayer.isPlaying())
                mPlayer.pause();
            mStatus = PlayStatus.PAUSE;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void stop() {
        try{
            if (mPlayer == null) return;
            if (mPlayer.isPlaying())
                mPlayer.stop();
            mStatus = PlayStatus.NORMAL;
            if (VideoPlayer.isFullScreenNow()) {
                if (mFullScreenListener != null)
                    mFullScreenListener.onCompletion();
            } else if (mListener != null)
                mListener.onCompletion();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void release() {
        try{
            if (mPlayer == null) return;
            stop();
            mPlayer.release();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    void setDisplay(SurfaceHolder sh, boolean isExitFullScreen) {
        if (mPlayer == null) return;
        mPlayer.setDisplay(sh);
        if (mListener != null)
            mListener.onDisplayChanged(isExitFullScreen);
    }

    long getDuration() {
        if (mPlayer == null) return -1;
        return mPlayer.getDuration();
    }

    long getCurrentPosition() {
        if (mPlayer == null) return -1;
        return mPlayer.getCurrentPosition();
    }

    void seekTo(long position) {
        if (mPlayer == null) return;
        mPlayer.seekTo((int) position);
    }

    void setCurrentListener(PlayListener listener) {
        if (listener == null) return;
        if (mListener != listener) {
            setPlayerStatus(PlayStatus.NORMAL);
            if (mListener != null)
                mListener.onResetStatus();
            mListener = listener;
        }
    }

    void setFullScreenListener(PlayListener listener) {
        mFullScreenListener = listener;
    }

    void setPlayerData(VideoPlayer.Status data) {
        mPlayerData = data;
    }

    VideoPlayer.Status getPlayerData() {
        return mPlayerData;
    }


    void setCurrentPlayer(VideoPlayer videoPlayer) {
        if (mVideoPlayer != videoPlayer)
            mVideoPlayer = videoPlayer;
    }

    VideoPlayer getLastPlayer() {
        return mVideoPlayer;
    }

    interface PlayListener {
        void onPrepared();

        void onCompletion();

        void onBufferingUpdate(int percent);

        void onError(int what, int extra);

        void onDisplayChanged(boolean isExitFullScreen);

        void onResetStatus();
    }

    void setPlayerStatus(PlayStatus status) {
        mStatus = status;
    }

    PlayStatus getPlayStatus() {
        return mStatus;
    }

    enum PlayStatus {
        PREPARING, PAUSE, PLAYING, NORMAL, ERROR
    }
}
