package ballidaku.videolistdemo;

import android.net.Uri;

/**
 * Created by brst-pc93 on 7/20/17.
 */

public class DetailsModel
{

    String text;
    Uri imagePath;
    Uri videoPath;
    String type;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Uri getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(Uri imagePath)
    {
        this.imagePath = imagePath;
    }

    public Uri getVideoPath()
    {
        return videoPath;
    }

    public void setVideoPath(Uri videoPath)
    {
        this.videoPath = videoPath;
    }
}
