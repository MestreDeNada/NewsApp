package edu.calstatela.cs594.rickbennett.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rick on 7/5/17.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    private ArrayList<NewsItem> mNewsData;

    //Homework 4 database: Adding cursor for database.
    private Cursor mCursor;

    private NewsItemClickListener mClickListener;

    //Homework 4 database: Add cursor to the click listener interface.
    public interface NewsItemClickListener {
        void onClick(Cursor cursor, int itemIndex);
    }

    //Homework 4 database: Add cursor to the adapter constructor.
    public NewsAdapter (Cursor cursor, NewsItemClickListener clickListener) {
        this.mCursor = cursor;
        this.mClickListener = clickListener;
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTitleTextView;
        public final TextView mDescriptionTextView;
        public final TextView mTimePublishedTextView;

        public NewsAdapterViewHolder(View view) {
            super(view);
            mTitleTextView = (TextView) view.findViewById(R.id.tv_news_title);
            mDescriptionTextView = (TextView) view.findViewById(R.id.tv_news_description);
            mTimePublishedTextView = (TextView) view.findViewById(R.id.tv_news_time_published);
            view.setOnClickListener(this);

        }

        //Homework 4 database: Add mCursor to the listener.
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickListener.onClick(mCursor, adapterPosition);
        }
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.news_list_item, viewGroup, shouldAttachToParentImmediately);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder newsAdapterViewHolder, int position) {
        //Homework 4 database; use cursor to get info from database table.
        //check if there is data at the cursor position and if not, return.
        if(!mCursor.moveToPosition(position))
            return;
        String title = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Articles.COLUMN_TITLE));
        String description = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Articles.COLUMN_DESCRIPTION));
        String published = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Articles.COLUMN_PUBLISHED_AT));
        newsAdapterViewHolder.mTitleTextView.setText(title);
        newsAdapterViewHolder.mDescriptionTextView.setText(description);
        newsAdapterViewHolder.mTimePublishedTextView.setText(published);

    }

    //Homework 4 database: Use cursor.getCount instead of array size.
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /* Homework 4 database: These methods not needed anymore.
    public void setNewsData(ArrayList<NewsItem> newsData) {
        mNewsData = newsData;
        notifyDataSetChanged();
    }

    public NewsItem getNewsItem(int position) {
        return mNewsData.get(position);
    }
    */
}
