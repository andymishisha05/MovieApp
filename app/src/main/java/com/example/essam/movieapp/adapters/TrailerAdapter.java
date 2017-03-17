package com.example.essam.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.essam.movieapp.R;
import com.example.essam.movieapp.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by essam on 12/2/2016.
 */

public class TrailerAdapter extends BaseAdapter {

    private final Context mCont;
    private final LayoutInflater mLayout;
    private final Trailer mModel = new Trailer();

    private List<Trailer> mArray;

    public TrailerAdapter(Context context, List<Trailer> array) {
        mCont = context;
        mLayout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mArray = array;
    }

    public Context getContext() {
        return mCont;
    }

    public void add(Trailer object) {
        synchronized (mModel) {
            mArray.add(object);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mModel) {
            mArray.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mArray.size();
    }

    @Override
    public Trailer getItem(int i) {

        return mArray.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = mLayout.inflate(R.layout.item_trailer, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        final Trailer trailer = getItem(position);
        viewHolder = (ViewHolder) view.getTag();


        Picasso.with(getContext())
                .load("http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg")
                .into(viewHolder.imageView);


        viewHolder.nameView.setText(trailer.getName());

        return view;
    }

    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView nameView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.trailer_image);
            nameView = (TextView) view.findViewById(R.id.trailer_name);
        }
    }

}
