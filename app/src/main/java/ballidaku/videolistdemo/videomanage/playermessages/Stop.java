package ballidaku.videolistdemo.videomanage.playermessages;

import android.media.MediaPlayer;

import ballidaku.videolistdemo.videomanage.PlayerMessageState;
import ballidaku.videolistdemo.videomanage.manager.VideoPlayerManagerCallback;
import ballidaku.videolistdemo.videomanage.ui.VideoPlayerView;

//import com.brucetoo.listvideoplay.videomanage.PlayerMessageState;
//import com.brucetoo.listvideoplay.videomanage.manager.VideoPlayerManagerCallback;
//import com.brucetoo.listvideoplay.videomanage.ui.VideoPlayerView;


/**
 * This PlayerMessage calls {@link MediaPlayer#stop()} on the instance that is used inside {@link VideoPlayerView}
 */
public class Stop extends PlayerMessage {
    public Stop(VideoPlayerView videoView, VideoPlayerManagerCallback callback) {
        super(videoView, callback);
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        currentPlayer.stop();
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.STOPPING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.STOPPED;
    }
}
