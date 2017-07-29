package ballidaku.videolistdemo.videomanage.playermessages;


//import com.brucetoo.listvideoplay.videomanage.PlayerMessageState;
//import com.brucetoo.listvideoplay.videomanage.manager.VideoPlayerManagerCallback;
//import com.brucetoo.listvideoplay.videomanage.ui.VideoPlayerView;

import ballidaku.videolistdemo.videomanage.PlayerMessageState;
import ballidaku.videolistdemo.videomanage.manager.VideoPlayerManagerCallback;
import ballidaku.videolistdemo.videomanage.ui.VideoPlayerView;

/**
 * This is generic PlayerMessage for setDataSource
 */
public abstract class SetDataSourceMessage extends PlayerMessage{

    public SetDataSourceMessage(VideoPlayerView videoPlayerView, VideoPlayerManagerCallback callback) {
        super(videoPlayerView, callback);
    }

    @Override
    protected PlayerMessageState stateBefore() {
        return PlayerMessageState.SETTING_DATA_SOURCE;
    }

    @Override
    protected PlayerMessageState stateAfter() {
        return PlayerMessageState.DATA_SOURCE_SET;
    }
}
