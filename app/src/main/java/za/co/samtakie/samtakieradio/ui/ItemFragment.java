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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.provider.Contract;
import za.co.samtakie.samtakieradio.provider.RadioWidgetAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemFragment.RadioWidgetAdapterOnClickHandler} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
@SuppressWarnings("unused")
public class ItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RadioWidgetAdapterOnClickHandler mListener;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    private RadioWidgetAdapter mAdapter;
    private static final int ID_RADIO_LOADER = 10; // loader to load all radio stations
    /* The columns of data that we are interested in displaying within our MainActivity's list of
     * Radio data */
    private static final String[] MAIN_RADIO_PROJECTION = {
            Contract.RadioEntry._ID,

            Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_LINK,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_IMAGE,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID
    };

    public ItemFragment() {
        // Required empty public constructor
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        ButterKnife.bind(this, view);

        Context context = view.getContext();

        mRecyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns()));

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RadioWidgetAdapter(getActivity(), mListener);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @SuppressWarnings("ConstantConditions")
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

    @SuppressWarnings("ConstantConditions")
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
    @SuppressWarnings("unused")
    public interface RadioWidgetAdapterOnClickHandler {
        void radioItemOnClickHandler(int radioID, View view, int adapterPosition, String radioLink, String radioName, String radioImage);
    }
}