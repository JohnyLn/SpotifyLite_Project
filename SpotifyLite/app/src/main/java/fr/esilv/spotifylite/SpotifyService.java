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

    @GET("/search?type=track")
    void searchTracks(@Query("q") String q, Callback<SpotifySearchResponse> callback);


    //@GET("search?part=snippet&type=video&maxResults=50")

    //Call<SpotifySearchResponse> search(@Query("q") String query, @Query("key") String apiKey);



    //https://accounts.spotify.com/authorize?client_id=3b5446bb2fee48b787fef966f279592d&response_type=code&redirect_uri=fr.esilv.spotifylite%3A%2F%2Fcallback

    //https://accounts.spotify.com/authorize?client_id=3b5446bb2fee48b787fef966f279592d&response_type=code&redirect_uri=fr.esilv.spotifylite%3A%2F%2Fcallback
}
