package fr.esilv.spotifylite.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import fr.esilv.spotifylite.R;
import fr.esilv.spotifylite.models.Snippet;
import fr.esilv.spotifylite.models.SpotifySearchItem;

public class SearchItemViewHolder extends RecyclerView.ViewHolder {



    private TextView  name;
    private TextView  album;
    private TextView  artist;
    private TextView  uri;

    public SearchItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.name);
        this.album = itemView.findViewById(R.id.album);
        this.artist= itemView.findViewById(R.id.artist);
        this.uri =itemView.findViewById(R.id.uri);

    }

    public void bind(final SpotifySearchItem SpotifySearchItem) {
        final Snippet snippet = SpotifySearchItem.getSnippet();

        name.setText(snippet.getname());
        album.setText(snippet.getalbum());
        artist.setText(snippet.getartist());
        uri.setText(snippet.getUri_music());


    }





}
