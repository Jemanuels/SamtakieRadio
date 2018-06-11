package za.co.samtakie.samtakieradio.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.View;
import android.view.ViewGroup;

import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.data.Contract;
import za.co.samtakie.samtakieradio.data.RadioAdapter;
import za.co.samtakie.samtakieradio.data.RadioWidgetAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemFragment.RadioWidgetAdapterOnClickHandler} interface
 * to handle interaction events.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RadioWidgetAdapterOnClickHandler mListener;

    private RecyclerView mRecyclerView;
    private RadioWidgetAdapter mAdapter;
    private static final int ID_RADIO_LOADER = 10; // loader to load all radio stations
    private String urlRadioLink;
    private int radioID;
    private int radioImg;
    private static final String TAG = ItemFragment.class.getSimpleName();

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


    public ItemFragment() {
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
    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // Build the url to get all the radio data in the database
        Contract.RadioEntry.buildRadioAll();

        // Load the loader for the data to be be loaded in the recyclerView
        getLoaderManager().initLoader(ID_RADIO_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mRecyclerView = (RecyclerView)  view.findViewById(R.id.recyclerView);

        Context context = view.getContext();

        mRecyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns()));

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RadioWidgetAdapter(getActivity(), mListener);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthDivider = 600;
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
        if (context instanceof RadioWidgetAdapterOnClickHandler) {
            mListener = (RadioWidgetAdapterOnClickHandler) context;
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
                return new CursorLoader(getActivity(),
                        radioQueryUri,
                        MAIN_RADIO_PROJECTION,
                        null,
                        null,
                        null);

                default:
                    throw new RuntimeException("Error loading the data " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data.getCount() != 0){
            Log.d("ItemFragment", "data has more than on data");
        }
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
    public interface RadioWidgetAdapterOnClickHandler {
        void radioItemOnClickHandler(int radioID, View view, int adapterPosition, String radioLink, String radioName, String radioImage);
    }
}
