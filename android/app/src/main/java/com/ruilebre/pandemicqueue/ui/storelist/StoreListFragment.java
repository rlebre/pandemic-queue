package com.ruilebre.pandemicqueue.ui.storelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruilebre.pandemicqueue.R;
import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.services.StoreService;
import com.ruilebre.pandemicqueue.ui.store.StoreFragment;
import com.ruilebre.pandemicqueue.utils.ApiUtils;
import com.ruilebre.pandemicqueue.utils.TextAdjust;

import java.util.LinkedList;
import java.util.List;

public class StoreListFragment extends Fragment {
    private StoreService storeService;
    private StoreListViewModel storeListViewModel;

    public StoreListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        storeService = ApiUtils.getStoreService();
        storeListViewModel = new StoreListViewModel(storeService);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading_store_list);

        RecyclerView recyclerView = view.findViewById(R.id.store_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new StoreListRecyclerViewAdapter(new LinkedList<>()));
        storeListViewModel.getStoreListResult().observe(getViewLifecycleOwner(), storeListResult -> {
            if (storeListResult == null) {
                return;
            }

            if (storeListResult != null) {
                loadingProgressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(new StoreListRecyclerViewAdapter(storeListResult));
            } else {

            }
        });

        loadingProgressBar.setVisibility(View.VISIBLE);
        storeListViewModel.getStoreList();
    }

    private class StoreListRecyclerViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        private ImageView image;
        private TextView name;
        private TextView description;
        private TextView numberWaitingTickets;

        public StoreListRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public StoreListRecyclerViewHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.store_item, container, false));

            cardView = itemView.findViewById(R.id.store_card_view);

            image = itemView.findViewById(R.id.store_image);
            name = itemView.findViewById(R.id.store_name);
            description = itemView.findViewById(R.id.store_location);
            numberWaitingTickets = itemView.findViewById(R.id.store_number_waiting_tickets);

        }
    }

    private class StoreListRecyclerViewAdapter extends RecyclerView.Adapter<StoreListRecyclerViewHolder> {
        private List<Store> storeList;

        public StoreListRecyclerViewAdapter(List<Store> storeList) {
            this.storeList = storeList;
        }

        @Override
        public StoreListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new StoreListRecyclerViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull StoreListRecyclerViewHolder holder, int position) {
            Store store = storeList.get(position);

            holder.image.setImageResource(findStore(store.getParentStore()));
            holder.name.setText(TextAdjust.toTitleCase(store.getName()));
            holder.description.setText(TextAdjust.toTitleCase(store.getCity()));
            holder.numberWaitingTickets.setText(String.valueOf(store.getnWaiting()));

            holder.cardView.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                StoreFragment myFragment = StoreFragment.newInstance(store);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
            });
        }

        @Override
        public int getItemCount() {
            return storeList.size();
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


}
