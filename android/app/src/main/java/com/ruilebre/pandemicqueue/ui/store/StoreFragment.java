package com.ruilebre.pandemicqueue.ui.store;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ruilebre.pandemicqueue.R;
import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.services.StoreService;
import com.ruilebre.pandemicqueue.services.TicketService;
import com.ruilebre.pandemicqueue.utils.ApiUtils;
import com.ruilebre.pandemicqueue.utils.TextAdjust;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;


public class StoreFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "StoreFragment";
    private static final String STORE_PARAM = "STORE";

    private ImageView storeDetailsImageView;
    private TextView storeNameTextView;
    private TextView storeLocationTextView;
    private TextView storeNumberTicketsWaitingTextView;
    private TextView storeLastOnQueueTextView;
    private TextView storeLastEnteredStoreTextView;
    private TextView storeCapacityTextView;
    private MapView storeLocationMapView;
    private Button storeGetTicketButton;
    private Button storeCancelTicketButton;

    private TicketService ticketService;
    private StoreService storeService;
    private StoreViewModel storeViewModel;

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
        View view = inflater.inflate(R.layout.fragment_store_details, container, false);

        if (getArguments() != null) {
            store = (Store) getArguments().getSerializable(STORE_PARAM);
        }

        storeLocationMapView = view.findViewById(R.id.store_details_mapView);
        initGoogleMap(savedInstanceState);

        ticketService = ApiUtils.getTicketService();
        storeService = ApiUtils.getStoreService();
        storeViewModel = new StoreViewModel(ticketService, storeService, store);

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
        storeGetTicketButton = view.findViewById(R.id.button_create_ticket);
        storeCancelTicketButton = view.findViewById(R.id.button_cancel_ticket);
        TextView labelLastOnQueue = view.findViewById(R.id.labelLastOnQueue);
        TextView labelLastEnteredStore = view.findViewById(R.id.labelLastEnteredStore);

        storeViewModel.getCreateTicketResult().observe(getViewLifecycleOwner(), status -> {
            if (status == null) {
                return;
            } else {
                if (status.getStatus()) {
                    Toast.makeText(getContext(), getString(R.string.ticket_reserved), Toast.LENGTH_LONG).show();
                    storeGetTicketButton.setVisibility(View.GONE);
                    storeCancelTicketButton.setVisibility(View.VISIBLE);
                } else {
                    //TODO: Check how can discard session
                    getActivity().recreate();
                }
            }
        });

        storeViewModel.getCheckTicketResult().observe(getViewLifecycleOwner(), statusCheckTicket -> {
            if (statusCheckTicket == null) {
                return;
            } else {
                if (statusCheckTicket.getTicketExists()) {
                    storeGetTicketButton.setVisibility(View.GONE);
                    storeCancelTicketButton.setVisibility(View.VISIBLE);
                } else {
                    //TODO: Check how can discard session
                    getActivity().recreate();
                }
            }
        });

        storeViewModel.getCancelTicketResult().observe(getViewLifecycleOwner(), status -> {
            if (status == null) {
                return;
            } else {
                if (status.getStatus()) {
                    Toast.makeText(getContext(), getString(R.string.ticket_canceled), Toast.LENGTH_LONG).show();
                    storeGetTicketButton.setVisibility(View.VISIBLE);
                    storeCancelTicketButton.setVisibility(View.GONE);
                } else {
                    //TODO: Check how can discard session
                    getActivity().recreate();
                }
            }
        });

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

        if (store.getLastEnteredStore() != null) {
            storeLastEnteredStoreTextView.setText(f.format(store.getLastEnteredStore()));
            storeLastEnteredStoreTextView.setVisibility(View.VISIBLE);
            labelLastEnteredStore.setVisibility(View.VISIBLE);
        }

        storeCapacityTextView.setText(String.valueOf(store.getCapacity()));
        storeGetTicketButton.setOnClickListener(v -> {
            storeViewModel.createTicket();
        });

        storeCancelTicketButton.setOnClickListener(v -> {
            storeViewModel.cancelTicket();
        });

        storeViewModel.checkTicket();
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

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("AIzaSyAYEHnK6GKTon6Ha65T_akUz3i8O8vHWKI");
        }

        storeLocationMapView.onCreate(mapViewBundle);

        storeLocationMapView.getMapAsync(this);
        storeLocationMapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            List<Address> addressList = null;
            Geocoder geocoder = new Geocoder(getContext());
            try {
                addressList = geocoder.getFromLocationName(store.getAddress(), 1);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

            LatLng addressCoordinates = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(addressCoordinates);
            markerOptions.title(TextAdjust.toTitleCase(store.getName()));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker());

            Marker marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(marker.getPosition());
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);

            googleMap.moveCamera(cu);
            googleMap.animateCamera(zoom);
        }
    }
}
