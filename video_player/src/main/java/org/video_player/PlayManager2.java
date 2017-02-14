package org.video_player;

/**
 * Created by wuyr on 12/3/16 5:53 PM.
 */

public class PlayManager2 {

   /* private volatile static PlayManager2 mInstance;
    private SimpleExoPlayer mPlayer;
    private PlayListener mListener, mFullScreenListener;
    private PlayStatus mStatus = PlayStatus.NORMAL;
    private VideoPlayer2.Status mPlayerData;
    private VideoPlayer2 mVideoPlayer2;

    private PlayManager2(Context context) {
        mPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(
                new AdaptiveVideoTrackSelection.Factory(new DefaultBandwidthMeter())),
                new DefaultLoadControl());
    }

    public static PlayManager2 getInstance(Context context) {
        LogUtil.print("get play manager instance");
        if (mInstance == null)
            synchronized (PlayManager2.class) {
                if (mInstance == null)
                    mInstance = new PlayManager2(context);
            }
        return mInstance;
    }

    void prepareAsync(final Context context, final String url) {
        LogUtil.print("prepareAsync");
        if (TextUtils.isEmpty(url)) {
            if (VideoPlayer2.isFullScreenNow()) {
                if (mFullScreenListener != null)
                    mFullScreenListener.onError(0, 0);
            } else if (mListener != null)
                mListener.onError(0, 0);
            return;
        }
        mPlayer.release();
        mPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(
                        new AdaptiveVideoTrackSelection.Factory(new DefaultBandwidthMeter())),
                new DefaultLoadControl());
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
// Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(
                Util.getUserAgent(context, "lvu"), bandwidthMeter);
// Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
// This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory, extractorsFactory, null, null);
// Prepare the player with the source.
        mPlayer.prepare(videoSource);
        *try {
            *mPlayer.setDataSource(context, Uri.parse(url));
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer iMediaPlayer) {
                    if (VideoPlayer2.isFullScreenNow()) {
                        if (mFullScreenListener != null)
                            mFullScreenListener.onPrepared();
                    } else if (mListener != null)
                        mListener.onPrepared();
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer iMediaPlayer) {
                    if (VideoPlayer2.isFullScreenNow()) {
                        if (mFullScreenListener != null)
                            mFullScreenListener.onCompletion();
                    } else if (mListener != null)
                        mListener.onCompletion();
                }
            });
            mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer iMediaPlayer, int i) {
                    if (VideoPlayer2.isFullScreenNow()) {
                        if (mFullScreenListener != null)
                            mFullScreenListener.onBufferingUpdate(i);
                    } else if (mListener != null)
                        mListener.onBufferingUpdate(i);
                }
            });
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer iMediaPlayer, int i, int i1) {
                    if (VideoPlayer2.isFullScreenNow()) {
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
        if (mPlayer == null) return;
        if (!mPlayer.isPlaying())
            mPlayer.start();
        mStatus = PlayStatus.PLAYING;
    }

    void pause() {
        if (mPlayer == null) return;
        if (mPlayer.isPlaying())
            mPlayer.pause();
        mStatus = PlayStatus.PAUSE;
    }

    void stop() {
        if (mPlayer == null) return;
        if (mPlayer.isPlaying())
            mPlayer.stop();
        mStatus = PlayStatus.NORMAL;
        if (VideoPlayer2.isFullScreenNow()) {
            if (mFullScreenListener != null)
                mFullScreenListener.onCompletion();
        } else if (mListener != null)
            mListener.onCompletion();
    }

    public void release() {
        if (mPlayer == null) return;
        stop();
        mPlayer.release();
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

    void setPlayerData(VideoPlayer2.Status data) {
        mPlayerData = data;
    }

    VideoPlayer2.Status getPlayerData() {
        return mPlayerData;
    }


    void setCurrentPlayer(VideoPlayer2 videoPlayer) {
        if (mVideoPlayer2 != videoPlayer)
            mVideoPlayer2 = videoPlayer;
    }

    VideoPlayer2 getLastPlayer() {
        return mVideoPlayer2;
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
    }*/
}
