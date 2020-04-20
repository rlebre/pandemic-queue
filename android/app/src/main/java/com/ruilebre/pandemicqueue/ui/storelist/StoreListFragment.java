package com.ruilebre.pandemicqueue.ui.storelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruilebre.pandemicqueue.R;
import com.ruilebre.pandemicqueue.services.StoreService;
import com.ruilebre.pandemicqueue.utils.ApiUtils;

import java.util.LinkedList;

public class StoreListFragment extends Fragment {
    private StoreService storeService;
    private StoreListViewModel storeListViewModel;

    private StoreListRecyclerViewAdapter adapter;

    public StoreListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

                adapter = new StoreListRecyclerViewAdapter(storeListResult);
                recyclerView.setAdapter(adapter);
            } else {

            }
        });

        loadingProgressBar.setVisibility(View.VISIBLE);
        storeListViewModel.getStoreList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView;
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                if (adapter != null) {
                    adapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if (adapter != null) {
                    adapter.getFilter().filter(query);
                }
                return false;
            }
        });
    }
}
