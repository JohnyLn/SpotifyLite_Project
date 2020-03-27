package fr.esilv.spotifylite;



import fr.esilv.spotifylite.models.SpotifySearchResponse;
import retrofit2.Call;

import retrofit2.http.GET;

import retrofit2.http.Query;



import retrofit2.Callback;




public interface SpotifyService {
    //Query Search an Item
    //https://developer.spotify.com/documentation/web-api/reference/search/search/
    @GET("search?type=track&limit=20")
    Call<SpotifySearchResponse> search(@Query("q") String query,@Query("key") String apiKey);

}
