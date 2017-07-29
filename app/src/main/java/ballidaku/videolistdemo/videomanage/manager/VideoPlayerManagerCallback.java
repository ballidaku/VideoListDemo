package ballidaku.videolistdemo.videomanage.manager;


//import com.brucetoo.listvideoplay.videomanage.PlayerMessageState;
//import com.brucetoo.listvideoplay.videomanage.meta.MetaData;
//import com.brucetoo.listvideoplay.videomanage.ui.VideoPlayerView;

import ballidaku.videolistdemo.videomanage.PlayerMessageState;
import ballidaku.videolistdemo.videomanage.meta.MetaData;
import ballidaku.videolistdemo.videomanage.ui.VideoPlayerView;

/**
 * This callback is used by {@link com.brucetoo.listvideoplay.videomanage.playermessages.PlayerMessage}
 * to get and set data it needs
 */
public interface VideoPlayerManagerCallback {

    void setCurrentItem(MetaData currentItemMetaData, VideoPlayerView newPlayerView);

    void setVideoPlayerState(VideoPlayerView videoPlayerView, PlayerMessageState playerMessageState);

    PlayerMessageState getCurrentPlayerState();
}
