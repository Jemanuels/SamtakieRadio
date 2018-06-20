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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.SettingsActivity;
import za.co.samtakie.samtakieradio.provider.Contract;
import za.co.samtakie.samtakieradio.provider.RadioAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.RadioAdapterOnClickHandler} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RadioAdapterOnClickHandler mListener;



    private RecyclerView mRecyclerView;
    private RadioAdapter mAdapter;
    private static final int ID_RADIO_LOADER = 10; // loader to load all radio stations

    private static final int ID_RADIO_FAV_LOADER = 20;
    SharedPreferences prefs;



    private String urlRadioLink;
    private int radioID;
    private int radioImg;
    Context context;
    private static final String TAG = MainFragment.class.getSimpleName();

    /* Static variable linked to the column index for passing into the Cursor to get the correct
     *  column data */
    public static final int INDEX_COLUMN_ONLINE_RADIO_ID = 0;
    public static final int INDEX_COLUMN_ONLINE_RADIO_NAME = 1;
    public static final int INDEX_COLUMN_ONLINE_RADIO_LINK = 2;
    public static final int INDEX_COLUMN_ONLINE_RADIO_IMAGE = 3;

    /* The columns of data that we are interested in displaying within our MainActivity's list of
     * Radoi data */
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {





        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Build the url to get all the radio data in the database
        Contract.RadioEntry.buildRadioAll();

        // Load the loader for the data to be be loaded in the recyclerView
        //getLoaderManager().initLoader(ID_RADIO_LOADER, null, this);
        context = getContext();
        Log.d("Main", "oncreate is being called");

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);


        if(prefs.getString("example_list", "Default_List").equals("Default_List")){
            getLoaderManager().initLoader(ID_RADIO_LOADER, null, this);
            Log.d("PrefMain", prefs.getString("example_list", "Default_List"));
        } else {

            //Toast.makeText(this, "You have selected not to play the radio when on Mobile", Toast.LENGTH_LONG).show();
            getLoaderManager().initLoader(ID_RADIO_FAV_LOADER, null, this);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d("PrefMain", "What sits in String s" + s);
        if(s.equals("example_list")) {
            Log.d("PrefMain", "View settings has been changed");
            if (sharedPreferences.getString(s, "Default_List").equals("Default_List")) {
                Log.d("PrefMain", "DEfault has been selected");
                getLoaderManager().initLoader(ID_RADIO_LOADER, null, this);
            } else {
                Log.d("PrefMain", "Load the Fav loader: " + s);
                getLoaderManager().initLoader(ID_RADIO_FAV_LOADER, null, this);
            }
        }else if(s.equals("example_switch")){
            Log.d("PrefMain", "Switch has been selected: " + s);
        }else if(s.equals("example_switch1")) {
            Log.d("PrefMain", "Switch has been selected: " + s);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);



        mRecyclerView = (RecyclerView)  view.findViewById(R.id.recyclerView);



        mRecyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns()));

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RadioAdapter(context, mListener);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

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
                //displayToast("ID Radio Loader request");
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
                //displayToast("ID Radio Loader request for Fav list");
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

        // Todo: Add a loading indicator while the recycler view is loading the data
        // Todo: hide the Loading indicator after the data has been successfully loaded in the recycler view


        mAdapter.swapCursor(data);



        if(data.getCount() != 0){
            // Todo: use this if block to show the list if the data object is greater than 0
        }
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
                //displayToast("List has been clicked!");
                // Start the settings activity when this setting menu item has been clicked
                Intent settingsIntent = new Intent(context, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_share:
                String message = "I'm listening to Samtakie Radio";
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