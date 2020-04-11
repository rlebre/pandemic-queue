package com.ruilebre.pandemicqueue.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.MapView;
import com.ruilebre.pandemicqueue.R;
import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.utils.TextAdjust;

import java.text.Format;
import java.text.SimpleDateFormat;


public class StoreFragment extends Fragment {
    private static final String STORE_PARAM = "STORE";

    private ImageView storeDetailsImageView;
    private TextView storeNameTextView;
    private TextView storeLocationTextView;
    private TextView storeNumberTicketsWaitingTextView;
    private TextView storeLastOnQueueTextView;
    private TextView storeLastEnteredStoreTextView;
    private TextView storeCapacityTextView;
    private MapView storeLocationMapView;
    private Button storeTicketButton;

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
        if (getArguments() != null) {
            store = (Store) getArguments().getSerializable(STORE_PARAM);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storeDetailsImageView = view.findViewById(R.id.store_details_image);
        storeNameTextView = view.findViewById(R.id.store_details_name);
        storeLocationTextView = view.findViewById(R.id.store_details_location);
        storeNumberTicketsWaitingTextView = view.findViewById(R.id.store_details_tickets_waiting);
        storeLastOnQueueTextView = view.findViewById(R.id.store_details_lastOnQueue);
        storeLastEnteredStoreTextView = view.findViewById(R.id.store_details_lastEnteredStore);
        storeCapacityTextView = view.findViewById(R.id.store_details_capacity);
        storeLocationMapView = view.findViewById(R.id.store_details_mapView);
        storeTicketButton = view.findViewById(R.id.button_ticket);

        TextView labelLastOnQueue = view.findViewById(R.id.labelLastOnQueue);
        TextView labelLastEnteredStore = view.findViewById(R.id.labelLastEnteredStore);

        storeDetailsImageView.setImageResource(findStore(store.getParentStore()));
        storeNameTextView.setText(TextAdjust.toTitleCase(store.getName()));
        storeLocationTextView.setText(TextAdjust.toTitleCase(store.getCity()));
        storeNumberTicketsWaitingTextView.setText(String.valueOf(store.getnWaiting()));

        Format f = new SimpleDateFormat("HH:mm");

        if (store.getLastOnQueue() != null) {
            storeLastOnQueueTextView.setText(f.format(store.getLastOnQueue()));
            storeLastOnQueueTextView.setVisibility(View.VISIBLE);
            labelLastOnQueue.setVisibility(View.VISIBLE);
        }

        if (store.getLastOnQueue() != null) {
            storeLastEnteredStoreTextView.setText(f.format(store.getLastEnteredStore()));
            storeLastEnteredStoreTextView.setVisibility(View.VISIBLE);
            labelLastEnteredStore.setVisibility(View.VISIBLE);
        }

        storeCapacityTextView.setText(String.valueOf(store.getCapacity()));
    }

    private int findStore(String parentStore) {
        switch (parentStore) {
            case "pingodoce":
                return R.drawable.ic_pingo_doce;
            case "auchan":
                return R.drawable.ic_auchan;
            case "continente":
            case "meusuper":
                return R.drawable.ic_continente;
            case "dia":
                return R.drawable.ic_dia;
            case "lidl":
                return R.drawable.ic_lidl;
            case "spar":
                return R.drawable.ic_spar;
        }
        return 0;
    }
}
