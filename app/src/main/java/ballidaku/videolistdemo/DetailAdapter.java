package ballidaku.videolistdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.util.List;


/**
 * Created by brst-pc93 on 7/20/17.
 */

public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context;

    View.OnClickListener listener;

    private static final int TYPE_VIDEO = 0;
    private static final int TYPE_ITEM = 1;


    private List<DetailsModel> detailAdapterList;


    public DetailAdapter(Context context,List<DetailsModel> detailAdapterList,View.OnClickListener listener)
    {
        this.context=context;
        this.detailAdapterList = detailAdapterList;
        this.listener = listener;
    }






    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        ImageView imageView;

        public MyViewHolder(View view)
        {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textView = (TextView) view.findViewById(R.id.textView);
        }
    }


    //View holder of Videos
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView cover;
        public View playArea;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_video_name);
            cover = (ImageView) itemView.findViewById(R.id.img_cover);
            playArea = itemView.findViewById(R.id.layout_play_area);
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView;
        if (viewType == TYPE_ITEM)
        {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_detail_item, parent, false);

            return new DetailAdapter.MyViewHolder(itemView);
        }
        else
        {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        DetailsModel detailsModel = detailAdapterList.get(position);

        if (holder instanceof MyViewHolder)
        {
            if (detailsModel.getType() == "Text")
            {
                ((MyViewHolder) holder).textView.setText(detailsModel.getText());
                ((MyViewHolder) holder).imageView.setVisibility(View.GONE);
            }
            else if (detailsModel.getType() == "Image")
            {
                ((MyViewHolder) holder).textView.setVisibility(View.GONE);

                //File imgFile = new File(detailsModel.getImagePath());

                //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                //holder.imageView.setImageURI(detailsModel.getImagePath());
                Bitmap bitmap = null;
                try
                {
                    bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(detailsModel.getImagePath()));
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }

                ((MyViewHolder) holder).imageView.setImageBitmap(bitmap);

                // Picasso.with(context).load(bitmap) /*.resize(96, 96).centerCrop()*/.into(holder.imageView);
            }
        }
        else
        {
            VideoModel model = ListDataGenerater.datas.get(position);
            ((ItemViewHolder)holder).name.setText("Just Video " + position);
            Picasso.with(context).load(model.coverImage)
                   .placeholder(R.drawable.shape_place_holder)
                   .into(((ItemViewHolder)holder).cover);

            Log.e("coverImage","I "+model.coverImage);
            model.position = position;
            ((ItemViewHolder)holder).playArea.setTag(model);
            ((ItemViewHolder)holder).playArea.setOnClickListener(listener);
        }
        /*else if (detailsModel.getType() == "Video")
        {

            *//*File imgFile = new File(detailsModel.getVideoPath());

            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imgFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);

            holder.imageView.setImageBitmap(bMap);*//*

            Picasso.with(context).load(Uri.fromFile( new File( "/storage/emulated/0/Pictures/MyCameraVideo/VID_20170502_025536.mp4" ) )) *//*.resize(96, 96).centerCrop()*//*.into(holder.imageView);

        }*/

    }


    @Override
    public int getItemCount()
    {
        return detailAdapterList.size();
    }


    @Override
    public int getItemViewType(int position)
    {
        if (isPositionHeader(position))
        {
            return TYPE_VIDEO;
        }
        else
        {
            return TYPE_ITEM;
        }

        //return super.getItemViewType(position);
    }

    private boolean isPositionHeader(int position)
    {
        return detailAdapterList.get(position).getType().equals("Video");
    }



    public void addText(String info)
    {
        DetailsModel detailsModel = new DetailsModel();
        detailsModel.setType("Text");
        detailsModel.setText(info);

        detailAdapterList.add(detailsModel);

        notifyDataSetChanged();
    }

    public void addImage(Uri path)
    {
        DetailsModel detailsModel = new DetailsModel();
        detailsModel.setType("Video");
        detailsModel.setImagePath(path);

        detailAdapterList.add(detailsModel);

        notifyDataSetChanged();
    }

    public void addVideo(Uri path)
    {
        DetailsModel detailsModel = new DetailsModel();
        detailsModel.setType("Video");
        detailsModel.setVideoPath(path);

        detailAdapterList.add(detailsModel);

        notifyDataSetChanged();
    }


}