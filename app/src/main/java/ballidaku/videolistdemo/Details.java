package ballidaku.videolistdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ballidaku.videolistdemo.videomanage.controller.VideoControllerView;
import ballidaku.videolistdemo.videomanage.controller.ViewAnimator;
import ballidaku.videolistdemo.videomanage.manager.SingleVideoPlayerManager;
import ballidaku.videolistdemo.videomanage.manager.VideoPlayerManager;
import ballidaku.videolistdemo.videomanage.meta.CurrentItemMetaData;
import ballidaku.videolistdemo.videomanage.meta.MetaData;
import ballidaku.videolistdemo.videomanage.ui.MediaPlayerWrapper;
import ballidaku.videolistdemo.videomanage.ui.VideoPlayerView;


/**
 * Created by brst-pc93 on 7/20/17.
 */

public class Details extends AppCompatActivity
{

    String TAG = Details.class.getSimpleName();

    Context context;


    Toolbar toolbar;

    TextView textViewTitle;

    RecyclerView recyclerViewDetails;

    DetailAdapter detailAdapter;

    List<DetailsModel> detailsModelArrayList = new ArrayList<>();




    private FrameLayout mVideoFloatContainer;
    private View mVideoPlayerBg;
    private ImageView mVideoCoverMask;
    private VideoPlayerView mVideoPlayerView;
    private View mVideoLoadingView;
    private ProgressBar mVideoProgressBar;

    private View mCurrentPlayArea;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private VideoControllerView mCurrentVideoControllerView;

    private int mCurrentBuffer;
    private boolean mIsClickToStop;

    private VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(null);

    private int mCurrentActiveVideoItem = -1;

    private float mOriginalHeight;

    private float mMoveDeltaY;

    private boolean mCanTriggerStop = true;

    LinearLayoutManager mLayoutManager;


    public Details()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_details);

        context = this;

        addFake();

        setUpViews();
    }


    public void addFake()
    {

        for (int i = 0; i <10 ; i++)
        {
            DetailsModel detailsModel = new DetailsModel();
            detailsModel.setType("Video");


            detailsModelArrayList.add(detailsModel);
        }

    }


    private void setUpViews()
    {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);


        textViewTitle = (TextView) findViewById(R.id.textViewTitle);

        recyclerViewDetails = (RecyclerView) findViewById(R.id.recyclerViewDetails);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewDetails.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewDetails.addItemDecoration(dividerItemDecoration);


        detailAdapter = new DetailAdapter(context,detailsModelArrayList,onClickListener12);

        mLayoutManager = new LinearLayoutManager(context);
        recyclerViewDetails.setLayoutManager(mLayoutManager);
        recyclerViewDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDetails.addOnScrollListener(mOnScrollListener);
        recyclerViewDetails.setAdapter(detailAdapter);





        mVideoFloatContainer = (FrameLayout) findViewById(R.id.layout_float_video_container);
        mVideoPlayerBg = findViewById(R.id.video_player_bg);
        mVideoCoverMask = (ImageView)findViewById(R.id.video_player_mask);
        mVideoPlayerView = (VideoPlayerView) findViewById(R.id.video_player_view);
        mVideoLoadingView = findViewById(R.id.video_progress_loading);
        mVideoProgressBar = (ProgressBar) findViewById(R.id.video_progress_bar);



        mVideoPlayerView.addMediaPlayerListener(new MediaPlayerWrapper.MainThreadMediaPlayerListener() {
            @Override
            public void onVideoSizeChangedMainThread(int width, int height) {

            }

            @Override
            public void onVideoPreparedMainThread() {

                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onVideoPreparedMainThread");
                mVideoFloatContainer.setVisibility(View.VISIBLE);
                mVideoPlayerView.setVisibility(View.VISIBLE);
                mVideoLoadingView.setVisibility(View.VISIBLE);
                //for cover the pre Video frame
                mVideoCoverMask.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVideoCompletionMainThread() {

                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onVideoCompletionMainThread");

                if (mCurrentPlayArea != null) {
                    mCurrentPlayArea.setClickable(true);
                }

                mVideoFloatContainer.setVisibility(View.INVISIBLE);
                mCurrentPlayArea.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                ViewAnimator.putOn(mVideoFloatContainer).translationY(0);

                //stop update progress
                mVideoProgressBar.setVisibility(View.GONE);
                mHandler.removeCallbacks(mProgressRunnable);
            }

            @Override
            public void onErrorMainThread(int what, int extra) {
                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onErrorMainThread");
                if (mCurrentPlayArea != null) {
                    mCurrentPlayArea.setClickable(true);
                    mCurrentPlayArea.setVisibility(View.VISIBLE);
                }
                mVideoFloatContainer.setVisibility(View.INVISIBLE);

                //stop update progress
                mVideoProgressBar.setVisibility(View.GONE);
                mHandler.removeCallbacks(mProgressRunnable);
            }

            @Override
            public void onBufferingUpdateMainThread(int percent) {
                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onBufferingUpdateMainThread");
                mCurrentBuffer = percent;
            }

            @Override
            public void onVideoStoppedMainThread() {
                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onVideoStoppedMainThread");
                if (!mIsClickToStop) {
                    mCurrentPlayArea.setClickable(true);
                    mCurrentPlayArea.setVisibility(View.VISIBLE);
                }

                //stop update progress
                mVideoProgressBar.setVisibility(View.GONE);
                mHandler.removeCallbacks(mProgressRunnable);
            }

            @Override
            public void onInfoMainThread(int what) {
                Log.e(MediaPlayerWrapper.VIDEO_TAG, "check play onInfoMainThread what:" + what);
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {

                    //start update progress
                    mVideoProgressBar.setVisibility(View.VISIBLE);
                    mHandler.post(mProgressRunnable);

                    mVideoPlayerView.setVisibility(View.VISIBLE);
                    mVideoLoadingView.setVisibility(View.GONE);
                    mVideoCoverMask.setVisibility(View.GONE);
                    mVideoPlayerBg.setVisibility(View.VISIBLE);
                    createVideoControllerView();

                    mCurrentVideoControllerView.showWithTitle("VIDEO TEST - " + mCurrentActiveVideoItem);
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    mVideoLoadingView.setVisibility(View.VISIBLE);
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    mVideoLoadingView.setVisibility(View.GONE);
                }
            }
        });

    }

    View.OnClickListener onClickListener12=new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            mIsClickToStop = true;
            v.setClickable(false);
            if (mCurrentPlayArea != null) {
                if (mCurrentPlayArea != v) {
                    mCurrentPlayArea.setClickable(true);
                    mCurrentPlayArea.setVisibility(View.VISIBLE);
                    mCurrentPlayArea = v;
                } else {//click same area
                    if (mVideoFloatContainer.getVisibility() == View.VISIBLE) return;
                }
            } else {
                mCurrentPlayArea = v;
            }

            //invisible self ,and make visible when video play completely
            v.setVisibility(View.INVISIBLE);
            if (mCurrentVideoControllerView != null)
                mCurrentVideoControllerView.hide();

            mVideoFloatContainer.setVisibility(View.VISIBLE);
            mVideoCoverMask.setVisibility(View.GONE);
            mVideoPlayerBg.setVisibility(View.GONE);

            VideoModel model = (VideoModel) v.getTag();
            mCurrentActiveVideoItem = model.position;
            mCanTriggerStop = true;

            //move container view
            startMoveFloatContainer(true);

            mVideoLoadingView.setVisibility(View.VISIBLE);
            mVideoPlayerView.setVisibility(View.INVISIBLE);

            //play video
            mVideoPlayerManager.playNewVideo(new CurrentItemMetaData(model.position, v), mVideoPlayerView, model.videoUrl);

        }
    };



    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        int totalDy;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e(TAG, "onScrollStateChanged state:" + newState);
            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                mOriginalHeight = mVideoFloatContainer.getTranslationY();
                totalDy = 0;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            /**
             * NOTE: RecyclerView will callback this method when {@link RecyclerViewFragment#onConfigurationChanged(Configuration)}
             * happened,so handle this special scene.
             */
            if(mPlayerControlListener.isFullScreen()) return;

            //Calculate the total scroll distance of RecyclerView
            totalDy += dy;
            mMoveDeltaY = -totalDy;
            Log.e(TAG, "onScrolled scrollY:" + -totalDy);
            startMoveFloatContainer(false);

            if (mCurrentActiveVideoItem < mLayoutManager.findFirstVisibleItemPosition()
                      || mCurrentActiveVideoItem > mLayoutManager.findLastVisibleItemPosition()) {
                if (mCanTriggerStop) {
                    mCanTriggerStop = false;
                    stopPlaybackImmediately();
                }
            }
        }
    };

    private boolean checkMediaPlayerInvalid() {
        return mVideoPlayerView != null && mVideoPlayerView.getMediaPlayer() != null;
    }


    private void startMoveFloatContainer(boolean click) {

        if (mVideoFloatContainer.getVisibility() != View.VISIBLE) return;
        final float moveDelta;

        if (click) {
            Log.e(TAG, "startMoveFloatContainer > mFloatVideoContainer getTranslationY:" + mVideoFloatContainer.getTranslationY());
            ViewAnimator.putOn(mVideoFloatContainer).translationY(0);

            int[] playAreaPos = new int[2];
            int[] floatContainerPos = new int[2];
            mCurrentPlayArea.getLocationOnScreen(playAreaPos);
            mVideoFloatContainer.getLocationOnScreen(floatContainerPos);
            mOriginalHeight = moveDelta = playAreaPos[1] - floatContainerPos[1];

            Log.e(TAG, "startMoveFloatContainer > mFloatVideoContainer playAreaPos[1]:" + playAreaPos[1] + " floatContainerPos[1]:" + floatContainerPos[1]);
        } else {
            moveDelta = mMoveDeltaY;
            Log.e(TAG, "ListView moveDelta :" + moveDelta + "");
        }

        float translationY = moveDelta + (!click ? mOriginalHeight : 0);

        Log.e(TAG, "startMoveFloatContainer > moveDelta:" + moveDelta + " before getTranslationY:" + mVideoFloatContainer.getTranslationY()
                  + " mOriginalHeight:" + mOriginalHeight + " translationY:" + translationY);

        ViewAnimator.putOn(mVideoFloatContainer).translationY(translationY);

        Log.i(TAG, "startMoveFloatContainer < after getTranslationY:" + mVideoFloatContainer.getTranslationY());
    }


    private void createVideoControllerView() {

        if (mCurrentVideoControllerView != null) {
            mCurrentVideoControllerView.hide();
            mCurrentVideoControllerView = null;
        }

        mCurrentVideoControllerView = new VideoControllerView.Builder(this, mPlayerControlListener)
                  .withVideoTitle("TEST VIDEO")
                  .withVideoView(mVideoPlayerView)//to enable toggle display controller view
                  .canControlBrightness(true)
                  .canControlVolume(true)
                  .canSeekVideo(false)
                  .exitIcon(R.drawable.video_top_back)
                  .pauseIcon(R.drawable.ic_media_pause)
                  .playIcon(R.drawable.ic_media_play)
                  .shrinkIcon(R.drawable.ic_media_fullscreen_shrink)
                  .stretchIcon(R.drawable.ic_media_fullscreen_stretch)
                  .build(mVideoFloatContainer);//layout container that hold video play view
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    EditText editTextInfo;

   View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            String info = editTextInfo.getText().toString().trim();

            MyDialogs.getInstance().dialog.dismiss();

            detailAdapter.addText(info);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.addText:

                editTextInfo = MyDialogs.getInstance().addDetail(context,onClickListener);

                return true;

            case R.id.addImage:

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);


                return true;

            case R.id.addVideo:

                Intent intent2 = new Intent();
                intent2.setType("video/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent2, "Select Video"), 2);


                return true;


            case R.id.edit:

                return true;


            case R.id.delete:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (resultCode == RESULT_OK)
        {

            if (requestCode == 1)
            {

                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                Uri currImageURI = data.getData();

                Log.e("Uri", currImageURI + "");
                Log.e("Path", "" + getRealPathFromURI_API19(context, currImageURI,"Image"));

                //detailAdapter.addImage(getRealPathFromURI_API19(context, currImageURI,"Image"));
                detailAdapter.addImage(currImageURI);
            }
            else if (requestCode == 2)
            {
                Uri currImageURI = data.getData();

                Log.e("VideoUri", currImageURI + "");
              //  Log.e("VideoPath", "" + getRealPathFromURI_API19(context, currImageURI,"Video"));

                //detailAdapter.addVideo(getRealPathFromURI_API19(context, currImageURI,"Video"));
                detailAdapter.addVideo(currImageURI);

            }
        }
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri, String inageVideo)
    {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Uri u;
        if (inageVideo.equals("Image"))
        {
            u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        else
        {
            u = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor cursor = context.getContentResolver().query(u,column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst())
        {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }



    private VideoControllerView.MediaPlayerControlListener mPlayerControlListener = new VideoControllerView.MediaPlayerControlListener() {
        @Override
        public void start() {
            if (checkMediaPlayerInvalid())
                mVideoPlayerView.getMediaPlayer().start();
        }

        @Override
        public void pause() {
            mVideoPlayerView.getMediaPlayer().pause();
        }

        @Override
        public int getDuration() {
            if (checkMediaPlayerInvalid()) {
                return mVideoPlayerView.getMediaPlayer().getDuration();
            }
            return 0;
        }

        @Override
        public int getCurrentPosition() {
            if (checkMediaPlayerInvalid()) {
                return mVideoPlayerView.getMediaPlayer().getCurrentPosition();
            }
            return 0;
        }

        @Override
        public void seekTo(int position) {
            if (checkMediaPlayerInvalid()) {
                mVideoPlayerView.getMediaPlayer().seekToPosition(position);
            }
        }

        @Override
        public boolean isPlaying() {
            if (checkMediaPlayerInvalid()) {
                return mVideoPlayerView.getMediaPlayer().isPlaying();
            }
            return false;
        }

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public int getBufferPercentage() {
            return mCurrentBuffer;
        }

        @Override
        public boolean isFullScreen() {
            return getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                      || getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        }

        @Override
        public void toggleFullScreen() {
            if (isFullScreen()) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(Build.VERSION.SDK_INT < 9 ?
                          ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                          ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        }

        @Override
        public void exit() {
            //TODO to handle exit status
            if (isFullScreen()) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    };

    public void stopPlaybackImmediately() {

        mIsClickToStop = false;

        if (mCurrentPlayArea != null) {
            mCurrentPlayArea.setClickable(true);
        }

        if (mVideoPlayerManager != null) {
            Log.e(TAG, "check play stopPlaybackImmediately");
            mVideoFloatContainer.setVisibility(View.INVISIBLE);
            mVideoPlayerManager.stopAnyPlayback();
        }
    }


    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if(mPlayerControlListener != null){

                if(mCurrentVideoControllerView.isShowing()){
                    mVideoProgressBar.setVisibility(View.GONE);
                }else {
                    mVideoProgressBar.setVisibility(View.VISIBLE);
                }

                int position = mPlayerControlListener.getCurrentPosition();
                int duration = mPlayerControlListener.getDuration();
                if(duration != 0) {
                    long pos = 1000L * position / duration;
                    int percent = mPlayerControlListener.getBufferPercentage() * 10;
                    mVideoProgressBar.setProgress((int) pos);
                    mVideoProgressBar.setSecondaryProgress(percent);
                    mHandler.postDelayed(this,1000);
                }
            }
        }
    };


}