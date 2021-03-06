package ballidaku.videolistdemo.videomanage.playermessages;

import android.media.MediaPlayer;

import ballidaku.videolistdemo.videomanage.PlayerMessageState;
import ballidaku.videolistdemo.videomanage.manager.VideoPlayerManagerCallback;
import ballidaku.videolistdemo.videomanage.ui.VideoPlayerView;

//import com.brucetoo.listvideoplay.videomanage.PlayerMessageState;
//import com.brucetoo.listvideoplay.videomanage.manager.VideoPlayerManagerCallback;
//import com.brucetoo.listvideoplay.videomanage.ui.VideoPlayerView;


/**
 * This PlayerMessage calls {@link MediaPlayer#prepare()} on the instance that is used inside {@link VideoPlayerView}
 */
public class Prepare extends PlayerMessage{

    public Prepare(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
    }

    @Override
    protected void performAction(VideoPlayerView currentPlayer) {
        currentPlayer.prepare();
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.PREPARING;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.PREPARED;
    }
}
