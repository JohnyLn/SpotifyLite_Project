package fr.esilv.spotifylite.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.esilv.spotifylite.R;
import fr.esilv.spotifylite.models.SpotifySearchItem;
import fr.esilv.spotifylite.viewholders.SearchItemViewHolder;
import android.util.Log;

public class SpotifySearchItemAdapter extends RecyclerView.Adapter<SearchItemViewHolder> {

    private final List<SpotifySearchItem> items;

    public SpotifySearchItemAdapter(List<SpotifySearchItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_search_item, viewGroup, false);
        Log.d("SpotifySearchItAd","view_search_item launced");
        return new SearchItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemViewHolder searchItemViewHolder, int position) {
        searchItemViewHolder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }



}
