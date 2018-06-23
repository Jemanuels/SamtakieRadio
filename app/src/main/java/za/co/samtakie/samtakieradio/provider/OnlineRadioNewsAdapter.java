/*Copyright [2018] [Jurgen Emanuels]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package za.co.samtakie.samtakieradio.provider;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.ui.News;

public class OnlineRadioNewsAdapter extends RecyclerView.Adapter<OnlineRadioNewsAdapter.NewsRadioHolder> {

    private Cursor mData;
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
    private static final long MINUTE_MILLIS = 1000 * 60;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    @NonNull
    @Override
    public NewsRadioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);

        return new NewsRadioHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRadioHolder holder, int position) {
        mData.moveToPosition(position);

        //String newsId = mData.getString(News.COL_NUM_ID);
        String author = mData.getString(News.COL_NUM_AUTHOR_TITLE);
        // Get the date for displaying
        long dateMillis = mData.getLong(News.COL_NUM_DATE);
        String message = mData.getString(News.COL_NUM_MESSAGE);
        String IDnews = String.valueOf(mData.getInt(News.COL_NUM_ID));
        String date;
        long now = System.currentTimeMillis();

        // Change how the date is displayed depending on whether it was written in the last minute,
        // the hour, etc.
        if (now - dateMillis < (DAY_MILLIS)) {
            if (now - dateMillis < (HOUR_MILLIS)) {
                long minutes = Math.round((now - dateMillis) / MINUTE_MILLIS);
                date = String.valueOf(minutes) + "m";
            } else {
                long minutes = Math.round((now - dateMillis) / HOUR_MILLIS);
                date = String.valueOf(minutes) + "h";
            }
        } else {
            Date dateDate = new Date(dateMillis);
            date = sDateFormat.format(dateDate);
        }

        // Add a dot to the date string
        date = "\u2022 " + date;

        holder.messageTextView.setText(message);
        holder.authorTextView.setText(author);
        holder.dateTextView.setText(date);
        holder.newsID.setText(IDnews);
    }


    @Override
    public int getItemCount() {
        if(null == mData){
            return 0;
        }
        return mData.getCount();
    }

    public void swapCursor(Cursor newCursor){
        mData = newCursor;
        notifyDataSetChanged();
    }

    @SuppressWarnings("WeakerAccess")
    public class NewsRadioHolder extends RecyclerView.ViewHolder{

        final TextView authorTextView;
        final TextView messageTextView;
        final TextView dateTextView;
        final TextView newsID;


        public NewsRadioHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.author_text_view);
            messageTextView = itemView.findViewById(R.id.message_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            newsID = itemView.findViewById(R.id.news_id);
        }
    }
}