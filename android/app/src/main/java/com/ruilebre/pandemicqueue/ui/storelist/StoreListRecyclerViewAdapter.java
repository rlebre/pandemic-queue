package com.ruilebre.pandemicqueue.ui.storelist;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ruilebre.pandemicqueue.R;
import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.ui.store.StoreFragment;
import com.ruilebre.pandemicqueue.utils.TextAdjust;

import java.util.ArrayList;
import java.util.List;

public class StoreListRecyclerViewAdapter extends RecyclerView.Adapter<StoreListRecyclerViewHolder> implements Filterable {
    private List<Store> storeList;
    private List<Store> storeListFiltered;

    public StoreListRecyclerViewAdapter(List<Store> storeList) {
        this.storeList = storeList;
        this.storeListFiltered = storeList;
    }

    @Override
    public StoreListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        return new StoreListRecyclerViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreListRecyclerViewHolder holder, int position) {
        Store store = storeListFiltered.get(position);

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
        return storeListFiltered.size();
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    storeListFiltered = storeList;
                } else {
                    List<Store> filteredList = new ArrayList<>();
                    for (Store store : storeList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (store.getName().toLowerCase().contains(charString) || store.getAddress().toLowerCase().contains(charString) || store.getCity().contains(charString)) {
                            filteredList.add(store);
                        }
                    }

                    storeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = storeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                storeListFiltered = (ArrayList<Store>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
