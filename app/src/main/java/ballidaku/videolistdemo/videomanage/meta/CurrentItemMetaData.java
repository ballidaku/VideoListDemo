package ballidaku.videolistdemo.videomanage.meta;

import android.view.View;

import ballidaku.videolistdemo.videomanage.ui.VideoPlayerView;

//import com.brucetoo.listvideoplay.videomanage.ui.VideoPlayerView;


/**
 * Record current active item info about position in ListView
 */
public class CurrentItemMetaData implements MetaData {

    public int positionOfCurrentItem;
    public View videoPlayArea;
    public VideoPlayerView videoPlayerView;

    public CurrentItemMetaData(int positionOfCurrentItem, View videoPlayArea) {
        this.positionOfCurrentItem = positionOfCurrentItem;
        this.videoPlayArea = videoPlayArea;
    }

    @Override
    public String toString() {
        //TODO
        return "CurrentItemMetaData{" +
                "positionOfCurrentItem=" + positionOfCurrentItem +
                '}';
    }

    @Override
    public boolean isMorePlayView() {
        return true;
    }
}
