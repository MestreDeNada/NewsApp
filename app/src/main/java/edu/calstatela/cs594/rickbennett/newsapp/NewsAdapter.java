package edu.calstatela.cs594.rickbennett.newsapp;

import android.content.Context;
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

    private NewsItemClickListener mClickListener;

    public interface NewsItemClickListener {
        void onClick(int itemIndex);
    }

    public NewsAdapter (NewsItemClickListener clickListener) {
        mClickListener = clickListener;
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

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickListener.onClick(adapterPosition);
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
        NewsItem newsItem = mNewsData.get(position);
        newsAdapterViewHolder.mTitleTextView.setText(newsItem.getTitle());
        newsAdapterViewHolder.mDescriptionTextView.setText(newsItem.getDescription());
        newsAdapterViewHolder.mTimePublishedTextView.setText(newsItem.getTimePublished());
    }

    @Override
    public int getItemCount() {
        if(mNewsData == null) return 0;
        return mNewsData.size();
    }

    public void setNewsData(ArrayList<NewsItem> newsData) {
        mNewsData = newsData;
        notifyDataSetChanged();
    }

    public NewsItem getNewsItem(int position) {
        return mNewsData.get(position);
    }
}
