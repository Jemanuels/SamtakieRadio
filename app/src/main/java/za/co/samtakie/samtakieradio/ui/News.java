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
package za.co.samtakie.samtakieradio.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.provider.Contract;
import za.co.samtakie.samtakieradio.provider.OnlineRadioNewsAdapter;

@SuppressWarnings({"CanBeFinal", "FieldCanBeLocal"})
public class News extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int COL_NUM_ID= 0;
    public static final int COL_NUM_AUTHOR_TITLE = 1;
    public static final int COL_NUM_DATE = 2;
    public static final int COL_NUM_MESSAGE = 3;

    private static final String[] MESSAGES_PROJECTION = {
            Contract.RadioEntry._ID,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_NEWS_TITLE,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_NEWS_DATE,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_NEWS_MESSAGE
    };

    private static final int LOADER_ID_MESSAGES = 0;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private OnlineRadioNewsAdapter mAdapter;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


        mRecyclerView = findViewById(R.id.news_recycler_view);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Add dividers
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // Specify an adapter
        mAdapter = new OnlineRadioNewsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        int swipeDirs = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP, swipeDirs) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                TextView newsID = mRecyclerView.findViewHolderForAdapterPosition(viewHolder.getAdapterPosition()).itemView.findViewById(R.id.news_id);
                String news_id = newsID.getText().toString();
                String[] mProjection = {Contract.RadioEntry._ID};
                String mSelectionClause = Contract.RadioEntry._ID + " = ?";
                String[] selectionArgs = {""};

                selectionArgs[0] = news_id;

                @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(
                        Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_NEWS,
                        mProjection,
                        mSelectionClause,
                        selectionArgs,
                        null);

                // check and make sure the online radio doesn't exits in the fav table
                // if it is, ignore adding the data and inform the user.
                assert cursor != null;
                if (cursor.getCount() != 0) {
                    getContentResolver().delete(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_NEWS, mSelectionClause, selectionArgs);
                }

                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // Start the loader
        getSupportLoaderManager().initLoader(LOADER_ID_MESSAGES, null, this);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this,
                Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_NEWS,
                MESSAGES_PROJECTION,
                null,
                null,
                Contract.RadioEntry.COLUMN_ONLINE_RADIO_NEWS_DATE + " DESC");

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu layout
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get the menu id
        int id = item.getItemId();

        // check which menu item has been clicked and perform the switch case action
        switch (id){
            case R.id.action_settings:
                // Start the settings activity when this setting menu item has been clicked
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_share:
                String message = getString(R.string.share_message);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(share);
                return true;

            case R.id.action_news:
                Intent news = new Intent(this, News.class);
                startActivity(news);
                return true;

            default:
                // Do nothing for the time being

        }
        return super.onOptionsItemSelected(item);
    }
}