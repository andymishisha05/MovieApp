package com.example.essam.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.essam.movieapp.R;
import com.example.essam.movieapp.Review;

import java.util.List;

/**
 * Created by essam on 12/2/2016.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {
    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Review review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_review, parent, false);
        }

        TextView authorText = (TextView)convertView.findViewById(R.id.review_author);
        authorText.setText(review.getAuthor());

        TextView reviewText = (TextView)convertView.findViewById(R.id.review_content);
        reviewText.setText(review.getContent());


        return convertView;
    }
}


   /* private final Context mCont;
    private final LayoutInflater mLayout;
    private final Review mModel = new Review();

    private List<Review> mArray;

    public ReviewAdapter(Context context, List<Review> paths) {
        mCont = context;

         mLayout= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mArray = paths;
    }

    public Context getContext() {

        return mCont;

    }

    public void add(Review object) {
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
    public Review getItem(int position) {
        return mArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = mLayout.inflate(R.layout.item_review, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Review review = getItem(position);

        viewHolder = (ViewHolder) view.getTag();

        viewHolder.authorView.setText(review.getAuthor());

        viewHolder.contentView.setText(Html.fromHtml(review.getContent()));

        return view;
    }

    public static class ViewHolder {
        public final TextView authorView;
        public final TextView contentView;

        public ViewHolder(View view) {
            authorView = (TextView) view.findViewById(R.id.review_author);
            contentView = (TextView) view.findViewById(R.id.review_content);
        }
    }

}*/
