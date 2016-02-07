package com.example.shabnam.instaphotos;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shabnam on 2/4/16.
 */
public class IntagramPhotosAdapter extends ArrayAdapter<InstagramPhoto>{
    public IntagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1 , objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo =getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext() ).inflate(R.layout.item_photo, parent,false);
        }
        TextView tvCaption=(TextView) convertView.findViewById(R.id.tvCaption );
        ImageView ivPhoto=(ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView ivProfilePhoto=(ImageView) convertView.findViewById(R.id.ivProfilePic);
        TextView tvUsername=(TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvLikes=(TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvCreation=(TextView) convertView.findViewById(R.id.tvCreation);
        tvCaption.setText(photo.caption);
        tvUsername.setText(photo.username);
        tvLikes.setText(String.valueOf(photo.likesCount) + " Likes");
        long createdTime=photo.longCreation;
        //Date currentDate = new Date();
        long currentTime = System.currentTimeMillis()/1000;

        CharSequence diffTime=DateUtils.getRelativeTimeSpanString(createdTime,currentTime , 0);

        tvCreation.setText(diffTime);
        ivPhoto.setImageResource(0);

        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.placeholder).fit().centerInside().into(ivPhoto);
        ivProfilePhoto.setImageResource(0);

        Picasso.with(getContext()).load(photo.userImageUrl).transform(new CircleTransform()).into(ivProfilePhoto);

        return convertView;
    }
}
