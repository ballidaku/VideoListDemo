package ballidaku.videolistdemo.videomanage.manager;


//import com.brucetoo.listvideoplay.videomanage.meta.MetaData;

import ballidaku.videolistdemo.videomanage.meta.MetaData;

public interface PlayerItemChangeListener {
    void onPlayerItemChanged(MetaData currentItemMetaData);
}
