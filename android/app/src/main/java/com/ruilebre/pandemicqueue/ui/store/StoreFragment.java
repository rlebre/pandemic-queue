package com.ruilebre.pandemicqueue.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ruilebre.pandemicqueue.R;
import com.ruilebre.pandemicqueue.data.models.Store;


public class StoreFragment extends Fragment {
    private static final String STORE_PARAM = "STORE";

    private Store store;

    public StoreFragment() {
        // Required empty public constructor
    }


    public static StoreFragment newInstance(Store param1) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putSerializable(STORE_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            store = (Store) getArguments().getSerializable(STORE_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        return view;
    }
}
