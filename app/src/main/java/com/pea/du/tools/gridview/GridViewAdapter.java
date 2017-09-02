package com.pea.du.tools.gridview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pea.du.R;
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
            Picasso
                    .with(context)
                    .load(data.get(position).getUrl())
                    .resize(250, 200)
                    .into((ImageView) convertView);
        else

        if (!(data.get(position).getPath()==null))
            Picasso
                    .with(context)
                    .load(new File(data.get(position).getPath()))
                    .resize(250, 200)
                    .into((ImageView) convertView);
        else
            Picasso
                    .with(context)
                    .load(data.get(position).getId())
                    .into((ImageView) convertView);



        return convertView;
    }

    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.grid_item_title);
            holder.image = (ImageView) row.findViewById(R.id.grid_item_image);
            //row.setTag(holder);
            Picasso.with(context).load("http://78.47.195.208/MSK3/DeffectAct/17/50.jpg").into(row);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem item = (ImageItem) data.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageBitmap(item.getImage());
        return row;
    }
    */

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}