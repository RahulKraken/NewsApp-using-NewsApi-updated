package com.krakn.scoophour.ViewPagerFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.krakn.scoophour.ContentRecievers.DataModel;
import com.krakn.scoophour.ContentRecievers.NetworkUtils;
import com.krakn.scoophour.R;
import com.krakn.scoophour.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class General extends Fragment {

    View rootView;

    // This is the unique id of the fragment.
    private final int FRAG_ID = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_general, container, false);
        new GeneralNetworkTask().execute(FRAG_ID);
        return rootView;
    }

    class GeneralNetworkTask extends AsyncTask<Integer, Integer, Void> {

        // This is the data model that we are going to use to populate the recycler view.
        private List<DataModel> data;
        ProgressBar progressBar;

        @Override
        protected Void doInBackground(Integer... integers) {
            data = new ArrayList<>();
            NetworkUtils utils = new NetworkUtils();
            data = utils.returnContent(integers[0], null);
            for (DataModel dataModel : data) {
                Log.d("TAG", dataModel.getTitle() + "\t" + dataModel.getArticleUrl());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = rootView.findViewById(R.id.generalProgress_bar);
            progressBar.setVisibility(View.VISIBLE);
        }

        // This method is invoked at the end of Async task and will be used to inflate the recyclerView.
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
            RecyclerView recyclerView = rootView.findViewById(R.id.generalRecyclerView);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(data, getContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}

