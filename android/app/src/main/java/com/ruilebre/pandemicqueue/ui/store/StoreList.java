package com.ruilebre.pandemicqueue.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruilebre.pandemicqueue.R;
import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.services.StoreService;
import com.ruilebre.pandemicqueue.utils.ApiUtils;
import com.ruilebre.pandemicqueue.utils.TextAdjust;

import java.util.List;

public class StoreList extends Fragment {
    private StoreService storeService;
    private StoreListViewModel storeListViewModel;

    public StoreList() {
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

        storeListViewModel.getStoreListResult().observe(getViewLifecycleOwner(), storeListResult -> {
            if (storeListResult == null) {
                return;
            }

            if (storeListResult != null) {
                loadingProgressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(new RecyclerViewAdapter(storeListResult));
            } else {

            }
        });

        loadingProgressBar.setVisibility(View.VISIBLE);
        storeListViewModel.getStoreList();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private CardView storeCardView;
        private TextView name;
        private TextView description;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.store_item, container, false));

            storeCardView = itemView.findViewById(R.id.store_card_view);
            name = itemView.findViewById(R.id.store_name);
            description = itemView.findViewById(R.id.store_location);
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        private List<Store> storeList;

        public RecyclerViewAdapter(List<Store> storeList) {
            this.storeList = storeList;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new RecyclerViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            Store store = storeList.get(position);

            holder.name.setText(TextAdjust.toTitleCase(store.getName()));
            holder.description.setText(TextAdjust.toTitleCase(store.getCity()));
        }

        @Override
        public int getItemCount() {
            return storeList.size();
        }
    }
}
