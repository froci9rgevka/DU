package com.pea.du.tools.gridview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.pea.du.R;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        if (!(data.get(position).getUrl()==null))
            PicassoSingleton.getSharedInstance(context)
                    .load(data.get(position).getUrl())
                    .placeholder( R.drawable.progress_animation )
                    .resize(250, 250)
                    .into((ImageView) convertView);
                /*
                Picasso
                        .with(context)
                        .load(data.get(position).getUrl())
                        .placeholder( R.drawable.progress_animation )
                        .resize(250, 250)
                        .into((ImageView) convertView);
                */
        else

        if (!(data.get(position).getPath()==null))
            Picasso
                    .with(context)
                    .load(new File(data.get(position).getPath()))
                    .resize(250, 250)
                    .into((ImageView) convertView);
        else
            Picasso
                    .with(context)
                    .load(data.get(position).getId())
                    .into((ImageView) convertView);

        return convertView;


    }


}