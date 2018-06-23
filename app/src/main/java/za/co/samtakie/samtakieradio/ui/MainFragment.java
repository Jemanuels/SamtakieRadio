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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.provider.Contract;
import za.co.samtakie.samtakieradio.provider.RadioAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.RadioAdapterOnClickHandler} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener{
     private RadioAdapterOnClickHandler mListener;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    private RadioAdapter mAdapter;
    private static final int ID_RADIO_LOADER = 10; // loader to load all radio stations
    private static final int ID_RADIO_FAV_LOADER = 20;
    private SharedPreferences prefs;
    private Context context;

    /* The columns of data that we are interested in displaying within our MainActivity's list of
     * Radio data */
    public static final String[] MAIN_RADIO_PROJECTION = {
            Contract.RadioEntry._ID,

            Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_LINK,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_IMAGE,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID
    };

    //private static final int ID_RADIO_LOADER = 37;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Build the url to get all the radio data in the database
        Contract.RadioEntry.buildRadioAll();

        // Load the loader for the data to be be loaded in the recyclerView
        //getLoaderManager().initLoader(ID_RADIO_LOADER, null, this);
        context = getContext();

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);


        if(prefs.getString("example_list", "Default_List").equals("Default_List")){
            getLoaderManager().initLoader(ID_RADIO_LOADER, null, this);
        } else {
            getLoaderManager().initLoader(ID_RADIO_FAV_LOADER, null, this);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "example_list":
                if (sharedPreferences.getString(s, "Default_List").equals("Default_List")) {
                    getLoaderManager().initLoader(ID_RADIO_LOADER, null, this);
                } else {
                    getLoaderManager().initLoader(ID_RADIO_FAV_LOADER, null, this);
                }
                break;
            case "example_switch":
                // do nothing at the moment
                break;
            case "example_switch1":
                 // do nothing at the moment
                break;

            default:
                // Do nothing
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns()));

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RadioAdapter(context, mListener);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @SuppressWarnings("ConstantConditions")
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthDivider = 300;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if(nColumns < 2){
            return 1;
        }
        return nColumns;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.radioItemOnClickHandler(0, null, 0, null, null, null);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RadioAdapterOnClickHandler) {
            mListener = (RadioAdapterOnClickHandler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id){
            case ID_RADIO_LOADER:
                Uri radioQueryUri = Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO;
                String sortRadio = Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME + " ASC";

                return new CursorLoader(
                        context,
                        radioQueryUri,
                        MAIN_RADIO_PROJECTION,
                        null,
                        null,
                        sortRadio);

            case ID_RADIO_FAV_LOADER:
                Uri radioFavQueryUri = Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV;
                String sortRadioFav = Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME + " ASC";

                return new CursorLoader(
                        context,
                        radioFavQueryUri,
                        MAIN_RADIO_PROJECTION,
                        null,
                        null,
                        sortRadioFav);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface RadioAdapterOnClickHandler {
        void radioItemOnClickHandler(int radioID, View view, int adapterPosition, String radioLink, String radioName, String radioImage);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu layout
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get the menu id
        int id = item.getItemId();

        // check which menu item has been clicked and perform the switch case action
        switch (id){
            case R.id.action_settings:
                // Start the settings activity when this setting menu item has been clicked
                Intent settingsIntent = new Intent(context, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_share:
                String message = getString(R.string.share_message);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(share);
                return true;

            default:
                // Do nothing for the time being

        }
        return super.onOptionsItemSelected(item);
    }
}